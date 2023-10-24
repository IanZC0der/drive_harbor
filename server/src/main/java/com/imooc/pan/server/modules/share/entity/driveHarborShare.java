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
    private Long share_id;

    /**
     * share_name
     */
    @TableField(value = "share_name")
    private String share_name;

    /**
     * share_type_0_meaning_password
     */
    @TableField(value = "share_type")
    private Integer share_type;

    /**
     * type, 0: permanent sharing, 1: valid for 7 days, 2: valid for 30days
     */
    @TableField(value = "share_day_type")
    private Integer share_day_type;

    /**
     * sharing duration, 0 means permanent
     */
    @TableField(value = "share_day")
    private Integer share_day;

    /**
     * share end time
     */
    @TableField(value = "share_end_time")
    private Date share_end_time;

    /**
     * sharing url
     */
    @TableField(value = "share_url")
    private String share_url;

    /**
     * sharing password
     */
    @TableField(value = "share_code")
    private String share_code;

    /**
     * sharing status, 0: normal, 1: file deleted already
     */
    @TableField(value = "share_status")
    private Integer share_status;

    /**
     * sharer
     */
    @TableField(value = "create_user")
    private Long create_user;

    /**
     * sharing time
     */
    @TableField(value = "create_time")
    private Date create_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}