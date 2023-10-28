package com.imooc.pan.server.modules.file.context;

import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import lombok.Data;

import java.io.Serializable;

/**
 * update file name context
 */
@Data
public class UpdateFilenameContext implements Serializable {

    private static final long serialVersionUID = 6171000069397717175L;

    /**
     * the id of the file to be renamed
     */
    private Long fileId;

    /**
     * new file name
     */
    private String newFilename;

    /**
     * user id
     */
    private Long userId;

    /**
     * file entity to be updated
     */
    private driveHarborUserFile entity;

}
