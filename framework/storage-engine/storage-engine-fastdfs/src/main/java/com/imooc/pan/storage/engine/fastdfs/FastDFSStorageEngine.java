package com.imooc.pan.storage.engine.fastdfs;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.exception.driveHarborFrameworkException;
import com.imooc.pan.core.utils.FileUtil;
import com.imooc.pan.storage.engine.core.AbstractStorageEngine;
import com.imooc.pan.storage.engine.core.context.*;
import com.imooc.pan.storage.engine.fastdfs.config.FastDFSStorageEngineConfig;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * storage engine based on fastdfs
 */
@Component
public class FastDFSStorageEngine extends AbstractStorageEngine {
    @Autowired
    private FastFileStorageClient client;

    @Autowired
    private FastDFSStorageEngineConfig config;
    @Override
    protected void doStore(StoreFileContext context) throws IOException {
        StorePath storePath = client.uploadFile(config.getGroup(), context.getInputStream(), context.getTotalSize(), FileUtil.getFileExtName(context.getFilename()));
        context.setRealPath(storePath.getFullPath());

    }

    @Override
    protected void doDelete(DeleteFileContext context) throws IOException {
        List<String> realFilePathList = context.getRealFilePathList();
        if (CollectionUtils.isNotEmpty(realFilePathList)) {
            realFilePathList.stream().forEach(client::deleteFile);
        }

    }

    @Override
    protected void doStoreChunk(StoreFileChunkContext context) throws IOException {
        throw new driveHarborFrameworkException("FastDFS doesn't support chunks uploading");

    }

    @Override
    protected void doMergeFile(MergeFileContext context) throws IOException {
        throw new driveHarborFrameworkException("FastDFS doesn't support chunks uploading");

    }

    @Override
    protected void doReadFile(ReadFileContext context) throws IOException {
        String realPath = context.getRealPath();
        String group = realPath.substring(driveHarborConstants.ZERO_INT, realPath.indexOf(driveHarborConstants.SLASH_STR));
        String path = realPath.substring(realPath.indexOf(driveHarborConstants.SLASH_STR) + driveHarborConstants.ONE_INT);

        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = client.downloadFile(group, path, downloadByteArray);

        OutputStream outputStream = context.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();

    }

}
