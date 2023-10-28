package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("file chunks merge PO")
@Data
public class FileChunkMergePO implements Serializable {

    private static final long serialVersionUID = 1415904752976870786L;

    @ApiModelProperty(value = "file name", required = true)
    @NotBlank(message = "File name cannot be null.")
    private String filename;

    @ApiModelProperty(value = "file identifier", required = true)
    @NotBlank(message = "Identifier cannot be null.")
    private String identifier;

    @ApiModelProperty(value = "total size", required = true)
    @NotNull(message = "Total size cannot be null.")
    private Long totalSize;

    @ApiModelProperty(value = "Parent folder id", required = true)
    @NotBlank(message = "Parent folde id cannot be null.")
    private String parentId;

}
