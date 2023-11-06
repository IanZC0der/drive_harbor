package com.imooc.pan.server.modules.share.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.modules.share.context.SaveShareFilesContext;
import com.imooc.pan.server.modules.share.entity.driveHarborShareFile;
import com.imooc.pan.server.modules.share.service.IShareFileService;
import com.imooc.pan.server.modules.share.mapper.driveHarborShareFileMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author benchi
* @description 针对表【r_pan_share_file(files_shared_by_user)】的数据库操作Service实现
* @createDate 2023-10-23 21:08:08
*/
@Service
public class ShareFileServiceImpl extends ServiceImpl<driveHarborShareFileMapper, driveHarborShareFile>
    implements IShareFileService {

    @Override
    public void saveShareFiles(SaveShareFilesContext context) {
        Long shareId = context.getShareId();
        List<Long> shareFileIdList = context.getShareFileIdList();
        Long userId = context.getUserId();

        List<driveHarborShareFile> records = Lists.newArrayList();

        for (Long shareFileId : shareFileIdList) {
            driveHarborShareFile record = new driveHarborShareFile();
            record.setId(IdUtil.get());
            record.setShareId(shareId);
            record.setFileId(shareFileId);
            record.setCreateUser(userId);
            record.setCreateTime(new Date());
            records.add(record);
        }

        if (!saveBatch(records)) {
            throw new driveHarborBusinessException("save share and user info failure");
        }
    }
}




