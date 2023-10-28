package com.imooc.pan.server.modules.file.context;

import com.imooc.pan.server.modules.file.entity.driveHarborFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * single file uploading context
 */
@Data
public class FileUploadContext implements Serializable {

    private static final long serialVersionUID = 3046407826281125463L;

    /**
     * file name
     */
    private String filename;

    /**
     * file identifier
     */
    private String identifier;

    /**
     * total size
     */
    private Long totalSize;

    /**
     * parent folder id
     */
    private Long parentId;

    /**
     * file entity
     */
    private MultipartFile file;

    /**
     * user id
     */
    private Long userId;

    /**
     * record
     */
    private driveHarborFile record;

}
