package com.imooc.pan.storage.engine.core.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * merge file context
 */
@Data
public class MergeFileContext implements Serializable {

    private static final long serialVersionUID = -4164388478940590401L;

    /**
     * file name
     */
    private String filename;

    /**
     * file identifier
     */
    private String identifier;

    /**
     * user ID
     */
    private Long userId;

    /**
     * real path list
     */
    private List<String> realPathList;

    /**
     * real path after merging
     */
    private String realPath;

}
