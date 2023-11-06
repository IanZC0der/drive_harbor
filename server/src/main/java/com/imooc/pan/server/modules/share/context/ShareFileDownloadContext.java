package com.imooc.pan.server.modules.share.context;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * share file download context
 */
@Data
public class ShareFileDownloadContext implements Serializable {

    private static final long serialVersionUID = 6163063566421910008L;

    /**
     * id of the file to be downloaded
     */
    private Long fileId;

    /**
     * current user id
     */
    private Long userId;

    /**
     * share id
     */
    private Long shareId;

    /**
     * response
     */
    private HttpServletResponse response;

}
