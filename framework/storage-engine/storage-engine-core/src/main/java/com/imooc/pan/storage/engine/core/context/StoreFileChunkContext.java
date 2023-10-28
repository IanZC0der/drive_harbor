package com.imooc.pan.storage.engine.core.context;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

/**
 * store file chunk context
 */
@Data
public class StoreFileChunkContext implements Serializable {

    /**
     * file name
     */
    private String filename;

    /**
     * identifier
     */
    private String identifier;

    /**
     * file total size
     */
    private Long totalSize;

    /**
     * input stream
     */
    private InputStream inputStream;

    /**
     * file real path
     */
    private String realPath;

    /**
     * total chunks
     */
    private Integer totalChunks;

    /**
     * current chunk number
     */
    private Integer chunkNumber;

    /**
     * current chunk size
     */
    private Long currentChunkSize;

    /**
     * user id
     */
    private Long userId;

}
