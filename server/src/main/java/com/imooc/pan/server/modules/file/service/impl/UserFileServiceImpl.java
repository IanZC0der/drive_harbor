package com.imooc.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.core.utils.FileUtil;
import com.imooc.pan.server.common.event.event.UserSearchEvent;
import com.imooc.pan.server.common.event.file.DeleteFileEvent;
import com.imooc.pan.server.common.utils.HttpUtil;
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
import com.imooc.pan.server.modules.file.vo.*;
import com.imooc.pan.storage.engine.core.AbstractStorageEngine;
import com.imooc.pan.storage.engine.core.StorageEngine;
import com.imooc.pan.storage.engine.core.context.ReadFileContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.modules.file.constants.FileConstants;
import com.imooc.pan.server.modules.file.entity.driveHarborFile;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
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
    @Autowired
    private StorageEngine storageEngine;

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

    /**
     * 1. check: file exists, the file belongs to the current user
     * 2. check if it's a folder
     * 3. download
     * @param context
     */
    @Override
    public void download(FileDownloadContext context) {
        driveHarborUserFile record = getById(context.getFileId());
        checkOperatePermission(record, context.getUserId());
        if (checkIsFolder(record)) {
            throw new driveHarborBusinessException("Folder cannot be downloaded.");
        }
        doDownload(record, context.getResponse());
    }

    /**
     * 1. check: file exists, the file belongs to the current user
     * 2. check if it's a folder
     * 3. preview
     * @param context
     */
    @Override
    public void preview(FilePreviewContext context) {
        driveHarborUserFile record = getById(context.getFileId());
        checkOperatePermission(record, context.getUserId());
        if (checkIsFolder(record)) {
            throw new driveHarborBusinessException("Folder cannot be previewed.");
        }
        doPreview(record, context.getResponse());
    }

    /**
     * query folder tree: non recursive
     * 1. query the folder list of this user
     * 2. assemble the folder tree in the buffer
     *
     * @param context
     * @return
     */
    @Override
    public List<FolderTreeNodeVO> getFolderTree(QueryFolderTreeContext context) {
        List<driveHarborUserFile> folderRecords = queryFolderRecords(context.getUserId());
        List<FolderTreeNodeVO> result = assembleFolderTreeNodeVOList(folderRecords);
        return result;

    }

    /**
     * 1. check transfer conditions
     * 2. transfer
     * @param context
     */
    @Override
    public void transfer(TransferFileContext context) {
        checkTransferCondition(context);
        doTransfer(context);
    }

    /**
     * copy files
     * 1. check conditions
     * 2. do copy
     * @param context
     */
    @Override
    public void copy(CopyFileContext context) {
        checkCopyCondition(context);
        doCopy(context);
    }

    /**
     * 1. do search
     * 2. assemble parent folder names to return to the front end
     * 3. after search: e.g, event
     * @param context
     * @return
     */
    @Override
    public List<FileSearchResultVO> search(FileSearchContext context) {
        List<FileSearchResultVO> result = doSearch(context);
        fillParentFilename(result);
        afterSearch(context);
        return result;
    }

    /**
     * get folders info
     * ssemble the folders info to a list
     * @param context
     * @return
     */
    @Override
    public List<BreadcrumbVO> getBreadcrumbs(QueryBreadcrumbsContext context) {
        List<driveHarborUserFile> folderRecords = queryFolderRecords(context.getUserId());
        Map<Long, BreadcrumbVO> prepareBreadcrumbVOMap = folderRecords.stream().map(BreadcrumbVO::transfer).collect(Collectors.toMap(BreadcrumbVO::getId, a -> a));
        BreadcrumbVO currentNode;
        Long fileId = context.getFileId();
        List<BreadcrumbVO> result = Lists.newLinkedList();
        do {
            currentNode = prepareBreadcrumbVOMap.get(fileId);
            if (Objects.nonNull(currentNode)) {
                result.add(0, currentNode);
                fileId = currentNode.getParentId();
            }
        } while (Objects.nonNull(currentNode));
        return result;
    }

    /**
     * recursively query the file records
     * the file could be a folder, so we need to keep querying until there is no folder left to be queried
     * @param records
     * @return
     */
    @Override
    public List<driveHarborUserFile> findAllFileRecords(List<driveHarborUserFile> records) {
        List<driveHarborUserFile> result = Lists.newArrayList(records);
        if (CollectionUtils.isEmpty(result)) {
            return result;
        }
        long folderCount = result.stream().filter(record -> Objects.equals(record.getFolderFlag(), FolderFlagEnum.YES.getCode())).count();
        if (folderCount == 0) {
            return result;
        }
        records.stream().forEach(record -> doFindAllChildRecords(result, record));
        return result;

    }

    private void doFindAllChildRecords(List<driveHarborUserFile> result, driveHarborUserFile record) {
        if (Objects.isNull(record)) {
            return;
        }
        if (!checkIsFolder(record)) {
            return;
        }
        List<driveHarborUserFile> childRecords = findChildRecordsIgnoreDelFlag(record.getFileId());
        if (CollectionUtils.isEmpty(childRecords)) {
            return;
        }
        result.addAll(childRecords);
        childRecords.stream()
                .filter(childRecord -> FolderFlagEnum.YES.getCode().equals(childRecord.getFolderFlag()))
                .forEach(childRecord -> doFindAllChildRecords(result, childRecord));
    }

    private List<driveHarborUserFile> findChildRecordsIgnoreDelFlag(Long fileId) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("parent_id", fileId);
        List<driveHarborUserFile> childRecords = list(queryWrapper);
        return childRecords;
    }


    /**
     * 1. publish event
     *
     * @param context
     */
    private void afterSearch(FileSearchContext context) {
        UserSearchEvent event = new UserSearchEvent(this, context.getKeyword(), context.getUserId());
        applicationContext.publishEvent(event);
    }


    /**
     * assemble parent folder names
     *
     * @param result
     */
    private void fillParentFilename(List<FileSearchResultVO> result) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        List<Long> parentIdList = result.stream().map(FileSearchResultVO::getParentId).collect(Collectors.toList());
        List<driveHarborUserFile> parentRecords = listByIds(parentIdList);
        Map<Long, String> fileId2filenameMap = parentRecords.stream().collect(Collectors.toMap(driveHarborUserFile::getFileId, driveHarborUserFile::getFileName));
        result.stream().forEach(vo -> vo.setParentFilename(fileId2filenameMap.get(vo.getParentId())));
    }

    /**
     * search files
     *
     * @param context
     * @return
     */
    private List<FileSearchResultVO> doSearch(FileSearchContext context) {
        return baseMapper.searchFile(context);
    }

    /**
     * file copy condition check
     *
     * 1. target should be folder
     * 2. files to be copied should not include the target folder or its child folders
     *
     * @param context
     */
    private void checkCopyCondition(CopyFileContext context) {
        Long targetParentId = context.getTargetParentId();
        if (!checkIsFolder(getById(targetParentId))) {
            throw new driveHarborBusinessException("Target is not a folder.");
        }
        List<Long> fileIdList = context.getFileIdList();
        List<driveHarborUserFile> prepareRecords = listByIds(fileIdList);
        context.setPrepareRecords(prepareRecords);
        if (checkIsChildFolder(prepareRecords, targetParentId, context.getUserId())) {
            throw new driveHarborBusinessException("files to be copied should not include the target folder or its child folders");
        }
    }

    /**
     * do copy
     *
     * @param context
     */
    private void doCopy(CopyFileContext context) {
        List<driveHarborUserFile> prepareRecords = context.getPrepareRecords();
        if (CollectionUtils.isNotEmpty(prepareRecords)) {
            List<driveHarborUserFile> allRecords = Lists.newArrayList();
            prepareRecords.stream().forEach(record -> assembleCopyChildRecord(allRecords, record, context.getTargetParentId(), context.getUserId()));
            if (!saveBatch(allRecords)) {
                throw new driveHarborBusinessException("Copy failure.");
            }
        }
    }

    /**
     * assemble copy
     *
     * @param allRecords
     * @param record
     * @param targetParentId
     * @param userId
     */
    private void assembleCopyChildRecord(List<driveHarborUserFile> allRecords, driveHarborUserFile record, Long targetParentId, Long userId) {
        Long newFileId = IdUtil.get();
        Long oldFileId = record.getFileId();

        record.setParentId(targetParentId);
        record.setFileId(newFileId);
        record.setUserId(userId);
        record.setCreateUser(userId);
        record.setCreateTime(new Date());
        record.setUpdateUser(userId);
        record.setUpdateTime(new Date());
        handleDuplicateFilename(record);

        allRecords.add(record);

        if (checkIsFolder(record)) {
            List<driveHarborUserFile> childRecords = findChildRecords(oldFileId);
            if (CollectionUtils.isEmpty(childRecords)) {
                return;
            }
            childRecords.stream().forEach(childRecord -> assembleCopyChildRecord(allRecords, childRecord, newFileId, userId));
        }

    }

    /**
     * find children of the folder to be copied
     *
     * @param parentId
     * @return
     */
    private List<driveHarborUserFile> findChildRecords(Long parentId) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.eq("del_flag", DelFlagEnum.NO.getCode());
        return list(queryWrapper);
    }

    private void doTransfer(TransferFileContext context) {
        List<driveHarborUserFile> prepareRecords = context.getPrepareRecords();
        prepareRecords.stream().forEach(record -> {
            record.setParentId(context.getTargetParentId());
            record.setUserId(context.getUserId());
            record.setCreateUser(context.getUserId());
            record.setCreateTime(new Date());
            record.setUpdateUser(context.getUserId());
            record.setUpdateTime(new Date());
            handleDuplicateFilename(record);
        });
        if (!updateBatchById(prepareRecords)) {
            throw new driveHarborBusinessException("Transfer failure.");
        }
    }

    /**
     * target should be folder
     * the files to be transferred should not include the target folder nor its children
     * @param context
     */
    private void checkTransferCondition(TransferFileContext context) {
        Long targetParentId = context.getTargetParentId();
        if (!checkIsFolder(getById(targetParentId))) {
            throw new driveHarborBusinessException("Target is not a folder.");
        }
        List<Long> fileIdList = context.getFileIdList();
        List<driveHarborUserFile> prepareRecords = listByIds(fileIdList);
        context.setPrepareRecords(prepareRecords);
        if (checkIsChildFolder(prepareRecords, targetParentId, context.getUserId())) {
            throw new driveHarborBusinessException("Target folder and its children should be included.");
        }

    }

    /**
     * 1. selected files don't include a folder, return false
     * 2. check if the target id is included
     * @param prepareRecords
     * @param targetParentId
     * @param userId
     * @return
     */
    private boolean checkIsChildFolder(List<driveHarborUserFile> prepareRecords, Long targetParentId, Long userId) {
        prepareRecords = prepareRecords.stream().filter(record -> Objects.equals(record.getFolderFlag(), FolderFlagEnum.YES.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(prepareRecords)) {
            return false;
        }
        List<driveHarborUserFile> folderRecords = queryFolderRecords(userId);
        Map<Long, List<driveHarborUserFile>> folderRecordMap = folderRecords.stream().collect(Collectors.groupingBy(driveHarborUserFile::getParentId));
        List<driveHarborUserFile> unavailableFolderRecords = Lists.newArrayList();
        unavailableFolderRecords.addAll(prepareRecords);
        prepareRecords.stream().forEach(record -> findAllChildFolderRecords(unavailableFolderRecords, folderRecordMap, record));
        List<Long> unavailableFolderRecordIds = unavailableFolderRecords.stream().map(driveHarborUserFile::getFileId).collect(Collectors.toList());
        return unavailableFolderRecordIds.contains(targetParentId);
    }

    /**
     * find all the child folders
     * @param unavailableFolderRecords
     * @param folderRecordMap
     * @param record
     */
    private void findAllChildFolderRecords(List<driveHarborUserFile> unavailableFolderRecords, Map<Long, List<driveHarborUserFile>> folderRecordMap, driveHarborUserFile record) {
        if (Objects.isNull(record)) {
            return;
        }
        List<driveHarborUserFile> childFolderRecords = folderRecordMap.get(record.getFileId());
        if (CollectionUtils.isEmpty(childFolderRecords)) {
            return;
        }
        unavailableFolderRecords.addAll(childFolderRecords);
        childFolderRecords.stream().forEach(childRecord -> findAllChildFolderRecords(unavailableFolderRecords, folderRecordMap, childRecord));
    }

    /**
     * assemble tree node list
     * @param folderRecords
     * @return
     */
    private List<FolderTreeNodeVO> assembleFolderTreeNodeVOList(List<driveHarborUserFile> folderRecords) {
        if (CollectionUtils.isEmpty(folderRecords)) {
            return Lists.newArrayList();
        }
        List<FolderTreeNodeVO> mappedFolderTreeNodeVOList = folderRecords.stream().map(fileConverter::driveHarborUserFile2FolderTreeNodeVO).collect(Collectors.toList());
        Map<Long, List<FolderTreeNodeVO>> mappedFolderTreeNodeVOMap = mappedFolderTreeNodeVOList.stream().collect(Collectors.groupingBy(FolderTreeNodeVO::getParentId));
        for (FolderTreeNodeVO node : mappedFolderTreeNodeVOList) {
            List<FolderTreeNodeVO> children = mappedFolderTreeNodeVOMap.get(node.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                node.getChildren().addAll(children);
            }
        }
        return mappedFolderTreeNodeVOList.stream().filter(node -> Objects.equals(node.getParentId(), FileConstants.TOP_PARENT_ID)).collect(Collectors.toList());
    }

    /**
     * query all the folders
     * @param userId
     * @return
     */
    private List<driveHarborUserFile> queryFolderRecords(Long userId) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("folder_flag", FolderFlagEnum.YES.getCode());
        queryWrapper.eq("del_flag", DelFlagEnum.NO.getCode());
        return list(queryWrapper);
    }

    /**
     * 1, lookup
     * 2. add common response header
     * 3. write to the output stream
     * @param record
     * @param response
     */
    private void doPreview(driveHarborUserFile record, HttpServletResponse response) {
        driveHarborFile realFileRecord = iFileService.getById(record.getRealFileId());
        if (Objects.isNull(realFileRecord)) {
            throw new driveHarborBusinessException("File doesn't exist.");
        }
        addCommonResponseHeader(response, realFileRecord.getFilePreviewContentType());
        realFile2OutputStream(realFileRecord.getRealPath(), response);
    }

    /**
     * do download
     * 1. look up the path
     * 2. add cross-region common response header
     * 3. read the content to the output stream
     * @param record
     * @param response
     */
    private void doDownload(driveHarborUserFile record, HttpServletResponse response) {
        driveHarborFile realFileRecord = iFileService.getById(record.getRealFileId());
        if (Objects.isNull(realFileRecord)) {
            throw new driveHarborBusinessException("File doesn't exist.");
        }
        addCommonResponseHeader(response, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        addDownloadAttribute(response, record, realFileRecord);
        realFile2OutputStream(realFileRecord.getRealPath(), response);
    }

    /**
     * add common response header
     * @param response
     * @param contentTypeValue
     */
    private void addCommonResponseHeader(HttpServletResponse response, String contentTypeValue) {
        response.reset();
        HttpUtil.addCorsResponseHeaders(response);
        response.addHeader(FileConstants.CONTENT_TYPE_STR, contentTypeValue);
        response.setContentType(contentTypeValue);
    }

    /**
     * add download attributes
     * @param response
     * @param record
     * @param realFileRecord
     */
    private void addDownloadAttribute(HttpServletResponse response, driveHarborUserFile record, driveHarborFile realFileRecord) {
        try {
            response.addHeader(FileConstants.CONTENT_DISPOSITION_STR,
                    FileConstants.CONTENT_DISPOSITION_VALUE_PREFIX_STR + new String(record.getFileName().getBytes(FileConstants.GB2312_STR), FileConstants.IOS_8859_1_STR));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new driveHarborBusinessException("Download failure.");
        }
        response.setContentLengthLong(Long.valueOf(realFileRecord.getFileSize()));
    }

    /**
     * read file to the output stream
     * storage engine reads the contents and write it to the output stream
     * @param realPath
     * @param response
     */
    private void realFile2OutputStream(String realPath, HttpServletResponse response) {
        try {
            ReadFileContext context = new ReadFileContext();
            context.setRealPath(realPath);
            context.setOutputStream(response.getOutputStream());
            storageEngine.realFile(context);
        } catch (IOException e) {
            e.printStackTrace();
            throw new driveHarborBusinessException("Download failure.");
        }
    }

    /**
     * check to see if it's a folder
     * folder cannot be downloaded
     * @param record
     * @return
     */
    private boolean checkIsFolder(driveHarborUserFile record) {
        if (Objects.isNull(record)) {
            throw new driveHarborBusinessException("File doesn't exist.");
        }
        return FolderFlagEnum.YES.getCode().equals(record.getFolderFlag());
    }

    /**
     * check permission
     * 1. file exists
     * 2. permission
     * @param record
     * @param userId
     */
    private void checkOperatePermission(driveHarborUserFile record, Long userId) {
        if (Objects.isNull(record)) {
            throw new driveHarborBusinessException("File doesn't exist.");
        }
        if (!record.getUserId().equals(userId)) {
            throw new driveHarborBusinessException("No permission.");
        }

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




