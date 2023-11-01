package com.imooc.pan.server.modules.recycle.context;

import lombok.Data;

import java.io.Serializable;

/**
 * query  file list context
 */
@Data
public class QueryRecycleFileListContext implements Serializable {

    private static final long serialVersionUID = -9177250150561429061L;

    private Long userId;

}
