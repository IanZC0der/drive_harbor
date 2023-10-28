package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * speed file uploading entity context
 */
@Data
public class SecUploadFileContext implements Serializable {

    private static final long serialVersionUID = 865765374680289146L;

    /**
     * parent folder id
     */
    private Long parentId;

    /**
     * file name
     */
    private String filename;

    /**
     * file identifier
     */
    private String identifier;

    /**
     * user id
     */
    private Long userId;

}
