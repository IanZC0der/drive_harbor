package com.imooc.pan.server.modules.share.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.web.serializer.Date2StringSerializer;
import com.imooc.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel("share detail vo")
@Data
public class ShareDetailVO implements Serializable {

    private static final long serialVersionUID = -2446579294335071804L;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty("share id")
    private Long shareId;

    @ApiModelProperty("share name")
    private String shareName;

    @ApiModelProperty("share creation time")
    @JsonSerialize(using = Date2StringSerializer.class)
    private Date createTime;

    @ApiModelProperty("share valid days")
    private Integer shareDay;

    @ApiModelProperty("share end time")
    @JsonSerialize(using = Date2StringSerializer.class)
    private Date shareEndTime;

    @ApiModelProperty("list of files in the share")
    private List<DriveHarborUserFileVO> driveHarborUserFileVOList;

    @ApiModelProperty("sharer info")
    private ShareUserInfoVO shareUserInfoVO;

}
