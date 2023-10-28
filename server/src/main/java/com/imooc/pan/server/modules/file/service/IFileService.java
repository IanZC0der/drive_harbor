package com.imooc.pan.server.modules.file.service;

import com.imooc.pan.server.modules.file.context.FileChunkMergeAndSaveContext;
import com.imooc.pan.server.modules.file.context.FileSaveContext;
import com.imooc.pan.server.modules.file.entity.driveHarborFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author benchi
* @description 针对表【r_pan_file(physical_files_information_table)】的数据库操作Service
* @createDate 2023-10-23 21:04:43
*/
public interface IFileService extends IService<driveHarborFile> {
//    List<driveHarborFile> getFileList(QueryRealFileListContext context);

    /**
     * upload the file and save the record
     * @param context
     */
    void saveFile(FileSaveContext context);

    void mergeFileChunkAndSaveFile(FileChunkMergeAndSaveContext fileChunkMergeAndSaveContext);
}
