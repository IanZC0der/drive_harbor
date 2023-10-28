package com.imooc.pan.server.modules.file.controller;

import com.google.common.base.Splitter;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.response.R;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.common.utils.UserIdUtil;
import com.imooc.pan.server.modules.file.constants.FileConstants;
import com.imooc.pan.server.modules.file.context.*;
import com.imooc.pan.server.modules.file.converter.FileConverter;
import com.imooc.pan.server.modules.file.enums.DelFlagEnum;
import com.imooc.pan.server.modules.file.po.*;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.file.vo.FileChunkUploadVO;
import com.imooc.pan.server.modules.file.vo.FolderTreeNodeVO;
import com.imooc.pan.server.modules.file.vo.UploadedChunksVO;
import com.imooc.pan.server.modules.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * file module controller
 */
@RestController
@Validated
public class FileController {
    @Autowired
    private IUserFileService iUserFileService;

    @Autowired
    private FileConverter fileConverter;

    @ApiOperation(
            value = "query file list",
            notes = "the interface provides the functionality of querying the files of a specific type in a folder",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping(value = "files")
    public R<List<DriveHarborUserFileVO>> list(@NotBlank(message = "Parent folder ID cannot be blank") @RequestParam(value = "parentId", required = false) String parentId,
                                               @RequestParam(value = "fileTypes", required = false, defaultValue = FileConstants.ALL_FILE_TYPE) String fileTypes) {
        Long realParentId = -1L;
        if (!FileConstants.ALL_FILE_TYPE.equals(parentId)) {
            realParentId = IdUtil.decrypt(parentId);
        }

        List<Integer> fileTypeArray = null;
        if (!Objects.equals(FileConstants.ALL_FILE_TYPE, fileTypes)) {
            fileTypeArray = Splitter.on(driveHarborConstants.COMMON_SEPARATOR).splitToList(fileTypes).stream().map(Integer::valueOf).collect(Collectors.toList());
        }

        QueryFileListContext context = new QueryFileListContext();
        context.setParentId(realParentId);
        context.setFileTypeArray(fileTypeArray);
        context.setUserId(UserIdUtil.get());
        context.setDelFlag(DelFlagEnum.NO.getCode());

        List<DriveHarborUserFileVO> result = iUserFileService.getFileList(context);
        return R.data(result);
    }

    @ApiOperation(
            value = "create folder",
            notes = "this interface provides the functionality of creating folder",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/folder")
    public R<String> createFolder(@Validated @RequestBody CreateFolderPO createFolderPO) {
        CreateFolderContext context = fileConverter.createFolderPO2CreateFolderContext(createFolderPO);
        Long fileId = iUserFileService.createFolder(context);
        return R.data(IdUtil.encrypt(fileId));
    }

    @ApiOperation(
            value = "rename file",
            notes = "this interface provides the functionality of renaming the file",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PutMapping("file")
    public R updateFilename(@Validated @RequestBody UpdateFilenamePO updateFilenamePO) {
        UpdateFilenameContext context = fileConverter.updateFilenamePO2UpdateFilenameContext(updateFilenamePO);
        iUserFileService.updateFilename(context);
        return R.success();
    }

    @ApiOperation(
            value = "delete files",
            notes = "the interface provides the functionality of deleting files",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @DeleteMapping("file")
    public R deleteFile(@Validated @RequestBody DeleteFilePO deleteFilePO) {
        DeleteFileContext context = fileConverter.deleteFilePO2DeleteFileContext(deleteFilePO);

        String fileIds = deleteFilePO.getFileIds();
        List<Long> fileIdList = Splitter.on(driveHarborConstants.COMMON_SEPARATOR).splitToList(fileIds).stream().map(IdUtil::decrypt).collect(Collectors.toList());
        context.setFileIdList(fileIdList);
        iUserFileService.deleteFile(context);
        return R.success();
    }

    @ApiOperation(
            value = "speed file uploading",
            notes = "This interface provides the functionality of speed file uploading",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/sec-upload")
    public R secUpload(@Validated @RequestBody SecUploadFilePO secUploadFilePO) {
        SecUploadFileContext context = fileConverter.secUploadFilePO2SecUploadFileContext(secUploadFilePO);
        boolean result = iUserFileService.secUpload(context);
        if (result) {
            return R.success();
        }
        return R.fail("文件唯一标识不存在，请手动执行文件上传");
    }

    @ApiOperation(
            value = "single file uploading",
            notes = "This interface provides the functionality of single file uploading",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/upload")
    public R upload(@Validated FileUploadPO fileUploadPO) {
        FileUploadContext context = fileConverter.fileUploadPO2FileUploadContext(fileUploadPO);
        iUserFileService.upload(context);
        return R.success();
    }

    @ApiOperation(
            value = "chunking-up-and-uploading",
            notes = "this interface provides the functionality of chunking uploading",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/chunk-upload")
    public R<FileChunkUploadVO> chunkUpload(@Validated FileChunkUploadPO fileChunkUploadPO) {
        FileChunkUploadContext context = fileConverter.fileChunkUploadPO2FileChunkUploadContext(fileChunkUploadPO);
        FileChunkUploadVO vo = iUserFileService.chunkUpload(context);
        return R.data(vo);
    }

    @ApiOperation(
            value = "query uploaded chunked files",
            notes = "This interface provides the functionality of querying uploaded chunked files",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("file/chunk-upload")
    public R<UploadedChunksVO> getUploadedChunks(@Validated QueryUploadedChunksPO queryUploadedChunksPO) {
        QueryUploadedChunksContext context = fileConverter.queryUploadedChunksPO2QueryUploadedChunksContext(queryUploadedChunksPO);
        UploadedChunksVO vo = iUserFileService.getUploadedChunks(context);
        return R.data(vo);
    }

    @ApiOperation(
            value = "Merge chunks",
            notes = "This interface provides the functionality of chunks merging",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/merge")
    public R mergeFile(@Validated @RequestBody FileChunkMergePO fileChunkMergePO) {
        FileChunkMergeContext context = fileConverter.fileChunkMergePO2FileChunkMergeContext(fileChunkMergePO);
        iUserFileService.mergeFile(context);
        return R.success();
    }

    @ApiOperation(
            value = "File download",
            notes = "该接口提供了文件下载的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @GetMapping("file/download")
    public void download(@NotBlank(message = "Id cannot be blank") @RequestParam(value = "fileId", required = false) String fileId,
                         HttpServletResponse response) {
        FileDownloadContext context = new FileDownloadContext();
        context.setFileId(IdUtil.decrypt(fileId));
        context.setResponse(response);
        context.setUserId(UserIdUtil.get());
        iUserFileService.download(context);
    }

    @ApiOperation(
            value = "File preview",
            notes = "This interface provides the functionality of file preview",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @GetMapping("file/preview")
    public void preview(@NotBlank(message = "FileId cannot be null.") @RequestParam(value = "fileId", required = false) String fileId,
                        HttpServletResponse response) {
        FilePreviewContext context = new FilePreviewContext();
        context.setFileId(IdUtil.decrypt(fileId));
        context.setResponse(response);
        context.setUserId(UserIdUtil.get());
        iUserFileService.preview(context);
    }

    @ApiOperation(
            value = "Folder tree query",
            notes = "This interface provides the functionality of folder tree query",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("file/folder/tree")
    public R<List<FolderTreeNodeVO>> getFolderTree() {
        QueryFolderTreeContext context = new QueryFolderTreeContext();
        context.setUserId(UserIdUtil.get());
        List<FolderTreeNodeVO> result = iUserFileService.getFolderTree(context);
        return R.data(result);
    }

    @ApiOperation(
            value = "File transfer",
            notes = "This interface provides the functionality of file transfer",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/transfer")
    public R transfer(@Validated @RequestBody TransferFilePO transferFilePO) {
        String fileIds = transferFilePO.getFileIds();
        String targetParentId = transferFilePO.getTargetParentId();
        List<Long> fileIdList = Splitter.on(driveHarborConstants.COMMON_SEPARATOR).splitToList(fileIds).stream().map(IdUtil::decrypt).collect(Collectors.toList());
        TransferFileContext context = new TransferFileContext();
        context.setFileIdList(fileIdList);
        context.setTargetParentId(IdUtil.decrypt(targetParentId));
        context.setUserId(UserIdUtil.get());
        iUserFileService.transfer(context);
        return R.success();
    }

    @ApiOperation(
            value = "file copy",
            notes = "This interface provides the functionality of file copy",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/copy")
    public R copy(@Validated @RequestBody CopyFilePO copyFilePO) {
        String fileIds = copyFilePO.getFileIds();
        String targetParentId = copyFilePO.getTargetParentId();
        List<Long> fileIdList = Splitter.on(driveHarborConstants.COMMON_SEPARATOR).splitToList(fileIds).stream().map(IdUtil::decrypt).collect(Collectors.toList());
        CopyFileContext context = new CopyFileContext();
        context.setFileIdList(fileIdList);
        context.setTargetParentId(IdUtil.decrypt(targetParentId));
        context.setUserId(UserIdUtil.get());
        iUserFileService.copy(context);
        return R.success();
    }

}
