package com.imooc.pan.server.common.listener.file;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.server.common.event.file.FilePhysicalDeleteEvent;
import com.imooc.pan.server.common.event.log.ErrorLogEvent;
import com.imooc.pan.server.modules.file.entity.driveHarborFile;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.imooc.pan.server.modules.file.enums.FolderFlagEnum;
import com.imooc.pan.server.modules.file.service.IFileService;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.storage.engine.core.StorageEngine;
import com.imooc.pan.storage.engine.core.context.DeleteFileContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * physical file deletion listener
 */
@Component
public class FilePhysicalDeleteEventListener implements ApplicationContextAware {

    @Autowired
    private IFileService iFileService;

    @Autowired
    private IUserFileService iUserFileService;

    @Autowired
    private StorageEngine storageEngine;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * file deletion in the trash can listener
     *  release the physical resource occupied by the physical files deleted
     * 该执行器是一个资源释放器，释放被物理删除的文件列表中关联的实体文件记录
     * 1. query the physical records
     * 2. delete records
     * 3. delete physical files
     *
     * @param event
     */
    @EventListener(classes = FilePhysicalDeleteEvent.class)
//    @Async(value = "eventListenerTaskExecutor")
    public void physicalDeleteFile(FilePhysicalDeleteEvent event) {
        List<driveHarborUserFile> allRecords = event.getAllRecords();
        if (CollectionUtils.isEmpty(allRecords)) {
            return;
        }
        List<Long> realFileIdList = findAllUnusedRealFileIdList(allRecords);
        if (CollectionUtils.isEmpty(realFileIdList)) {
            return;
        }
        List<driveHarborFile> realFileRecords = iFileService.listByIds(realFileIdList);
        if (CollectionUtils.isEmpty(realFileRecords)) {
            return;
        }
        if (!iFileService.removeByIds(realFileIdList)) {
            applicationContext.publishEvent(new ErrorLogEvent(this, "Physical file：" + JSON.toJSONString(realFileIdList) + ", failed to be deleted, please delete manually", driveHarborConstants.ZERO_LONG));
            return;
        }
        physicalDeleteFileByStorageEngine(realFileRecords);
    }

    /*******************************************private*******************************************/

    /**
     * physical file delettion
     *
     * @param realFileRecords
     */
    private void physicalDeleteFileByStorageEngine(List<driveHarborFile> realFileRecords) {
        List<String> realPathList = realFileRecords.stream().map(driveHarborFile::getRealPath).collect(Collectors.toList());
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        deleteFileContext.setRealFilePathList(realPathList);
        try {
            storageEngine.delete(deleteFileContext);
        } catch (IOException e) {
            applicationContext.publishEvent(new ErrorLogEvent(this, "Physical File：" + JSON.toJSONString(realPathList) + ", failed to be deleted, please delete manually", driveHarborConstants.ZERO_LONG));
        }
    }

    /**
     * query all the records of the physical file ids that are not referred
     *
     * @param allRecords
     * @return
     */
    private List<Long> findAllUnusedRealFileIdList(List<driveHarborUserFile> allRecords) {
        List<Long> realFileIdList = allRecords.stream()
                .filter(record -> Objects.equals(record.getFolderFlag(), FolderFlagEnum.NO.getCode()))
                .filter(this::isUnused)
                .map(driveHarborUserFile::getRealFileId)
                .collect(Collectors.toList());
        return realFileIdList;
    }

    /**
     * verify that the real id is not referred
     *
     * @param record
     * @return
     */
    private boolean isUnused(driveHarborUserFile record) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("real_file_id", record.getRealFileId());
        return iUserFileService.count(queryWrapper) == driveHarborConstants.ZERO_INT.intValue();
    }


}
