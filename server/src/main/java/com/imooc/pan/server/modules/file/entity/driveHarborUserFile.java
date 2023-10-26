package com.imooc.pan.server.modules.file.entity;

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
public class driveHarborUserFile implements Serializable {
    /**
     * file id
     */
    @TableId(value = "file_id")
    private Long fileId;

    /**
     * user id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * parent folder id, root folder has id 0
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * real file id
     */
    @TableField(value = "real_file_id")
    private Long realFileId;

    /**
     * file name
     */
    @TableField(value = "filename")
    private String fileName;

    /**
     * if folder, 1 for yes
     */
    @TableField(value = "folder_flag")
    private Integer folderFlag;

    /**
     * file size desc
     */
    @TableField(value = "file_size_desc")
    private String fileSizeDesc;

    /**
     * file types, 1: normal file, 2: zipped file, 3: excel, 4: word, 5: pdf, 6: txt, 7: pics, 8: audios, 9: videos, 10: PPT, 11: code, 12: csv
     */
    @TableField(value = "file_type")
    private Integer fileType;

    /**
     * if deleted, 1 for true
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

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