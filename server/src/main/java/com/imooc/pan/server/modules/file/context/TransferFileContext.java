package com.imooc.pan.server.modules.file.context;

import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * transfer file context
 */
@Data
public class TransferFileContext implements Serializable {

    private static final long serialVersionUID = 8275733772636224847L;

    /**
     * file ID list
     */
    private List<Long> fileIdList;

    /**
     * target parent folder
     */
    private Long targetParentId;

    /**
     * user ID
     */
    private Long userId;

    /**
     * file list
     */
    private List<driveHarborUserFile> prepareRecords;

}
