package com.imooc.pan.server.modules.share.service;

import com.imooc.pan.server.modules.recycle.context.QueryChildFileListContext;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.recycle.context.ShareSaveContext;
import com.imooc.pan.server.modules.share.context.*;
import com.imooc.pan.server.modules.share.entity.driveHarborShare;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pan.server.modules.share.vo.DriveHarborShareUrlListVO;
import com.imooc.pan.server.modules.share.vo.DriveHarborShareUrlVO;
import com.imooc.pan.server.modules.share.vo.ShareDetailVO;
import com.imooc.pan.server.modules.share.vo.ShareSimpleDetailVO;

import java.util.List;

/**
* @author benchi
* @description 针对表【r_pan_share(User_Sharing_History_Table)】的数据库操作Service
* @createDate 2023-10-23 21:08:08
*/
public interface IShareService extends IService<driveHarborShare> {

    DriveHarborShareUrlVO create(CreateShareUrlContext context);

    List<DriveHarborShareUrlListVO> getShares(QueryShareListContext context);

    void cancelShare(CancelShareContext context);

    String checkShareCode(CheckShareCodeContext context);

    ShareDetailVO detail(QueryShareDetailContext context);

    ShareSimpleDetailVO simpleDetail(QueryShareSimpleDetailContext context);

    List<DriveHarborUserFileVO> fileList(QueryChildFileListContext context);

    void saveFiles(ShareSaveContext context);

    void download(ShareFileDownloadContext context);

    void refreshShareStatus(List<Long> allAvailableFileIdList);

    /**
     * rolling query share id
     * @param startId
     * @param limit
     * @return
     */
    List<Long> rollingQueryShareId(long startId, long limit);
}
