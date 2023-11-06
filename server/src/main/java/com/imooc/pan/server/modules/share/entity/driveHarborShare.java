package com.imooc.pan.server.modules.share.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * User_Sharing_History_Table
 * @TableName r_pan_share
 */
@TableName(value ="r_pan_share")
@Data
public class driveHarborShare implements Serializable {
    /**
     * share_id
     */
    @TableId(value = "share_id")
    private Long shareId;

    /**
     * share_name
     */
    @TableField(value = "share_name")
    private String shareName;

    /**
     * share_type_0_meaning_password
     */
    @TableField(value = "share_type")
    private Integer shareType;

    /**
     * type, 0: permanent sharing, 1: valid for 7 days, 2: valid for 30days
     */
    @TableField(value = "share_day_type")
    private Integer shareDayType;

    /**
     * sharing duration, 0 means permanent
     */
    @TableField(value = "share_day")
    private Integer shareDay;

    /**
     * share end time
     */
    @TableField(value = "share_end_time")
    private Date shareEndTime;

    /**
     * sharing url
     */
    @TableField(value = "share_url")
    private String shareUrl;

    /**
     * sharing password
     */
    @TableField(value = "share_code")
    private String shareCode;

    /**
     * sharing status, 0: normal, 1: file deleted already
     */
    @TableField(value = "share_status")
    private Integer shareStatus;

    /**
     * sharer
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * sharing time
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}