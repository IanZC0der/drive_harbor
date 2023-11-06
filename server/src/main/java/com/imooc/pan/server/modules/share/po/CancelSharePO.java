package com.imooc.pan.server.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("cancel share po")
@Data
public class CancelSharePO implements Serializable {

    private static final long serialVersionUID = -1536161974548607677L;

    @ApiModelProperty(value = "the share ids list, multiuples one separeted by ','", required = true)
    @NotBlank(message = "Please select the shares you want to cancel")
    private String shareIds;

}
