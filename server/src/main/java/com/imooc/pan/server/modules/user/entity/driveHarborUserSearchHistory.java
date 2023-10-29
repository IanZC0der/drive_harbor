package com.imooc.pan.server.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * user search history table
 * @TableName r_pan_user_search_history
 */
@TableName(value ="r_pan_user_search_history")
@Data
public class driveHarborUserSearchHistory implements Serializable {
    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * user id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * search content
     */
    @TableField(value = "search_content")
    private String searchContent;

    /**
     * create time
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * update time
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}