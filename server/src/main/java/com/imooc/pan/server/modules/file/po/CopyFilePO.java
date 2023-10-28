package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("Copy File PO")
@Data
public class CopyFilePO implements Serializable {

    private static final long serialVersionUID = 5713445035865802696L;



    @ApiModelProperty("collections of ids of files to be copied, separated by ','")
    @NotBlank(message = "Please select the files to be copied")
    private String fileIds;

    @ApiModelProperty("Folder ID to save the files to be transferred")
    @NotBlank(message = "Please select the target folder")
    private String targetParentId;

}
