package com.imooc.pan.server.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("save share vo")
@Data
public class ShareSavePO implements Serializable {

    private static final long serialVersionUID = 5218212643469089685L;

    @ApiModelProperty(value = "the list of ids of the files to be saved, multiples ones separated by common separator", required = true)
    @NotBlank(message = "Please select the files to be saved")
    private String fileIds;

    @ApiModelProperty(value = "target parent id", required = true)
    @NotBlank(message = "Please select the folder to save the files")
    private String targetParentId;

}
