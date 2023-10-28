package com.imooc.pan.server.modules.file.context;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * file chunk uploading context
 */
@Data
public class FileChunkUploadContext implements Serializable {

    private static final long serialVersionUID = 205285193253485462L;

    /**
     * file name
     */
    private String filename;

    /**
     * identifier
     */
    private String identifier;

    /**
     * total chunks
     */
    private Integer totalChunks;

    /**
     * chunk number, starting from 1
     *
     */
    private Integer chunkNumber;

    /**
     * current chunk size
     */
    private Long currentChunkSize;

    /**
     * total size
     */
    private Long totalSize;

    /**
     * file entity
     */
    private MultipartFile file;

    /**
     * user ID
     */
    private Long userId;

}
