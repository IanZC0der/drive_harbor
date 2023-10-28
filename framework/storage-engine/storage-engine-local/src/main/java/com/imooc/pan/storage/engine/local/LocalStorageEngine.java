package com.imooc.pan.storage.engine.local;
import com.imooc.pan.core.utils.FileUtil;
import com.imooc.pan.storage.engine.core.context.DeleteFileContext;
import com.imooc.pan.storage.engine.core.context.MergeFileContext;
import com.imooc.pan.storage.engine.core.context.StoreFileChunkContext;
import com.imooc.pan.storage.engine.core.context.StoreFileContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.imooc.pan.storage.engine.core.AbstractStorageEngine;
import com.imooc.pan.storage.engine.local.config.*;

import java.io.IOException;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * local storage engine
 */
@Component
public class LocalStorageEngine extends AbstractStorageEngine {

    @Autowired
    private LocalStorageEngineConfig config;

    @Override
    protected void doStore(StoreFileContext context) throws IOException {
        String basePath = config.getRootFilePath();
        String realFilePath = FileUtil.generateStoreFileRealPath(basePath, context.getFilename());
        FileUtil.writeStream2File(context.getInputStream(), new File(realFilePath), context.getTotalSize());
        context.setRealPath(realFilePath);

    }

    @Override
    protected void doDelete(DeleteFileContext context) throws IOException {

        FileUtil.deleteFiles(context.getRealFilePathList());

    }

    /**
     *
     * @param context
     * @throws IOException
     */
    @Override
    protected void doStoreChunk(StoreFileChunkContext context) throws IOException {
        String basePath = config.getRootFilePath();
        String realFilePath = FileUtil.generateStoreFileChunkRealPath(basePath, context.getIdentifier(), context.getChunkNumber());
        FileUtil.writeStream2File(context.getInputStream(), new File(realFilePath), context.getTotalSize());
        context.setRealPath(realFilePath);

    }

    @Override
    protected void doMergeFile(MergeFileContext context) throws IOException {
        String basePath = config.getRootFilePath();
        String realFilePath = FileUtil.generateStoreFileRealPath(basePath, context.getFilename());
        FileUtil.createFile(new File(realFilePath));
        List<String> chunkPaths = context.getRealPathList();
        for (String chunkPath : chunkPaths) {
            FileUtil.appendWrite(Paths.get(realFilePath), new File(chunkPath).toPath());
        }
        FileUtil.deleteFiles(chunkPaths);
        context.setRealPath(realFilePath);
    }

}
