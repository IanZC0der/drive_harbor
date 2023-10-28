package com.imooc.pan.storage.engine.core.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 删除物理文件的上下文试实体信息
 */
@Data
public class DeleteFileContext implements Serializable {

    private static final long serialVersionUID = 8752492154745347237L;

    /**
     * the real paths of the file to be deleted
     */
    private List<String> realFilePathList;

}
