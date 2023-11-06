package com.imooc.pan.server.modules.share.context;

import com.imooc.pan.server.modules.share.entity.driveHarborShare;
import lombok.Data;

import java.io.Serializable;

/**
 * check share code context
 */
@Data
public class CheckShareCodeContext implements Serializable {

    private static final long serialVersionUID = -5492075515460473471L;

    /**
     * share ID
     */
    private Long shareId;

    /**
     * share code
     */
    private String shareCode;

    /**
     * share entitty
     */
    private driveHarborShare record;

}
