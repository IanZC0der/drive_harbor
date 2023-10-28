package com.imooc.pan.server.modules.file.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("Folder tree node VO")
@Data
public class FolderTreeNodeVO implements Serializable {

    private static final long serialVersionUID = -2736575645065574820L;

    @ApiModelProperty("folder name")
    private String label;

    @ApiModelProperty("file ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long id;

    @ApiModelProperty("parent folder ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long parentId;

    @ApiModelProperty("child nodes list")
    private List<FolderTreeNodeVO> children;

    public void print() {
        String jsonString = JSON.toJSONString(this);
        System.out.println(jsonString);
    }

}
