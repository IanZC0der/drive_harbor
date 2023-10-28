package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * query folder tree context
 */
@Data
public class QueryFolderTreeContext implements Serializable {

    private static final long serialVersionUID = 2812241666707882447L;

    /**
     * user id
     */
    private Long userId;

}
