package com.imooc.pan.server.modules.file.context;

import com.imooc.pan.server.modules.file.entity.driveHarborFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * file save context
 */
@Data
public class FileSaveContext implements Serializable {

    /**
     * file name
     */
    private String filename;

    /**
     * file identifier
     */
    private String identifier;

    /**
     * size
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
     * 实体文件记录
     */
    private driveHarborFile record;

    /**
     * real path
     */
    private String realPath;

}
