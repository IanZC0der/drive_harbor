package com.imooc.pan.storage.engine.core.context;

import lombok.Data;

import java.io.OutputStream;
import java.io.Serializable;

/**
 * read file context
 */
@Data
public class ReadFileContext implements Serializable {

    private static final long serialVersionUID = 2506771761529717302L;

    /**
     * real path
     */
    private String realPath;

    /**
     * output stream
     */
    private OutputStream outputStream;

}
