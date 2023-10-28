package com.imooc.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * delete files context
 */
@Data
public class DeleteFileContext implements Serializable {

    private static final long serialVersionUID = -5040051387091567725L;

    /**
     * ids of the files to be deleted
     */
    private List<Long> fileIdList;

    /**
     * user id
     */
    private Long userId;

}
