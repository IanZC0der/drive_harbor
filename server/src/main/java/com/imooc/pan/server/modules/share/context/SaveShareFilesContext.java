package com.imooc.pan.server.modules.share.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * save share file context
 */
@Data
public class SaveShareFilesContext implements Serializable {

    private static final long serialVersionUID = -5668591757498143170L;

    /**
     * share ID
     */
    private Long shareId;

    /**
     * shared files Id list
     */
    private List<Long> shareFileIdList;

    /**
     * user id
     */
    private Long userId;

}
