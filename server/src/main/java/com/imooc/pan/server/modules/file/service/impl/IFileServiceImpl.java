package com.imooc.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.core.utils.FileUtil;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.common.stream.channel.DriveHarborChannels;
import com.imooc.pan.server.common.stream.event.log.ErrorLogEvent;
import com.imooc.pan.server.modules.file.context.FileChunkMergeAndSaveContext;
import com.imooc.pan.server.modules.file.entity.driveHarborFileChunk;
import com.imooc.pan.server.modules.file.service.IFileChunkService;
import com.imooc.pan.storage.engine.core.context.DeleteFileContext;
import com.imooc.pan.server.modules.file.entity.driveHarborFile;
import com.imooc.pan.server.modules.file.service.IFileService;
import com.imooc.pan.server.modules.file.mapper.driveHarborFileMapper;
import com.imooc.pan.server.modules.file.context.FileSaveContext;
import com.imooc.pan.storage.engine.core.StorageEngine;
import com.imooc.pan.storage.engine.core.context.MergeFileContext;
import com.imooc.pan.storage.engine.core.context.StoreFileContext;
import com.imooc.pan.stream.core.IStreamProducer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author benchi
* @description 针对表【r_pan_file(physical_files_information_table)】的数据库操作Service实现
* @createDate 2023-10-23 21:04:43
*/
@Service
public class IFileServiceImpl extends ServiceImpl<driveHarborFileMapper, driveHarborFile>
    implements IFileService{

    @Autowired
    private StorageEngine storageEngine;


    @Autowired
    private IFileChunkService iFileChunkService;

    @Autowired
    @Qualifier(value = "defaultStreamProducer")
    private IStreamProducer producer;


    /**
     * 1. uplad file
     * 2. save the record
     *
     * @param context
     */
    @Override
    public void saveFile(FileSaveContext context) {
        storeMultipartFile(context);
        driveHarborFile record = doSaveFile(context.getFilename(),
                context.getRealPath(),
                context.getTotalSize(),
                context.getIdentifier(),
                context.getUserId());
        context.setRecord(record);
    }

    /**
     * 1. merging implemented by the storage engine
     * 2. save the record
     * @param context
     */
    @Override
    public void mergeFileChunkAndSaveFile(FileChunkMergeAndSaveContext context) {
        doMergeFileChunk(context);
        driveHarborFile record = doSaveFile(context.getFilename(), context.getRealPath(), context.getTotalSize(), context.getIdentifier(), context.getUserId());
        context.setRecord(record);

    }
    /****************************private******************************************/

    /**
     * merging implemented by storage engine
     * 1. look up chunks record
     * 2. merge the chunks based on the record
     * 3. delete the chunks record
     * 2. assemble the real path to the context
     * @param context
     */
    private void doMergeFileChunk(FileChunkMergeAndSaveContext context) {
        QueryWrapper<driveHarborFileChunk> queryWrapper = Wrappers.query();
        queryWrapper.eq("identifier", context.getIdentifier());
        queryWrapper.eq("create_user", context.getUserId());
        queryWrapper.ge("expiration_time", new Date());
        List<driveHarborFileChunk> chunkRecoredList = iFileChunkService.list(queryWrapper);
        if (CollectionUtils.isEmpty(chunkRecoredList)) {
            throw new driveHarborBusinessException("No chunk record found");
        }
        List<String> realPathList = chunkRecoredList.stream()
                .sorted(Comparator.comparing(driveHarborFileChunk::getChunkNumber))
                .map(driveHarborFileChunk::getRealPath)
                .collect(Collectors.toList());

        try {
            MergeFileContext mergeFileContext = new MergeFileContext();
            mergeFileContext.setFilename(context.getFilename());
            mergeFileContext.setIdentifier(context.getIdentifier());
            mergeFileContext.setUserId(context.getUserId());
            mergeFileContext.setRealPathList(realPathList);
            storageEngine.mergeFile(mergeFileContext);
            context.setRealPath(mergeFileContext.getRealPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new driveHarborBusinessException("Merge failure");
        }

        List<Long> fileChunkRecordIdList = chunkRecoredList.stream().map(driveHarborFileChunk::getId).collect(Collectors.toList());
        iFileChunkService.removeByIds(fileChunkRecordIdList);
    }
    /**
     * single file uploading
     * delegate to storage engine
     *
     * @param filename
     * @param realPath
     * @param totalSize
     * @param identifier
     * @param userId
     * @return
     */
    private driveHarborFile doSaveFile(String filename, String realPath, Long totalSize, String identifier, Long userId) {
        driveHarborFile record = assembleRPanFile(filename, realPath, totalSize, identifier, userId);
        if (!save(record)) {
            try {
                DeleteFileContext deleteFileContext = new DeleteFileContext();
                deleteFileContext.setRealFilePathList(Lists.newArrayList(realPath));
                storageEngine.delete(deleteFileContext);
            } catch (IOException e) {
                e.printStackTrace();
                ErrorLogEvent errorLogEvent = new ErrorLogEvent("Physical deletion failure, please manually delete, path: " + realPath, userId);
                producer.sendMessage(DriveHarborChannels.ERROR_LOG_OUTPUT, errorLogEvent);
            }
        }
        return record;
    }

    private driveHarborFile assembleRPanFile(String filename, String realPath, Long totalSize, String identifier, Long userId) {
        driveHarborFile record = new driveHarborFile();

        record.setFileId(IdUtil.get());
        record.setFilename(filename);
        record.setRealPath(realPath);
        record.setFileSize(String.valueOf(totalSize));
        record.setFileSizeDesc(FileUtil.byteCountToDisplaySize(totalSize));
        record.setFileSuffix(FileUtil.getFileSuffix(filename));
        record.setIdentifier(identifier);
        record.setCreateUser(userId);
        record.setCreateTime(new Date());

        return record;
    }

    private void storeMultipartFile(FileSaveContext context) {
        try {
            StoreFileContext storeFileContext = new StoreFileContext();
            storeFileContext.setInputStream(context.getFile().getInputStream());
            storeFileContext.setFilename(context.getFilename());
            storeFileContext.setTotalSize(context.getTotalSize());
            storageEngine.store(storeFileContext);
            context.setRealPath(storeFileContext.getRealPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new driveHarborBusinessException("Upload failed.");
        }
    }
}






