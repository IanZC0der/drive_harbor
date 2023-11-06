package com.imooc.pan.server.modules.share.context;

import com.imooc.pan.server.modules.share.entity.driveHarborShare;
import com.imooc.pan.server.modules.share.vo.ShareSimpleDetailVO;
import lombok.Data;

import java.io.Serializable;

/**
 * query simplified share detail context
 */
@Data
public class QueryShareSimpleDetailContext implements Serializable {

    /**
     * share id
     */
    private Long shareId;

    /**
     * share record
     */
    private driveHarborShare record;

    /**
     * vo
     */
    private ShareSimpleDetailVO vo;

}
