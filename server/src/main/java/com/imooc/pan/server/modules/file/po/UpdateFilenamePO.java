package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * rename file PO params
 */
@Data
@ApiModel(value = "rename file PO params")
public class UpdateFilenamePO implements Serializable {

    private static final long serialVersionUID = -8138754986668154124L;

    @ApiModelProperty(value = "new file id", required = true)
    @NotBlank(message = "new file id cannot be blank")
    private String fileId;

    @ApiModelProperty(value = "new file name", required = true)
    @NotBlank(message = "new file name cannot be blank")
    private String newFilename;

}
