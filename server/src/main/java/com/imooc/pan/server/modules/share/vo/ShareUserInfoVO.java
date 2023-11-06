package com.imooc.pan.server.modules.share.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("share info vo")
@Data
public class ShareUserInfoVO implements Serializable {

    private static final long serialVersionUID = 5739630033108250153L;

    @ApiModelProperty("sharer ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long userId;

    @ApiModelProperty("sharer name")
    private String username;

}
