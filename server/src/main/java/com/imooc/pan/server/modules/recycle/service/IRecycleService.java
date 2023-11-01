package com.imooc.pan.server.modules.recycle.service;

import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.recycle.context.DeleteContext;
import com.imooc.pan.server.modules.recycle.context.QueryRecycleFileListContext;
import com.imooc.pan.server.modules.recycle.context.RestoreContext;

import java.util.List;

public interface IRecycleService {
    /**
     * query the files list in the trash can
     *
     * @param context
     * @return
     */
    List<DriveHarborUserFileVO> recycles(QueryRecycleFileListContext context);

    void restore(RestoreContext context);

    void delete(DeleteContext context);
}
