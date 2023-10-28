package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * file download context
 */
@Data
public class FileDownloadContext implements Serializable {

    private static final long serialVersionUID = -8571038899400767013L;

    /**
     * file id
     */
    private Long fileId;

    /**
     * http sercice response
     */
    private HttpServletResponse response;

    /**
     * user ID
     */
    private Long userId;

}
