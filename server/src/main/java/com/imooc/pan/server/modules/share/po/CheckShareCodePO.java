package com.imooc.pan.server.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("check share code PO")
@Data
public class CheckShareCodePO implements Serializable {

    private static final long serialVersionUID = -8829888408230236969L;

    @ApiModelProperty(value = "share ID", required = true)
    @NotBlank(message = "Share id cannot be null.")
    private String shareId;

    @ApiModelProperty(value = "share code", required = true)
    @NotBlank(message = "share code cannot be null.")
    private String shareCode;

}
