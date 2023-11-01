package com.imooc.pan.server.modules.recycle.context;

import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Delete Context
 */
@Data
public class DeleteContext implements Serializable {

    private static final long serialVersionUID = -588491390915976064L;

    /**
     *  list of the file ids to be deleted
     */
    private List<Long> fileIdList;

    /**
     * user id
     */
    private Long userId;

    /**
     * file records to be deleted
     */
    private List<driveHarborUserFile> records;

    /**
     * all records of the files and folders
     */
    private List<driveHarborUserFile> allRecords;

}
