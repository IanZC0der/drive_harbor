package com.imooc.pan.server.modules.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("file chunk uploading responder entity")
@Data
public class FileChunkUploadVO implements Serializable {

    private static final long serialVersionUID = 7670192129580713809L;

    @ApiModelProperty("if merging is needed,0 for false, 1 for true")
    private Integer mergeFlag;

}
