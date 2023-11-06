package com.imooc.pan.server.modules.share.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.pan.web.serializer.Date2StringSerializer;
import com.imooc.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@ApiModel("the list of the share urls VO")
@Data
public class DriveHarborShareUrlListVO {
    private static final long serialVersionUID = -5301645564554502650L;

    @ApiModelProperty("share id")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long shareId;

    @ApiModelProperty("share name")
    private String shareName;

    @ApiModelProperty("share URL")
    private String shareUrl;

    @ApiModelProperty("share code")
    private String shareCode;

    @ApiModelProperty("share status")
    private Integer shareStatus;

    @ApiModelProperty("share type")
    private Integer shareType;

    @ApiModelProperty("share day type")
    private Integer shareDayType;

    @ApiModelProperty("share end time")
    @JsonSerialize(using = Date2StringSerializer.class)
    private Date shareEndTime;

    @ApiModelProperty("share create time")
    @JsonSerialize(using = Date2StringSerializer.class)
    private Date createTime;
}
