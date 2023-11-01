package com.imooc.pan.server.modules.recycle.service.impl;

import com.imooc.pan.server.modules.file.context.QueryFileListContext;
import com.imooc.pan.server.modules.file.enums.DelFlagEnum;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.recycle.context.QueryRecycleFileListContext;
import com.imooc.pan.server.modules.recycle.service.IRecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecycleServiceImpl implements IRecycleService {
    @Autowired
    private IUserFileService iUserFileService;

    /**
     * query the file list in the trash can
     * @param context
     * @return
     */
    @Override
    public List<DriveHarborUserFileVO> recycles(QueryRecycleFileListContext context) {
        QueryFileListContext queryFileListContext = new QueryFileListContext();
        queryFileListContext.setUserId(context.getUserId());
        queryFileListContext.setDelFlag(DelFlagEnum.YES.getCode());
        return iUserFileService.getFileList(queryFileListContext);
    }
}
