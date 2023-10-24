package com.imooc.pan.server.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * user information table
 * @TableName r_pan_user
 */
@TableName(value ="r_pan_user")
@Data
public class driveHarborUser implements Serializable {
    /**
     * user id
     */
    @TableId(value = "user_id")
    private Long user_id;

    /**
     * user name
     */
    @TableField(value = "username")
    private String username;

    /**
     * password
     */
    @TableField(value = "password")
    private String password;

    /**
     * salt
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * security question
     */
    @TableField(value = "question")
    private String question;

    /**
     * security answer
     */
    @TableField(value = "answer")
    private String answer;

    /**
     * account creation time
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * update time
     */
    @TableField(value = "update_time")
    private Date update_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}