package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "single file uploading entity params")
@Data
public class FileUploadPO implements Serializable {

    private static final long serialVersionUID = -7944685825604968904L;

    @ApiModelProperty(value = "file name", required = true)
    @NotBlank(message = "file name cannot be empty.")
    private String filename;

    @ApiModelProperty(value = "file identifier", required = true)
    @NotBlank(message = "identifier cannot be blank.")
    private String identifier;

    @ApiModelProperty(value = "file size", required = true)
    @NotNull(message = "File size cannot be null.")
    private Long totalSize;

    @ApiModelProperty(value = "parent folder ID", required = true)
    @NotBlank(message = "ID cannot be null.")
    private String parentId;

    @ApiModelProperty(value = "File entity", required = true)
    @NotNull(message = "File entity cannot be null.")
    private MultipartFile file;

}
