package com.imooc.pan.server.modules.file.context;

import com.imooc.pan.server.modules.file.enums.MergeFlagEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * file chunk save context
 */
@Data
public class FileChunkSaveContext implements Serializable {

    private static final long serialVersionUID = 2958393596320065564L;

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
     * current chunk number
     */
    private Integer chunkNumber;

    /**
     * chunk size
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
     * user id
     */
    private Long userId;


    /**
     * merge flag enum
     */
    private MergeFlagEnum mergeFlagEnum = MergeFlagEnum.NOT_READY;

    /**
     * path of the uploaded file
     */
    private String realPath;


}
