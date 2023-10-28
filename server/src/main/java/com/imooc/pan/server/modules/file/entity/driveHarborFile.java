package com.imooc.pan.server.modules.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * physical_files_information_table
 * @TableName r_pan_file
 */
@TableName(value ="r_pan_file")
@Data
public class driveHarborFile implements Serializable {
    /**
     * file_id
     */
    @TableId(value = "file_id")
    private Long fileId;

    /**
     * file_name
     */
    @TableField(value = "filename")
    private String filename;

    /**
     * file_path
     */
    @TableField(value = "real_path")
    private String realPath;

    /**
     * file_size
     */
    @TableField(value = "file_size")
    private String fileSize;

    /**
     * file_size_desc
     */
    @TableField(value = "file_size_desc")
    private String fileSizeDesc;

    /**
     * file_suffix
     */
    @TableField(value = "file_suffix")
    private String fileSuffix;

    /**
     * file_preview_content_type
     */
    @TableField(value = "file_preview_content_type")
    private String filePreviewContentType;

    /**
     * unique_identifier
     */
    @TableField(value = "identifier")
    private String identifier;

    /**
     * creator
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * creation_time
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}