package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * query uploaded chunks context
 */
@Data
public class QueryUploadedChunksContext implements Serializable {

    private static final long serialVersionUID = -2219913977857676171L;

    /**
     * file identifier
     */
    private String identifier;

    /**
     * user ID
     */
    private Long userId;

}
