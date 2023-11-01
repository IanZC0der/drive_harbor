package com.imooc.pan.storage.engine.gcs;
import cn.hutool.core.date.DateUtil;
import com.google.cloud.storage.*;
import com.google.common.io.ByteStreams;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.utils.FileUtil;
import com.imooc.pan.core.utils.UUIDUtil;
import com.imooc.pan.storage.engine.core.AbstractStorageEngine;
import com.imooc.pan.storage.engine.core.context.*;
import com.imooc.pan.storage.engine.gcs.config.GCSStorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GCSStorageEngine extends AbstractStorageEngine{
    @Autowired
    private GCSStorageConfig gcsStorageConfig;

    @Autowired
    private Storage storageClient;

    @Override
    protected void doStore(StoreFileContext context) throws IOException {
        String realPath = getFilePath(FileUtil.getFileSuffix(context.getFilename()));
        Bucket bucket = storageClient.get(gcsStorageConfig.getBucketName());
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(gcsStorageConfig.getBucketName(), realPath)).build();
        Blob blob = storageClient.create(blobInfo, ByteStreams.toByteArray(context.getInputStream()));
    }

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

    @Override
    protected void doDelete(DeleteFileContext context) throws IOException {

    }

    @Override
    protected void doStoreChunk(StoreFileChunkContext context) throws IOException {

    }

    @Override
    protected void doMergeFile(MergeFileContext context) throws IOException {

    }

    @Override
    protected void doReadFile(ReadFileContext context) throws IOException {

    }
}
