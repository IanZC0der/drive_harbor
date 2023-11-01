package com.imooc.pan.server.modules.recycle.context;

import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * restore context
 */
@Data
public class RestoreContext implements Serializable {

    private static final long serialVersionUID = 9084417258307062516L;

    /**
     * list of ids of files to be recovered
     */
    private List<Long> fileIdList;

    /**
     * user id
     */
    private Long userId;

    /**
     * records of the deleted files to be recovered
     */
    private List<driveHarborUserFile> records;

}
