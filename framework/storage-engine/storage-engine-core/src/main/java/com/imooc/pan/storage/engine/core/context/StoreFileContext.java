package com.imooc.pan.storage.engine.core.context;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

/**
 * storage file engine, storage file context
 */
@Data
public class StoreFileContext implements Serializable {

    private static final long serialVersionUID = -514678100134294180L;

    /**
     * file name
     */
    private String filename;

    /**
     * total size
     */
    private Long totalSize;

    /**
     * input stream
     */
    private InputStream inputStream;

    /**
     * real physical path
     */
    private String realPath;

}
