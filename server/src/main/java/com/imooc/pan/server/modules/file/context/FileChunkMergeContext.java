package com.imooc.pan.server.modules.file.context;

import com.imooc.pan.server.modules.file.entity.driveHarborFile;
import lombok.Data;

import java.io.Serializable;

/**
 * file chunk merge context
 */
@Data
public class FileChunkMergeContext implements Serializable {

    private static final long serialVersionUID = -8742316521679101082L;

    /**
     * file name
     */
    private String filename;

    /**
     * identifier
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
     * user ID
     */
    private Long userId;

    /**
     * physical file record
     */
    private driveHarborFile record;

}
