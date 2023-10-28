package com.imooc.pan.server.modules.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("user info")
@Data
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 831556981609248699L;

    @ApiModelProperty("username")
    private String username;

    @ApiModelProperty("encrypted id of the user root catalog")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long rootFileId;

    @ApiModelProperty("root file name")
    private String rootFilename;
}
