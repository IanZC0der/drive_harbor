package com.imooc.pan.server.common.stream.consumer.share;

import com.imooc.pan.server.common.stream.channel.DriveHarborChannels;
import com.imooc.pan.server.common.stream.event.file.DeleteFileEvent;
import com.imooc.pan.server.common.stream.event.file.FileRestoreEvent;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.imooc.pan.server.modules.file.enums.DelFlagEnum;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.server.modules.share.service.IShareService;
import com.imooc.pan.stream.core.AbstractConsumer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * share status change listener
 */
@Component
public class ShareStatusChangeConsumer extends AbstractConsumer {

    @Autowired
    private IUserFileService iUserFileService;

    @Autowired
    private IShareService iShareService;

    /**
     * refresh the share status after the file is deleted
     *
     * @param message
     */
    @StreamListener(DriveHarborChannels.DELETE_FILE_INPUT)
    public void changeShare2FileDeleted(Message<DeleteFileEvent> message) {
        if (isEmptyMessage(message)) {
            return;
        }
        printLog(message);
        DeleteFileEvent event = message.getPayload();
        List<Long> fileIdList = event.getFileIdList();
        if (CollectionUtils.isEmpty(fileIdList)) {
            return;
        }
        List<driveHarborUserFile> allRecords = iUserFileService.findAllFileRecordsByFileIdList(fileIdList);
        List<Long> allAvailableFileIdList = allRecords.stream()
                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
                .map(driveHarborUserFile::getFileId)
                .collect(Collectors.toList());
        allAvailableFileIdList.addAll(fileIdList);
        iShareService.refreshShareStatus(allAvailableFileIdList);
    }

    /**
     * refresh the share status after the deleted file is recovered
     *
     * @param message
     */
    @StreamListener(DriveHarborChannels.FILE_RESTORE_INPUT)
    public void changeShare2Normal(Message<FileRestoreEvent> message) {
        if (isEmptyMessage(message)) {
            return;
        }
        printLog(message);
        FileRestoreEvent event = message.getPayload();
        List<Long> fileIdList = event.getFileIdList();
        if (CollectionUtils.isEmpty(fileIdList)) {
            return;
        }
        List<driveHarborUserFile> allRecords = iUserFileService.findAllFileRecordsByFileIdList(fileIdList);
        List<Long> allAvailableFileIdList = allRecords.stream()
                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
                .map(driveHarborUserFile::getFileId)
                .collect(Collectors.toList());
        allAvailableFileIdList.addAll(fileIdList);
        iShareService.refreshShareStatus(allAvailableFileIdList);
    }

}
