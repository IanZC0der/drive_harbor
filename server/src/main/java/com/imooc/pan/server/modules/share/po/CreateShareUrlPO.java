package com.imooc.pan.server.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "create share url po")
@Data
public class CreateShareUrlPO implements Serializable {

    private static final long serialVersionUID = 3561328588508464262L;

    @ApiModelProperty(value = "share name", required = true)
    @NotBlank(message = "Sharename cannot be null.")
    private String shareName;

    @ApiModelProperty(value = "share type", required = true)
    @NotNull(message = "share type cannot be null.")
    private Integer shareType;

    @ApiModelProperty(value = "share days", required = true)
    @NotNull(message = "share days cannot be null.")
    private Integer shareDayType;

    @ApiModelProperty(value = "ids of the shared files, multiple ids separated by common separator", required = true)
    @NotBlank(message = "file id cannot be null.")
    private String shareFileIds;

}
