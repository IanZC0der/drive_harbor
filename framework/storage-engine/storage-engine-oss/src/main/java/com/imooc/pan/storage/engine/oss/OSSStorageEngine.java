package com.imooc.pan.storage.engine.oss;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.exception.driveHarborFrameworkException;
import com.imooc.pan.core.utils.FileUtil;
import com.imooc.pan.core.utils.UUIDUtil;
import com.imooc.pan.storage.engine.core.AbstractStorageEngine;
import com.imooc.pan.storage.engine.core.context.*;
import com.imooc.pan.storage.engine.oss.config.OssStorageEngineConfig;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OSSStorageEngine extends AbstractStorageEngine {

    private static final String IDENTIFIER_KEY = "identifier";

    private static final String UPLOAD_ID_KEY = "uploadId";

    private static final String USER_ID_KEY = "userId";

    private static final String PART_NUMBER_KEY = "partNumber";

    private static final String E_TAG_KEY = "eTag";

    private static final String PART_SIZE_KEY = "partSize";

    private static final String PART_CRC_KEY = "partCRC";

    private static final Integer TEN_THOUSAND_INT = 10000;
    private static final String CACHE_KEY_TEMPLATE = "oss_cache_upload_id_%s_%s";
    @Autowired
    private OssStorageEngineConfig config;

    @Autowired
    private OSSClient client;
    @Override
    protected void doStore(StoreFileContext context) throws IOException {
        String realPath = getFilePath(FileUtil.getFileSuffix(context.getFilename()));
        client.putObject(config.getBucketName(), realPath, context.getInputStream());
        context.setRealPath(realPath);

    }

    /**
     * 1. get real path
     * 2. if it's a chunk, abort the upload request
     * @param context
     * @throws IOException
     */
    @Override
    protected void doDelete(DeleteFileContext context) throws IOException {

        List<String> realFilePathList = context.getRealFilePathList();
        realFilePathList.stream().forEach(realPath -> {

            // get the path of the chunk and abort the upload request
            if (checkHaveParams(realPath)) {
                JSONObject params = analysisUrlParams(realPath);
                if (Objects.nonNull(params) && !params.isEmpty()) {
                    String uploadId = params.getString(UPLOAD_ID_KEY);
                    String identifier = params.getString(IDENTIFIER_KEY);
                    Long userId = params.getLong(USER_ID_KEY);
                    String cacheKey = getCacheKey(identifier, userId);

                    getCache().evict(cacheKey);

                    try {
                        AbortMultipartUploadRequest request = new AbortMultipartUploadRequest(config.getBucketName(), getBaseUrl(realPath), uploadId);
                        client.abortMultipartUpload(request);
                    } catch (Exception e) {

                    }
                }
            }
            // non-chunk file deletion
            else {
                client.deleteObject(config.getBucketName(), realPath);
            }

        });


    }


    /**
     *
     * 1. initialization, get upload id
     * 2. upload the chunks, each chunk has the upload id
     * 3. upload success, merge chunks
     *
     * thread lock
     * share the upload id
     *
     * solution:
     * 1. thread lock to lock the thread
     * 2. caching: distributed systems require distributed caching
     * 3.consider the edge cases where the uploading is interrupted
     *
     * @param context
     * @throws IOException
     */
    @Override
    protected synchronized void doStoreChunk(StoreFileChunkContext context) throws IOException {

        if (context.getTotalChunks() > TEN_THOUSAND_INT) {
            throw new driveHarborFrameworkException("The number of chunks should not be more than ： " + TEN_THOUSAND_INT);
        }

        String cacheKey = getCacheKey(context.getIdentifier(), context.getUserId());

        ChunkUploadEntity entity = getCache().get(cacheKey, ChunkUploadEntity.class);

        if (Objects.isNull(entity)) {
            entity = initChunkUpload(context.getFilename(), cacheKey);
        }

        UploadPartRequest request = new UploadPartRequest();
        request.setBucketName(config.getBucketName());
        request.setKey(entity.getObjectKey());
        request.setUploadId(entity.getUploadId());
        request.setInputStream(context.getInputStream());
        request.setPartSize(context.getCurrentChunkSize());
        request.setPartNumber(context.getChunkNumber());

        UploadPartResult result = client.uploadPart(request);

        if (Objects.isNull(result)) {
            throw new driveHarborFrameworkException("Upload failure");
        }

        PartETag partETag = result.getPartETag();

        // assemble chunks url
        JSONObject params = new JSONObject();
        params.put(IDENTIFIER_KEY, context.getIdentifier());
        params.put(UPLOAD_ID_KEY, entity.getUploadId());
        params.put(USER_ID_KEY, context.getUserId());
        params.put(PART_NUMBER_KEY, partETag.getPartNumber());
        params.put(E_TAG_KEY, partETag.getETag());
        params.put(PART_SIZE_KEY, partETag.getPartSize());
        params.put(PART_CRC_KEY, partETag.getPartCRC());

        String realPath = assembleUrl(entity.getObjectKey(), params);

        context.setRealPath(realPath);

    }

    /**
     * merge the chunks
     * @param context
     * @throws IOException
     */
    @Override
    protected void doMergeFile(MergeFileContext context) throws IOException{
        String cacheKey = getCacheKey(context.getIdentifier(), context.getUserId());

        ChunkUploadEntity entity = getCache().get(cacheKey, ChunkUploadEntity.class);

        if (Objects.isNull(entity)) {
            throw new driveHarborFrameworkException("merging failure, the file identifier is：" + context.getIdentifier());
        }

        List<String> chunkPaths = context.getRealPathList();
        List<PartETag> partETags = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(chunkPaths)) {
            partETags = chunkPaths.stream()
                    .filter(StringUtils::isNotBlank)
                    .map(this::analysisUrlParams)
                    .filter(Objects::nonNull)
                    .filter(jsonObject -> !jsonObject.isEmpty())
                    .map(jsonObject -> new PartETag(jsonObject.getIntValue(PART_NUMBER_KEY),
                            jsonObject.getString(E_TAG_KEY),
                            jsonObject.getLongValue(PART_SIZE_KEY),
                            jsonObject.getLong(PART_CRC_KEY)
                    )).collect(Collectors.toList());
        }

        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(config.getBucketName(), entity.getObjectKey(), entity.uploadId, partETags);
        CompleteMultipartUploadResult result = client.completeMultipartUpload(request);
        if (Objects.isNull(result)) {
            throw new driveHarborFrameworkException("merging failure, the file identifier is: " + context.getIdentifier());
        }

        getCache().evict(cacheKey);

        context.setRealPath(entity.getObjectKey());

    }

    @Override
    protected void doReadFile(ReadFileContext context) throws IOException {

        OSSObject ossObject = client.getObject(config.getBucketName(), context.getRealPath());
        if (Objects.isNull(ossObject)) {
            throw new driveHarborFrameworkException("Read file failure, file name is: " + context.getRealPath());
        }
        FileUtil.writeStream2StreamNormal(ossObject.getObjectContent(), context.getOutputStream());

    }

    /**
     * chunk upload entity
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static class ChunkUploadEntity implements Serializable {

        private static final long serialVersionUID = -2478112662029396728L;
        /**
         * global uploadId
         */
        private String uploadId;

        /**
         * object key
         */
        private String objectKey;

    }

    /**
     * get file path
     * y/m/d/UUID.fileSuffix
     *
     * @param fileSuffix
     * @return
     */
    private String getFilePath(String fileSuffix) {
        return new StringBuffer()
                .append(DateUtil.thisYear())
                .append(driveHarborConstants.SLASH_STR)
                .append(DateUtil.thisMonth() + 1)
                .append(driveHarborConstants.SLASH_STR)
                .append(DateUtil.thisDayOfMonth())
                .append(driveHarborConstants.SLASH_STR)
                .append(UUIDUtil.getUUID())
                .append(fileSuffix)
                .toString();
    }
    /**
     * upload chunk cache key
     *
     * @param identifier
     * @param userId
     * @return
     */
    private String getCacheKey(String identifier, Long userId) {
        return String.format(CACHE_KEY_TEMPLATE, identifier, userId);
    }

    /**
     * initialize the upload request
     * 1. initialize the request
     * 2. save it to the cache
     *
     * @param filename
     * @param cacheKey
     * @return
     */
    private ChunkUploadEntity initChunkUpload(String filename, String cacheKey) {
        String filePath = getFilePath(filename);

        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(config.getBucketName(), filePath);
        InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);

        if (Objects.isNull(result)) {
            throw new driveHarborFrameworkException("Upload initialization failure.");
        }

        ChunkUploadEntity entity = new ChunkUploadEntity();
        entity.setObjectKey(filePath);
        entity.setUploadId(result.getUploadId());

        getCache().put(cacheKey, entity);

        return entity;
    }

    private String assembleUrl(String baseUrl, JSONObject params) {
        if (Objects.isNull(params) || params.isEmpty()) {
            return baseUrl;
        }
        StringBuffer urlStringBuffer = new StringBuffer(baseUrl);
        urlStringBuffer.append(driveHarborConstants.QUESTION_MARK_STR);
        List<String> paramsList = Lists.newArrayList();
        StringBuffer urlParamsStringBuffer = new StringBuffer();
        params.entrySet().forEach(entry -> {
            urlParamsStringBuffer.setLength(driveHarborConstants.ZERO_INT);
            urlParamsStringBuffer.append(entry.getKey());
            urlParamsStringBuffer.append(driveHarborConstants.EQUALS_MARK_STR);
            urlParamsStringBuffer.append(entry.getValue());
            paramsList.add(urlParamsStringBuffer.toString());
        });
        return urlStringBuffer.append(Joiner.on(driveHarborConstants.AND_MARK_STR).join(paramsList)).toString();
    }

    /**
     * get the keys in the url
     * split the keys using brackets
     *
     * @param mark
     * @return
     */
    private String getSplitMark(String mark) {
        return new StringBuffer(driveHarborConstants.LEFT_BRACKET_STR)
                .append(mark)
                .append(driveHarborConstants.RIGHT_BRACKET_STR)
                .toString();
    }

    /**
     * get base url
     *
     * @param url
     * @return
     */
    private String getBaseUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return driveHarborConstants.EMPTY_STR;
        }
        if (checkHaveParams(url)) {
            return url.split(getSplitMark(driveHarborConstants.QUESTION_MARK_STR))[0];
        }
        return url;
    }

    /**
     * analyze url
     *
     * @param url
     * @return
     */
    private JSONObject analysisUrlParams(String url) {
        JSONObject result = new JSONObject();
        if (!checkHaveParams(url)) {
            return result;
        }
        String paramsPart = url.split(getSplitMark(driveHarborConstants.QUESTION_MARK_STR))[1];
        if (StringUtils.isNotBlank(paramsPart)) {
            List<String> paramPairList = Splitter.on(driveHarborConstants.AND_MARK_STR).splitToList(paramsPart);
            paramPairList.stream().forEach(paramPair -> {
                String[] paramArr = paramPair.split(getSplitMark(driveHarborConstants.EQUALS_MARK_STR));
                if (paramArr != null && paramArr.length == driveHarborConstants.TWO_INT) {
                    result.put(paramArr[0], paramArr[1]);
                }
            });
        }
        return result;
    }

    /**
     * check if the url has keys
     *
     * @param url
     * @return
     */
    private boolean checkHaveParams(String url) {
        return StringUtils.isNotBlank(url) && url.indexOf(driveHarborConstants.QUESTION_MARK_STR) != driveHarborConstants.MINUS_ONE_INT;
    }

}
