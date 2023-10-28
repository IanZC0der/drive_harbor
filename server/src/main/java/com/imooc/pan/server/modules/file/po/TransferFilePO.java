package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("file transfer PO")
@Data
public class TransferFilePO implements Serializable {

    private static final long serialVersionUID = -2014658290081167760L;

    @ApiModelProperty("collections of files ids, separated by ','")
    @NotBlank(message = "Please select the files")
    private String fileIds;

    @ApiModelProperty("Folder ID to save the files to be transferred")
    @NotBlank(message = "Please select the folder")
    private String targetParentId;

}
