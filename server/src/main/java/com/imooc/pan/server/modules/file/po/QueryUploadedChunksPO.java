package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("query uploaded chunks po")
@Data
public class QueryUploadedChunksPO implements Serializable {

    private static final long serialVersionUID = 866722676187500143L;

    @ApiModelProperty(value = "file identifier", required = true)
    @NotBlank(message = "Identifier cannot be null.")
    private String identifier;

}
