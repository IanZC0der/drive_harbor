package com.imooc.pan.server.modules.recycle.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("Delete PO")
@Data
public class DeletePO implements Serializable {

    private static final long serialVersionUID = 3810211260497917439L;

    @ApiModelProperty(value = "list of file ids to be deleted, separated by common separator", required = true)
    @NotBlank(message = "Please select the files to be deleted")
    private String fileIds;

}
