package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * File preview context
 */
@Data
public class FilePreviewContext implements Serializable {

    private static final long serialVersionUID = 4017444860148856342L;

    /**
     * file ID
     */
    private Long fileId;

    /**
     * http servlet response
     */
    private HttpServletResponse response;

    /**
     * user ID
     */
    private Long userId;

}
