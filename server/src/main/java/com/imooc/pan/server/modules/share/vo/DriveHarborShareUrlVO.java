package com.imooc.pan.server.modules.share.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "Driveharbor share url VO")
@Data
public class DriveHarborShareUrlVO implements Serializable {

    private static final long serialVersionUID = 3468789641541361147L;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty("share linkID")
    private Long shareId;

    @ApiModelProperty("share link name")
    private String shareName;

    @ApiModelProperty("share link URL")
    private String shareUrl;

    @ApiModelProperty("share password")
    private String shareCode;

    @ApiModelProperty("share status")
    private Integer shareStatus;

}
