package com.imooc.pan.server.modules.share.context;

import lombok.Data;

import java.io.Serializable;

/**
 * the context of share list context
 */
@Data
public class QueryShareListContext implements Serializable {

    private static final long serialVersionUID = 4348516200563466548L;

    /**
     * user ID
     */
    private Long userId;

}
