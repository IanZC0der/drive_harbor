package com.imooc.pan.server.modules.share.context;

import com.imooc.pan.server.modules.share.entity.driveHarborShare;
import com.imooc.pan.server.modules.share.vo.ShareDetailVO;
import lombok.Data;

import java.io.Serializable;

/**
 * query share detail context
 */
@Data
public class QueryShareDetailContext implements Serializable {

    /**
     * share id
     */
    private Long shareId;

    /**
     * share record
     */
    private driveHarborShare record;

    /**
     * share vo
     */
    private ShareDetailVO vo;

}
