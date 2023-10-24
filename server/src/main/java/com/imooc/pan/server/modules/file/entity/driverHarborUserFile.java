package com.imooc.pan.server.modules.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * user file information table
 * @TableName r_pan_user_file
 */
@TableName(value ="r_pan_user_file")
@Data
public class driverHarborUserFile implements Serializable {
    /**
     * file id
     */
    @TableId(value = "file_id")
    private Long file_id;

    /**
     * user id
     */
    @TableField(value = "user_id")
    private Long user_id;

    /**
     * parent folder id, root folder has id 0
     */
    @TableField(value = "parent_id")
    private Long parent_id;

    /**
     * real file id
     */
    @TableField(value = "real_file_id")
    private Long real_file_id;

    /**
     * file name
     */
    @TableField(value = "filename")
    private String filename;

    /**
     * if folder, 1 for yes
     */
    @TableField(value = "folder_flag")
    private Integer folder_flag;

    /**
     * file size desc
     */
    @TableField(value = "file_size_desc")
    private String file_size_desc;

    /**
     * file types, 1: normal file, 2: zipped file, 3: excel, 4: word, 5: pdf, 6: txt, 7: pics, 8: audios, 9: videos, 10: PPT, 11: code, 12: csv
     */
    @TableField(value = "file_type")
    private Integer file_type;

    /**
     * if deleted, 1 for true
     */
    @TableField(value = "del_flag")
    private Integer del_flag;

    /**
     * creator
     */
    @TableField(value = "create_user")
    private Long create_user;

    /**
     * create time
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * updater
     */
    @TableField(value = "update_user")
    private Long update_user;

    /**
     * update time
     */
    @TableField(value = "update_time")
    private Date update_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}