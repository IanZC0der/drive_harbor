package com.imooc.pan.server.modules.file.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.imooc.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@ApiModel("Bread Crumb VO")
@Data
public class BreadcrumbVO implements Serializable {

    private static final long serialVersionUID = -6113151935665730951L;

    @ApiModelProperty("file Id")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long id;

    @ApiModelProperty("parent folder ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long parentId;

    @ApiModelProperty("folder name")
    private String name;

    /**
     * use file to breadCrumb VO
     *
     * @param record
     * @return
     */
    public static BreadcrumbVO transfer(driveHarborUserFile record) {
        BreadcrumbVO vo = new BreadcrumbVO();

        if (Objects.nonNull(record)) {
            vo.setId(record.getFileId());
            vo.setParentId(record.getParentId());
            vo.setName(record.getFileName());
        }

        return vo;
    }

}
