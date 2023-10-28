package com.imooc.pan.server.modules.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("query uploaded chunks vo")
@Data
public class UploadedChunksVO implements Serializable {

    private static final long serialVersionUID = 8694674586602329820L;

    @ApiModelProperty("list of the indexes of the uploaded chunks")
    private List<Integer> uploadedChunks;

}
