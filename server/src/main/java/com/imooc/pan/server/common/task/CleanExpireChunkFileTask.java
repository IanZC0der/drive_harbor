package com.imooc.pan.server.common.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.schedule.ScheduleTask;
import com.imooc.pan.server.common.event.log.ErrorLogEvent;
import com.imooc.pan.server.modules.file.entity.driveHarborFileChunk;
import com.imooc.pan.server.modules.file.service.IFileChunkService;
import com.imooc.pan.storage.engine.core.StorageEngine;
import com.imooc.pan.storage.engine.core.context.DeleteFileContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CleanExpireChunkFileTask implements ScheduleTask, ApplicationContextAware {
    private static final Long BATCH_SIZE = 500L;

    @Autowired
    private IFileChunkService iFileChunkService;

    @Autowired
    private StorageEngine storageEngine;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    /**
     * get task name
     *
     * @return
     */
    @Override
    public String getName() {
        return "CleanExpireChunkFileTask";
    }

    /**
     * 执行清理任务
     * <p>
     * 1. look up expired chunks
     * 2. delete chunks
     * 3. delete record
     * 4. continue with lookup for the chunks with the largest size
     */
    @Override
    public void run() {
        log.info("{} start clean expire chunk file...", getName());

        List<driveHarborFileChunk> expireFileChunkRecords;
        Long scrollPointer = 1L;

        do {
            expireFileChunkRecords = scrollQueryExpireFileChunkRecords(scrollPointer);
            if (CollectionUtils.isNotEmpty(expireFileChunkRecords)) {
                deleteRealChunkFiles(expireFileChunkRecords);
                List<Long> idList = deleteChunkFileRecords(expireFileChunkRecords);
                scrollPointer = Collections.max(idList);
            }
        } while (CollectionUtils.isNotEmpty(expireFileChunkRecords));

        log.info("{} finished clean expire chunk file...", getName());
    }

    /*********************************************private*********************************************/

    /**
     * query expired file chunks records
     *
     * @param scrollPointer
     * @return
     */
    private List<driveHarborFileChunk> scrollQueryExpireFileChunkRecords(Long scrollPointer) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.le("expiration_time", new Date());
        queryWrapper.ge("id", scrollPointer);
        queryWrapper.last(" limit " + BATCH_SIZE);
        return iFileChunkService.list(queryWrapper);
    }

    /**
     * delete file chunk records
     *
     * @param expireFileChunkRecords
     */
    private void deleteRealChunkFiles(List<driveHarborFileChunk> expireFileChunkRecords) {
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<String> realPaths = expireFileChunkRecords.stream().map(driveHarborFileChunk::getRealPath).collect(Collectors.toList());
        deleteFileContext.setRealFilePathList(realPaths);
        try {
            storageEngine.delete(deleteFileContext);
        } catch (IOException e) {
            saveErrorLog(realPaths);
        }
    }

    /**
     * @param realPaths
     */
    private void saveErrorLog(List<String> realPaths) {
        ErrorLogEvent event = new ErrorLogEvent(this, "File deletation failture. Please manully remove the file chunks. The path is：" + JSON.toJSONString(realPaths), driveHarborConstants.ZERO_LONG);
        applicationContext.publishEvent(event);
    }

    /**
     * 删除过期文件分片记录
     *
     * @param expireFileChunkRecords
     * @return
     */
    private List<Long> deleteChunkFileRecords(List<driveHarborFileChunk> expireFileChunkRecords) {
        List<Long> idList = expireFileChunkRecords.stream().map(driveHarborFileChunk::getId).collect(Collectors.toList());
        iFileChunkService.removeByIds(idList);
        return idList;
    }


}
