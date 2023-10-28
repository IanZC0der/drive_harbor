package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("file chunk upload PO params")
@Data
public class FileChunkUploadPO implements Serializable {

    private static final long serialVersionUID = 8036267299049093753L;

    @ApiModelProperty(value = "file name", required = true)
    @NotBlank(message = "File name cannot be null.")
    private String filename;

    @ApiModelProperty(value = "file identifier", required = true)
    @NotBlank(message = "file identifier cannot be null.")
    private String identifier;

    @ApiModelProperty(value = "total number of chunks", required = true)
    @NotNull(message = "Number cannot be null.")
    private Integer totalChunks;

    @ApiModelProperty(value = "chunk number", required = true)
    @NotNull(message = "Chunk number cannot be null")
    private Integer chunkNumber;

    @ApiModelProperty(value = "current chunk size", required = true)
    @NotNull(message = "Size cannot be null")
    private Long currentChunkSize;

    @ApiModelProperty(value = "total file size", required = true)
    @NotNull(message = "Size cannot be null.")
    private Long totalSize;

    @ApiModelProperty(value = "file chunk entity", required = true)
    @NotNull(message = "Entity cannot be null")
    private MultipartFile file;

}
