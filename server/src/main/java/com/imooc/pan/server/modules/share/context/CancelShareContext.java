package com.imooc.pan.server.modules.share.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * cancel share context
 */
@Data
public class CancelShareContext implements Serializable {

    private static final long serialVersionUID = -7170454089602402200L;

    /**
     * user id
     */
    private Long userId;

    /**
     * list of share ids
     */
    private List<Long> shareIdList;

}
