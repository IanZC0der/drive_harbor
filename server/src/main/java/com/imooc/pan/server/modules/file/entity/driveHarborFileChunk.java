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
    private String realPath;

    /**
     * chunk number
     */
    @TableField(value = "chunk_number")
    private Integer chunkNumber;

    /**
     * expiration time
     */
    @TableField(value = "expiration_time")
    private Date expirationTime;

    /**
     * creator
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * update time
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}