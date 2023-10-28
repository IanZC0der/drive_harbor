package com.imooc.pan.server.modules.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * error log table
 * @TableName r_pan_error_log
 */
@TableName(value ="r_pan_error_log")
@Data
public class driveHarborErrorLog implements Serializable {
    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * log contents
     */
    @TableField(value = "log_content")
    private String logContent;

    /**
     * log status, 0: unaddressed, 1: addressed
     */
    @TableField(value = "log_status")
    private Integer logStatus;

    /**
     * creator
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * create time
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * updater
     */
    @TableField(value = "update_user")
    private Long updateUser;

    /**
     * update time
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}