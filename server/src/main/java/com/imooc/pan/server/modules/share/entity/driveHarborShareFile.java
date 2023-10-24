package com.imooc.pan.server.modules.share.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * files_shared_by_user
 * @TableName r_pan_share_file
 */
@TableName(value ="r_pan_share_file")
@Data
public class driveHarborShareFile implements Serializable {
    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * share id
     */
    @TableField(value = "share_id")
    private Long share_id;

    /**
     * file id
     */
    @TableField(value = "file_id")
    private Long file_id;

    /**
     * sharer
     */
    @TableField(value = "create_user")
    private Long create_user;

    /**
     * share time
     */
    @TableField(value = "create_time")
    private Date create_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}