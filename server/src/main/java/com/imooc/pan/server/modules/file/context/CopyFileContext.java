package com.imooc.pan.server.modules.file.context;

import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文件复制操作上下文实体对象
 */
@Data
public class CopyFileContext implements Serializable {

    private static final long serialVersionUID = -4498476381209653058L;

    /**
     * file id list
     */
    private List<Long> fileIdList;

    /**
     * target folder
     */
    private Long targetParentId;

    /**
     * user id
     */
    private Long userId;

    /**
     * files to be copied
     */
    private List<driveHarborUserFile> prepareRecords;

}
