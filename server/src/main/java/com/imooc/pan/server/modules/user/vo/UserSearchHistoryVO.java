package com.imooc.pan.server.modules.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("User search history VO")
@Data
public class UserSearchHistoryVO implements Serializable {

    private static final long serialVersionUID = -1336804234981929854L;

    @ApiModelProperty("search value")
    private String value;

}
