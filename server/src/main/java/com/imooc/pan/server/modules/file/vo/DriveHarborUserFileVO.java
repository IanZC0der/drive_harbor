package com.imooc.pan.server.modules.file.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.pan.web.serializer.Date2StringSerializer;
import com.imooc.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * user queries the file list of a specific file type
 */
@Data
@ApiModel(value = "file list of a specific file type")
public class DriveHarborUserFileVO implements Serializable{
    private static final long serialVersionUID = 6113069180217057240L;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty(value = "fileId")
    private Long fileId;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty(value = "parentId")
    private Long parentId;

    @ApiModelProperty(value = "filename")
    private String filename;

    @ApiModelProperty(value = "fileSizeDesc")
    private String fileSizeDesc;

    @ApiModelProperty(value = "folder flag, 1 for true, 0 for false")
    private Integer folderFlag;

    @ApiModelProperty(value = "file types, 1: normal file, 2: zipped file, 3: excel, 4: word, 5: pdf, 6: txt, 7: pics, 8: audios, 9: videos, 10: PPT, 11: code, 12: csv")
    private Integer fileType;

    @ApiModelProperty(value = "file update time")
    @JsonSerialize(using = Date2StringSerializer.class)
    private Date updateTime;
}
