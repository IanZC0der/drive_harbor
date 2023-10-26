package com.imooc.pan.server.modules.file.service;

import com.imooc.pan.server.modules.file.context.CreateFolderContext;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author benchi
* @description 针对表【r_pan_user_file(user file information table)】的数据库操作Service
* @createDate 2023-10-23 21:04:43
*/
public interface IUserFileService extends IService<driveHarborUserFile> {
    /**
     * create folder
     * @param createFolderContext
     * @return
     */
    Long createFolder(CreateFolderContext createFolderContext);

}
