package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * query file list context
 */
@Data
public class QueryFileListContext implements Serializable {

    private static final long serialVersionUID = 1361135823223937852L;

    /**
     * parent folder id
     */
    private Long parentId;

    /**
     * file array
     */
    private List<Integer> fileTypeArray;

    /**
     * user id
     */
    private Long userId;

    /**
     * file del flag
     */
    private Integer delFlag;

    /**
     * list of file ids
     */
    private List<Long> fileIdList;

}
