package com.imooc.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.core.utils.FileUtil;
import com.imooc.pan.server.common.event.file.DeleteFileEvent;
import com.imooc.pan.server.modules.file.context.*;
import com.imooc.pan.server.modules.file.converter.FileConverter;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.imooc.pan.server.modules.file.enums.DelFlagEnum;
import com.imooc.pan.server.modules.file.enums.FileTypeEnum;
import com.imooc.pan.server.modules.file.enums.FolderFlagEnum;
import com.imooc.pan.server.modules.file.service.IFileChunkService;
import com.imooc.pan.server.modules.file.service.IFileService;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.server.modules.file.mapper.driverHarborUserFileMapper;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.file.vo.FileChunkUploadVO;
import com.imooc.pan.server.modules.file.vo.UploadedChunksVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.modules.file.constants.FileConstants;
import com.imooc.pan.server.modules.file.entity.driveHarborFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author benchi
* @description 针对表【r_pan_user_file(user file information table)】的数据库操作Service实现
* @createDate 2023-10-23 21:04:43
*/
@Service(value = "userFileService")
public class UserFileServiceImpl extends ServiceImpl<driverHarborUserFileMapper, driveHarborUserFile> implements IUserFileService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private IFileService iFileService;

    @Autowired
    private FileConverter fileConverter;

    @Autowired
    private IFileChunkService iFileChunkService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Long createFolder(CreateFolderContext createFolderContext) {
        return saveUserFile(createFolderContext.getParentId(),
                createFolderContext.getFolderName(),
                FolderFlagEnum.YES,
                null,
                null,
                createFolderContext.getUserId(),
                null);
    }

    @Override
    public driveHarborUserFile getUserRootFile(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("parent_id", FileConstants.TOP_PARENT_ID);
        queryWrapper.eq("del_flag", DelFlagEnum.NO.getCode());
        queryWrapper.eq("folder_flag", FolderFlagEnum.YES.getCode());
        return getOne(queryWrapper);
    }

    @Override
    public List<DriveHarborUserFileVO> getFileList(QueryFileListContext context) {

        return baseMapper.selectFileList(context);
    }

    /**
     * update file name
     * 1. verfify the condition for the file name to be updated
     * 2. update the file name
     * @param context
     */
    @Override
    public void updateFilename(UpdateFilenameContext context) {
        checkUpdateFilenameCondition(context);
        doUpdateFilename(context);
    }

    /**
     * 1. verify the conditions to execute the delete operation
     * 2. delete
     * 3. generate the delete event for other modules to refer to
     * @param context
     */
    @Override
    public void deleteFile(DeleteFileContext context) {
        checkFileDeleteCondition(context);
        doDeleteFile(context);
        afterFileDelete(context);
    }

    /**
     * 1. look up the file thru the identifier
     * 2. return failure if lookup fails
     * 3. return success if lookup succeeds
     * @param context
     * @return
     */
    @Override
    public boolean secUpload(SecUploadFileContext context) {
        driveHarborFile record = getFileListByUserIdAndIdentifier(context.getUserId(), context.getIdentifier());
        if(Objects.isNull(record)){
            return false;
        }
        saveUserFile(context.getParentId(),
                context.getFilename(),
                FolderFlagEnum.NO,
                FileTypeEnum.getFileTypeCode(FileUtil.getFileSuffix(context.getFilename())),
                record.getFileId(),
                context.getUserId(),
                record.getFileSizeDesc());
        return true;
    }

    /**
     * 1. upload and save the record
     * 2.save file and user
     * @param context
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void upload(FileUploadContext context) {
        saveFile(context);
        saveUserFile(context.getParentId(),
                context.getFilename(),
                FolderFlagEnum.NO,
                FileTypeEnum.getFileTypeCode(FileUtil.getFileSuffix(context.getFilename())),
                context.getRecord().getFileId(),
                context.getUserId(),
                context.getRecord().getFileSizeDesc());
    }

    /**
     * 1. upload the file
     * 2. save chunks
     * 3. verify if the uploading is completed
     * @param context
     * @return
     */
    @Override
    public FileChunkUploadVO chunkUpload(FileChunkUploadContext context) {

        FileChunkSaveContext fileChunkSaveContext = fileConverter.fileChunkUploadContext2FileChunkSaveContext(context);
        iFileChunkService.saveChunkFile(fileChunkSaveContext);
        FileChunkUploadVO vo = new FileChunkUploadVO();
        vo.setMergeFlag(fileChunkSaveContext.getMergeFlagEnum().getCode());
        return vo;
    }

    @Override
    public UploadedChunksVO getUploadedChunks(QueryUploadedChunksContext context) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.select("chunk_number");
        queryWrapper.eq("identifier", context.getIdentifier());
        queryWrapper.eq("create_user", context.getUserId());
        queryWrapper.gt("expiration_time", new Date());

        List<Integer> uploadedChunks = iFileChunkService.listObjs(queryWrapper, value -> (Integer) value);

        UploadedChunksVO vo = new UploadedChunksVO();
        vo.setUploadedChunks(uploadedChunks);
        return vo;

    }

    /**
     * 1. merge chunks
     * 2. save record
     * 3. save the user and file mapping
     * @param context
     */
    @Override
    public void mergeFile(FileChunkMergeContext context) {
        mergeFileChunkAndSaveFile(context);
        saveUserFile(context.getParentId(),
                context.getFilename(),
                FolderFlagEnum.NO,
                FileTypeEnum.getFileTypeCode(FileUtil.getFileSuffix(context.getFilename())),
                context.getRecord().getFileId(),
                context.getUserId(),
                context.getRecord().getFileSizeDesc());

    }

    private void mergeFileChunkAndSaveFile(FileChunkMergeContext context) {
        FileChunkMergeAndSaveContext fileChunkMergeAndSaveContext = fileConverter.fileChunkMergeContext2FileChunkMergeAndSaveContext(context);
        iFileService.mergeFileChunkAndSaveFile(fileChunkMergeAndSaveContext);
        context.setRecord(fileChunkMergeAndSaveContext.getRecord());
    }

    private void saveFile(FileUploadContext context) {
        FileSaveContext fileSaveContext = fileConverter.fileUploadContext2FileSaveContext(context);
        iFileService.saveFile(fileSaveContext);
        context.setRecord(fileSaveContext.getRecord());
    }

    private driveHarborFile getFileListByUserIdAndIdentifier(Long userId, String identifier) {
        QueryWrapper wrapper = Wrappers.query();
        wrapper.eq("create_user", userId);
        wrapper.eq("identifier", identifier);
        List<driveHarborFile> records = iFileService.list(wrapper);
        if(CollectionUtils.isEmpty(records)){
            return null;
        }

        return records.get(driveHarborConstants.ZERO_INT);

    }

    private Long saveUserFile(Long parentId,
                              String fileName,
                              FolderFlagEnum folderFlagEnum,
                              Integer fileType,
                              Long realFileId,
                              Long userId,
                              String fileSizeDesc){
        driveHarborUserFile entity = assembleDriveHarborUserFile(parentId, userId, fileName, folderFlagEnum, fileType, realFileId, fileSizeDesc);
        if (!save((entity))) {
            throw new driveHarborBusinessException("Failed to save file info");
        }
        return entity.getFileId();

    }

    private driveHarborUserFile assembleDriveHarborUserFile(Long parentId, Long userId, String fileName, FolderFlagEnum folderFlagEnum, Integer fileType, Long realFileId, String fileSizeDesc) {
        driveHarborUserFile entity = new driveHarborUserFile();

        entity.setFileId(IdUtil.get());
        entity.setUserId(userId);
        entity.setParentId(parentId);
        entity.setRealFileId(realFileId);
        entity.setFileName(fileName);
        entity.setFolderFlag(folderFlagEnum.getCode());
        entity.setFileSizeDesc(fileSizeDesc);
        entity.setFileType(fileType);
        entity.setDelFlag(DelFlagEnum.NO.getCode());
        entity.setCreateUser(userId);
        entity.setCreateTime(new Date());
        entity.setUpdateUser(userId);
        entity.setUpdateTime(new Date());

        handleDuplicateFilename(entity);

        return entity;
    }

    /**
     * handle duplicate files names
     * @param entity
     */
    private void handleDuplicateFilename(driveHarborUserFile entity) {
        String filename = entity.getFileName(),
                newFilenameWithoutSuffix,
                newFilenameSuffix;
        int newFilenamePointPosition = filename.lastIndexOf(driveHarborConstants.POINT_STR);
        if (newFilenamePointPosition == driveHarborConstants.MINUS_ONE_INT) {
            newFilenameWithoutSuffix = filename;
            newFilenameSuffix = StringUtils.EMPTY;
        } else {
            newFilenameWithoutSuffix = filename.substring(driveHarborConstants.ZERO_INT, newFilenamePointPosition);
            newFilenameSuffix = filename.replace(newFilenameWithoutSuffix, StringUtils.EMPTY);
        }

        int count = getDuplicateFilename(entity, newFilenameWithoutSuffix);
        if(count == 0) {
            return;
        }
        String newFilename = assembleNewFileName(newFilenameWithoutSuffix, count, newFilenameSuffix);
        entity.setFileName(newFilename);
    }

    /**
     * aeemble new file name.
     * the duplicated file should keep the duplicated name with suffix like "(1)"
     * for example, "photo(1).jpeg"
     * @param newFilenameWithoutSuffix
     * @param count
     * @param newFilenameSuffix
     * @return
     */
    private String assembleNewFileName(String newFilenameWithoutSuffix, int count, String newFilenameSuffix) {
        String newFilename = new StringBuilder(newFilenameWithoutSuffix)
                .append(FileConstants.LEFT_PARENTHESES_STR)
                .append(count)
                .append(FileConstants.RIGHT_PARENTHESES_STR)
                .append(newFilenameSuffix)
                .toString();
        return newFilename;
    }

    /**
     * look up the number of duplicates
     * @param entity
     * @param newFilenameWithoutSuffix
     * @return
     */
    private int getDuplicateFilename(driveHarborUserFile entity, String newFilenameWithoutSuffix) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", entity.getParentId());
        queryWrapper.eq("folder_flag", entity.getFolderFlag());
        queryWrapper.eq("user_id", entity.getUserId());
        queryWrapper.eq("del_flag", DelFlagEnum.NO.getCode());
        queryWrapper.likeRight("filename", newFilenameWithoutSuffix);
        return count(queryWrapper);
    }

    /**
     * set the new file name
     * @param context
     */
    private void doUpdateFilename(UpdateFilenameContext context) {
        driveHarborUserFile entity = context.getEntity();
        entity.setFileName(context.getNewFilename());
        entity.setUpdateUser(context.getUserId());
        entity.setUpdateTime(new Date());

        if (!updateById(entity)) {
            throw new driveHarborBusinessException("rename failure");
        }
    }

    /**
     * conditions:
     * 1. valid file id
     * 2. current user has the permission to change the file name
     * 3. new name cannot be the same as the old name
     * 4. cannot use names same as other files in the current folder
     * @param context
     */
    private void checkUpdateFilenameCondition(UpdateFilenameContext context) {

        Long fileId = context.getFileId();
        driveHarborUserFile entity = getById(fileId);

        if (Objects.isNull(entity)) {
            throw new driveHarborBusinessException("invalid file id");
        }

        if (!Objects.equals(entity.getUserId(), context.getUserId())) {
            throw new driveHarborBusinessException("No permission for the current user to change the file name");
        }

        if (Objects.equals(entity.getFileName(), context.getNewFilename())) {
            throw new driveHarborBusinessException("Please use different name");
        }


        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", entity.getParentId());
        queryWrapper.eq("filename", context.getNewFilename());
        int count = count(queryWrapper);

        if (count > 0) {
            throw new driveHarborBusinessException("The name has existed in the current folder");
        }

        context.setEntity(entity);
    }

    /**
     * 1. validate the file ids
     * 2. user has the permission to delete ths files
     * @param context
     */
    private void checkFileDeleteCondition(DeleteFileContext context) {
        List<Long> fileIdList = context.getFileIdList();

        List<driveHarborUserFile> harborUserFiles = listByIds(fileIdList);
        if (harborUserFiles.size() != fileIdList.size()) {
            throw new driveHarborBusinessException("illegal files");
        }

        Set<Long> fileIdSet = harborUserFiles.stream().map(driveHarborUserFile::getFileId).collect(Collectors.toSet());
        int oldSize = fileIdSet.size();
        fileIdSet.addAll(fileIdList);
        int newSize = fileIdSet.size();

        if (oldSize != newSize) {
            throw new driveHarborBusinessException("illegal files");
        }

        Set<Long> userIdSet = harborUserFiles.stream().map(driveHarborUserFile::getUserId).collect(Collectors.toSet());
        if (userIdSet.size() != 1) {
            throw new driveHarborBusinessException("illegal files");
        }

        Long dbUserId = userIdSet.stream().findFirst().get();
        if (!Objects.equals(dbUserId, context.getUserId())) {
            throw new driveHarborBusinessException("Current user doesn't have the permission to delete the files");
        }
    }

    /**
     * event generation after file deletion
     * <p>
     * 1、对外发布文件删除的事件
     *
     * @param context
     */
    private void afterFileDelete(DeleteFileContext context) {
        DeleteFileEvent deleteFileEvent = new DeleteFileEvent(this, context.getFileIdList());
        applicationContext.publishEvent(deleteFileEvent);
    }

    /**
     * execute the deletion operation
     *
     * @param context
     */
    private void doDeleteFile(DeleteFileContext context) {
        List<Long> fileIdList = context.getFileIdList();

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.in("file_id", fileIdList);
        updateWrapper.set("del_flag", DelFlagEnum.YES.getCode());
        updateWrapper.set("update_time", new Date());

        if (!update(updateWrapper)) {
            throw new driveHarborBusinessException("delete failure");
        }
    }


}




