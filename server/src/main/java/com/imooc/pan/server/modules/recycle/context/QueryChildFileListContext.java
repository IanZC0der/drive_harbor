package com.imooc.pan.server.modules.recycle.context;

import com.imooc.pan.server.modules.share.entity.driveHarborShare;
import lombok.Data;

import java.io.Serializable;

/**
 * query the childrens files
 */
@Data
public class QueryChildFileListContext implements Serializable {

    private static final long serialVersionUID = 884255624221527918L;

    /**
     * share id
     */
    private Long shareId;

    /**
     * parent id
     */
    private Long parentId;

    /**
     * record
     */
    private driveHarborShare record;

}
