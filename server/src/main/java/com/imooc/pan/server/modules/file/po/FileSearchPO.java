package com.imooc.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("file search PO")
@Data
public class FileSearchPO implements Serializable {

    private static final long serialVersionUID = 5477817929836619174L;

    @ApiModelProperty(value = "Search keyword", required = true)
    @NotBlank(message = "Keyword cannot be null.")
    private String keyword;

    @ApiModelProperty(value = "File types, multiples file types should be separated by ','")
    private String fileTypes;

}
