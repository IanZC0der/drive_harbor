package com.imooc.pan.storage.engine.core;

import com.imooc.pan.storage.engine.core.context.DeleteFileContext;
import com.imooc.pan.storage.engine.core.context.MergeFileContext;
import com.imooc.pan.storage.engine.core.context.StoreFileChunkContext;
import com.imooc.pan.storage.engine.core.context.StoreFileContext;

import java.io.IOException;

/**
 * file storage high level interface
 */
public interface StorageEngine {
    /**
     * save physical file
     * @param context
     * @throws IOException
     */
    void store(StoreFileContext context) throws IOException;

    /**
     * delte physical file
     * @param context
     * @throws IOException
     */
    void delete(DeleteFileContext context) throws IOException;

    /**
     * store chunk
     * @param context
     * @throws IOException
     */
    void storeChunk(StoreFileChunkContext context) throws IOException;

    /**
     * merge chunks
     * @param context
     * @throws IOException
     */
    void mergeFile(MergeFileContext context) throws IOException;


}
