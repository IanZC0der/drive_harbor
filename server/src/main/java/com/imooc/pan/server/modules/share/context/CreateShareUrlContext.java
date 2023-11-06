package com.imooc.pan.server.modules.share.context;

import com.imooc.pan.server.modules.share.entity.driveHarborShare;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * create share url context
 */
@Data
public class CreateShareUrlContext implements Serializable {

    private static final long serialVersionUID = 2945253863400727173L;

    /**
     * share name
     */
    private String shareName;

    /**
     * share type
     */
    private Integer shareType;

    /**
     * valid days
     */
    private Integer shareDayType;

    /**
     * share file ids list
     */
    private List<Long> shareFileIdList;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 已经保存的分享实体信息
     */
    private driveHarborShare record;

}
