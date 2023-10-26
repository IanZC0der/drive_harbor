package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * create folde context
 */
@Data
public class CreateFolderContext implements Serializable {

    private static final long serialVersionUID = -861882709652125971L;

    /**
     * parent folder id
     */
    private Long parentId;

    /**
     * user id
     */
    private Long userId;

    /**
     * folder name
     */
    private String folderName;
}
