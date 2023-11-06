package com.imooc.pan.server.modules.share.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("query simplified share detail vo")
@Data
public class ShareSimpleDetailVO implements Serializable {

    private static final long serialVersionUID = -244174348108049506L;

    @ApiModelProperty("share id")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long shareId;

    @ApiModelProperty("share name")
    private String shareName;

    @ApiModelProperty("sharer")
    private ShareUserInfoVO shareUserInfoVO;

}
