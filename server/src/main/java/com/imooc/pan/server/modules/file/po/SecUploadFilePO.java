package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * speed file uploading PO entity
 */
@ApiModel("speed file uploading PO params")
@Data
public class SecUploadFilePO implements Serializable {

    private static final long serialVersionUID = 5638531322051664526L;

    @ApiModelProperty(value = "IDParent folder id of the file to be uploaded", required = true)
    @NotBlank(message = "Parent folder id cannot be blank")
    private String parentId;

    @ApiModelProperty(value = "file name", required = true)
    @NotBlank(message = "file name cannot be blank")
    private String filename;

    @ApiModelProperty(value = "unique file identifier", required = true)
    @NotBlank(message = "Identifier cannot be blank")
    private String identifier;

}
