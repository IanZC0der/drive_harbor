package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel(value = "delete files params")
@Data
public class DeleteFilePO implements Serializable {

    private static final long serialVersionUID = 3098611201745909528L;

    @ApiModelProperty(value = "ids of the files to be deleted, splited by public splitter", required = true)
    @NotBlank(message = "Please select the file info to be deleted")
    private String fileIds;

}
