package com.imooc.pan.server.modules.recycle.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * share save context
 */
@Data
public class ShareSaveContext implements Serializable {

    private static final long serialVersionUID = -5381349624053569146L;

    /**
     * id listt
     */
    private List<Long> fileIdList;

    /**
     * target parent id
     */
    private Long targetParentId;

    /**
     * current user id
     */
    private Long userId;

    /**
     * share id
     */
    private Long shareId;

}
