package com.imooc.pan.storage.engine.core;

import cn.hutool.core.lang.Assert;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.storage.engine.core.context.DeleteFileContext;
import com.imooc.pan.storage.engine.core.context.MergeFileContext;
import com.imooc.pan.storage.engine.core.context.StoreFileChunkContext;
import com.imooc.pan.storage.engine.core.context.StoreFileContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import com.imooc.pan.cache.core.constants.CacheConstants;

import java.io.IOException;
import java.util.Objects;

/**
 * storage engine high level public parent class
 */
public abstract class AbstractStorageEngine implements StorageEngine{
    @Autowired
    private CacheManager cacheManager;

    /**
     * get cache method
     * @return
     */
    protected Cache getCache() {
        if (Objects.isNull(cacheManager)) {
            throw new driveHarborBusinessException("Cache manager is empty!");
        }
        return cacheManager.getCache(CacheConstants.DRIVE_HARBOR_CACHE_NAME);
    }

    @Override
    public void store(StoreFileContext context) throws IOException {
        checkStoreFileContext(context);
        doStore(context);
    }

    /**
     * do store
     * implemented by the child class
     *
     * @param context
     */
    protected abstract void doStore(StoreFileContext context) throws IOException;

    /**
     * check the context
     *
     * @param context
     */
    private void checkStoreFileContext(StoreFileContext context) {
        Assert.notBlank(context.getFilename(), "file name cannot be null");
        Assert.notNull(context.getTotalSize(), "file size cannot be null");
        Assert.notNull(context.getInputStream(), "file cannot be null");
    }

    @Override
    public void delete(DeleteFileContext context) throws IOException {
        checkDeleteFileContext(context);
        doDelete(context);
    }

    /**
     * do delete
     * implemented by the child class
     *
     * @param context
     * @throws IOException
     */
    protected abstract void doDelete(DeleteFileContext context) throws IOException;

    /**
     * check delete file context
     *
     * @param context
     */
    private void checkDeleteFileContext(DeleteFileContext context) {
        Assert.notEmpty(context.getRealFilePathList(), "path list cannot be null");
    }

    /**
     * 1. check context
     * 2. store chunk
     * @param context
     * @throws IOException
     */
    public void storeChunk(StoreFileChunkContext context) throws IOException {
        checkStoreFileChunkContext(context);
        doStoreChunk(context);
    }

    /**
     * store implemented by child class
     * @param context
     * @throws IOException
     */
    protected abstract void doStoreChunk(StoreFileChunkContext context) throws IOException;

    /**
     * 1. check context
     * 2. merge
     * @param context
     * @throws IOException
     */
    @Override
    public void mergeFile(MergeFileContext context) throws IOException {
        checkMergeFileContext(context);
        doMergeFile(context);
    }

    /**
     * merge file
     * implemented by children class
     * @param context
     */
    protected abstract void doMergeFile(MergeFileContext context) throws IOException;

    /**
     * check merge file context
     * @param context
     */
    private void checkMergeFileContext(MergeFileContext context) {
        Assert.notBlank(context.getFilename(), "文件名称不能为空");
        Assert.notBlank(context.getIdentifier(), "文件唯一标识不能为空");
        Assert.notNull(context.getUserId(), "当前登录用户的ID不能为空");
        Assert.notEmpty(context.getRealPathList(), "文件分片列表不能为空");
    }
    /**
     * verify the file chunk context
     * @param context
     */
    private void checkStoreFileChunkContext(StoreFileChunkContext context) {
        Assert.notBlank(context.getFilename(), "File name cannot be null.");
        Assert.notBlank(context.getIdentifier(), "Identifier cannot be null.");
        Assert.notNull(context.getTotalSize(), "File size cannot be null.");
        Assert.notNull(context.getInputStream(), "File chunk cannot be null.");
        Assert.notNull(context.getTotalChunks(), "Total chunks cannot be null.");
        Assert.notNull(context.getChunkNumber(), "Chunks number cannot be null.");
        Assert.notNull(context.getCurrentChunkSize(), "Chunk size cannot be null.");
        Assert.notNull(context.getUserId(), "user ID cannot be null.");

    }
}
