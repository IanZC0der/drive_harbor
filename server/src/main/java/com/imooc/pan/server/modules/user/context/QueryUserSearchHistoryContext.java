package com.imooc.pan.server.modules.user.context;

import lombok.Data;

import java.io.Serializable;

/**
 * query user search history context
 */
@Data
public class QueryUserSearchHistoryContext implements Serializable {

    /**
     * userid
     */
    private Long userId;

}
