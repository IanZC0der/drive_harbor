package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * search context
 */
@Data
public class FileSearchContext implements Serializable {

    private static final long serialVersionUID = -6935525392301408482L;

    /**
     * search keyword
     */
    private String keyword;

    /**
     * file types array
     */
    private List<Integer> fileTypeArray;

    /**
     * user ID
     */
    private Long userId;

}
