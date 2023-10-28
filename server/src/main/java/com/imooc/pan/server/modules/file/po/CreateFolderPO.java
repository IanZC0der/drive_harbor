package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel(value = "create folder params")
@Data
public class CreateFolderPO implements Serializable {

    private static final long serialVersionUID = 5475817231508440546L;

    @ApiModelProperty(value = "encrypted parent folder id", required = true)
    @NotBlank(message = "parent folder id cannot be blank")
    private String parentId;

    @ApiModelProperty(value = "folder name", required = true)
    @NotBlank(message = "folder name cannot be blank")
    private String folderName;

}
