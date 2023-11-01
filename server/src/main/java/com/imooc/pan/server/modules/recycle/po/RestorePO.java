package com.imooc.pan.server.modules.recycle.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("文件还原参数实体")
@Data
public class RestorePO implements Serializable {

    private static final long serialVersionUID = -8600005249933040664L;

    @ApiModelProperty(value = "list of IDs of the files to be restored, separated by the common splitter", required = true)
    @NotBlank(message = "Please select the files to be restored")
    private String fileIds;

}
