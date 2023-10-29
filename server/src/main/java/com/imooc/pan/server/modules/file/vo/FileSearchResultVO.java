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
 * File search result VO
 */
@Data
@ApiModel(value = "File search result VO")
public class FileSearchResultVO implements Serializable {

    private static final long serialVersionUID = 3317950091518504175L;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty(value = "file Id")
    private Long fileId;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty(value = "parent folder ID")
    private Long parentId;

    @ApiModelProperty(value = "parent folder name")
    private String parentFilename;

    @ApiModelProperty(value = "file name")
    private String filename;

    @ApiModelProperty(value = "file size desc")
    private String fileSizeDesc;

    @ApiModelProperty(value = "Folder flag, 1 for true")
    private Integer folderFlag;

    @ApiModelProperty(value = "file types, 1: normal file, 2: zipped file, 3: excel, 4: word, 5: pdf, 6: txt, 7: pics, 8: audios, 9: videos, 10: PPT, 11: code, 12: csv")
    private Integer fileType;

    @ApiModelProperty(value = "update time")
    @JsonSerialize(using = Date2StringSerializer.class)
    private Date updateTime;

}
