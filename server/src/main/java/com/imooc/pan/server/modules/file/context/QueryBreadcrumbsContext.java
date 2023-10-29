package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * query bread crumbs context
 */
@Data
public class QueryBreadcrumbsContext implements Serializable {

    private static final long serialVersionUID = -5125165545964102997L;

    /**
     * file id
     */
    private Long fileId;

    /**
     * user id
     */
    private Long userId;

}
