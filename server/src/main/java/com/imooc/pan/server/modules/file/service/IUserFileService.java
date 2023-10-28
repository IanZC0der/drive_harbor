package com.imooc.pan.server.modules.file.service;

import com.imooc.pan.server.modules.file.context.*;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.file.vo.FileChunkUploadVO;
import com.imooc.pan.server.modules.file.vo.FolderTreeNodeVO;
import com.imooc.pan.server.modules.file.vo.UploadedChunksVO;

import java.util.List;

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

    /**
     * query the info of the user root folder
     * @param userId
     * @return
     */
    driveHarborUserFile getUserRootFile(Long userId);

    /**
     * query the file list of a specific type
     * @param context
     * @return
     */
    List<DriveHarborUserFileVO> getFileList(QueryFileListContext context);

    /**
     * update file name
     * @param context
     */
    void updateFilename(UpdateFilenameContext context);

    void deleteFile(DeleteFileContext context);

    boolean secUpload(SecUploadFileContext context);

    /**
     * single file uploading
     * @param context
     */
    void upload(FileUploadContext context);

    FileChunkUploadVO chunkUpload(FileChunkUploadContext context);

    UploadedChunksVO getUploadedChunks(QueryUploadedChunksContext context);

    void mergeFile(FileChunkMergeContext context);

    void download(FileDownloadContext context);

    void preview(FilePreviewContext context);

    List<FolderTreeNodeVO> getFolderTree(QueryFolderTreeContext context);

    void transfer(TransferFileContext context);
}
