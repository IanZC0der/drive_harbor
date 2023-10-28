package com.imooc.pan.server.modules.file.context;

import com.imooc.pan.server.modules.file.entity.driveHarborFile;
import lombok.Data;

import java.io.Serializable;

/**
 * file chunk merge and save context
 */
@Data
public class FileChunkMergeAndSaveContext implements Serializable {

    private static final long serialVersionUID = -3913020166000401092L;

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
     * parent folder id
     */
    private Long parentId;

    /**
     * user id
     */
    private Long userId;

    /**
     * file physical record
     */
    private driveHarborFile record;

    /**
     * real path after merging
     */
    private String realPath;

}
