package com.imooc.pan.storage.engine.core;

import com.imooc.pan.storage.engine.core.context.*;

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

    /**
     * read file content and write it to the output stream
     *
     * @param context
     * @throws IOException
     */
    void realFile(ReadFileContext context) throws IOException;


}
