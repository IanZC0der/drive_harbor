package com.imooc.pan.server.modules.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * file chunks information table
 * @TableName r_pan_file_chunk
 */
@TableName(value ="r_pan_file_chunk")
@Data
public class driveHarborFileChunk implements Serializable {
    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * file identifier
     */
    @TableField(value = "identifier")
    private String identifier;

    /**
     * real path for the file chunk
     */
    @TableField(value = "real_path")
    private String real_path;

    /**
     * chunk number
     */
    @TableField(value = "chunk_number")
    private Integer chunk_number;

    /**
     * expiration time
     */
    @TableField(value = "expiration_time")
    private Date expiration_time;

    /**
     * creator
     */
    @TableField(value = "create_user")
    private Long create_user;

    /**
     * update time
     */
    @TableField(value = "create_time")
    private Date create_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}