package com.imooc.pan.server.modules.recycle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.server.common.stream.channel.DriveHarborChannels;
import com.imooc.pan.server.common.stream.event.file.FilePhysicalDeleteEvent;
import com.imooc.pan.server.common.stream.event.file.FileRestoreEvent;
import com.imooc.pan.server.modules.file.context.QueryFileListContext;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.imooc.pan.server.modules.file.enums.DelFlagEnum;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.recycle.context.DeleteContext;
import com.imooc.pan.server.modules.recycle.context.QueryRecycleFileListContext;
import com.imooc.pan.server.modules.recycle.context.RestoreContext;
import com.imooc.pan.server.modules.recycle.service.IRecycleService;
import com.imooc.pan.stream.core.IStreamProducer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecycleServiceImpl implements IRecycleService{
    @Autowired
    private IUserFileService iUserFileService;

    @Autowired
    @Qualifier(value = "defaultStreamProducer")
    private IStreamProducer producer;


    /**
     * query the file list in the trash can
     * @param context
     * @return
     */
    @Override
    public List<DriveHarborUserFileVO> recycles(QueryRecycleFileListContext context) {
        QueryFileListContext queryFileListContext = new QueryFileListContext();
        queryFileListContext.setUserId(context.getUserId());
        queryFileListContext.setDelFlag(DelFlagEnum.YES.getCode());
        return iUserFileService.getFileList(queryFileListContext);
    }

    /**
     * 1. check permission
     * 2. check restore condition
     * 3. restore
     * 4. publish event
     * @param context
     */
    @Override
    public void restore(RestoreContext context) {
        checkRestorePermission(context);
        checkRestoreFilename(context);
        doRestore(context);
        afterRestore(context);
    }

    /**
     * 1, check permission
     * 2. find all the file records
     * 3. delete
     * 4. publish event
     * @param context
     */
    @Override
    public void delete(DeleteContext context) {
        checkFileDeletePermission(context);
        findAllFileRecords(context);
        doDelete(context);
        afterDelete(context);
    }

    /**
     * pubish event
     * @param context
     */
    private void afterDelete(DeleteContext context) {
        FilePhysicalDeleteEvent event = new FilePhysicalDeleteEvent(context.getAllRecords());
        producer.sendMessage(DriveHarborChannels.PHYSICAL_DELETE_FILE_OUTPUT, event);

    }

    private void doDelete(DeleteContext context) {
        List<driveHarborUserFile> allRecords = context.getAllRecords();
        List<Long> fileIdList = allRecords.stream().map(driveHarborUserFile::getFileId).collect(Collectors.toList());
        if (!iUserFileService.removeByIds(fileIdList)) {
            throw new driveHarborBusinessException("Deletion failure");
        }

    }

    /**
     * recursively query all the file records
     * @param context
     */
    private void findAllFileRecords(DeleteContext context) {
        List<driveHarborUserFile> records = context.getRecords();
        List<driveHarborUserFile> allRecords = iUserFileService.findAllFileRecords(records);
        context.setAllRecords(allRecords);

    }

    private void checkFileDeletePermission(DeleteContext context) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("user_id", context.getUserId());
        queryWrapper.in("file_id", context.getFileIdList());
        List<driveHarborUserFile> records = iUserFileService.list(queryWrapper);
        if (CollectionUtils.isEmpty(records) || records.size() != context.getFileIdList().size()) {
            throw new driveHarborBusinessException("No permission");
        }
        context.setRecords(records);

    }
    /*********************private******************************/

    /**
     * publish restore event
     * @param context
     */
    private void afterRestore(RestoreContext context) {
        FileRestoreEvent event = new FileRestoreEvent(context.getFileIdList());
        producer.sendMessage(DriveHarborChannels.FILE_RESTORE_OUTPUT, event);

    }

    private void doRestore(RestoreContext context) {
        List<driveHarborUserFile> records = context.getRecords();
        records.stream().forEach(record -> {
            record.setDelFlag(DelFlagEnum.NO.getCode());
            record.setUpdateUser(context.getUserId());
            record.setUpdateTime(new Date());
        });
        boolean updateFlag = iUserFileService.updateBatchById(records);
        if (!updateFlag) {
            throw new driveHarborBusinessException("Restore failure");
        }

    }

    /**
     * 1. return restore failure if some files share the same names
     * 2. return restore failure if there is already a file with the same name in the path of the file to be restored
     * @param context
     */
    private void checkRestoreFilename(RestoreContext context) {
        List<driveHarborUserFile> records = context.getRecords();

        Set<String> filenameSet = records.stream().map(record -> record.getFileName() + driveHarborConstants.COMMON_SEPARATOR + record.getParentId()).collect(Collectors.toSet());
        if (filenameSet.size() != records.size()) {
            throw new driveHarborBusinessException("Repeated filenames exist. Please rename the files.");
        }

        for (driveHarborUserFile record : records) {
            QueryWrapper queryWrapper = Wrappers.query();
            queryWrapper.eq("user_id", context.getUserId());
            queryWrapper.eq("parent_id", record.getParentId());
            queryWrapper.eq("filename", record.getFileName());
            queryWrapper.eq("del_flag", DelFlagEnum.NO.getCode());
            if (iUserFileService.count(queryWrapper) > 0) {
                throw new driveHarborBusinessException("File: " + record.getFileName() + " restore failure. A file with the same same exists. Please rename the file.");
            }
        }
    }

    /**
     * check permissions
     * @param context
     */
    private void checkRestorePermission(RestoreContext context) {
        List<Long> fileIdList = context.getFileIdList();
        List<driveHarborUserFile> records = iUserFileService.listByIds(fileIdList);
        if (CollectionUtils.isEmpty(records)) {
            throw new driveHarborBusinessException("Restore failure");
        }
        Set<Long> userIdSet = records.stream().map(driveHarborUserFile::getUserId).collect(Collectors.toSet());
        if (userIdSet.size() > 1) {
            throw new driveHarborBusinessException("No permission");
        }

        if (!userIdSet.contains(context.getUserId())) {
            throw new driveHarborBusinessException("No permission");
        }
        context.setRecords(records);
    }

}
