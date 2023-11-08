//package com.imooc.pan.server.common.listener.share;
//
//import com.imooc.pan.server.common.event.file.DeleteFileEvent;
//import com.imooc.pan.server.common.event.file.FileRestoreEvent;
//import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
//import com.imooc.pan.server.modules.file.enums.DelFlagEnum;
//import com.imooc.pan.server.modules.file.service.IUserFileService;
//import com.imooc.pan.server.modules.share.service.IShareService;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * share status change listener
// */
//@Component
//public class ShareStatusChangeListener {
//
//    @Autowired
//    private IUserFileService iUserFileService;
//
//    @Autowired
//    private IShareService iShareService;
//
//    /**
//     * refresh the share status after the file is deleted
//     *
//     * @param event
//     */
//    @EventListener(DeleteFileEvent.class)
//    @Async(value = "eventListenerTaskExecutor")
//    public void changeShare2FileDeleted(DeleteFileEvent event) {
//        List<Long> fileIdList = event.getFileIdList();
//        if (CollectionUtils.isEmpty(fileIdList)) {
//            return;
//        }
//        List<driveHarborUserFile> allRecords = iUserFileService.findAllFileRecordsByFileIdList(fileIdList);
//        List<Long> allAvailableFileIdList = allRecords.stream()
//                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
//                .map(driveHarborUserFile::getFileId)
//                .collect(Collectors.toList());
//        allAvailableFileIdList.addAll(fileIdList);
//        iShareService.refreshShareStatus(allAvailableFileIdList);
//    }
//
//    /**
//     * refresh the share status after the deleted file is recovered
//     *
//     * @param event
//     */
//    @EventListener(FileRestoreEvent.class)
//    @Async(value = "eventListenerTaskExecutor")
//    public void changeShare2Normal(FileRestoreEvent event) {
//        List<Long> fileIdList = event.getFileIdList();
//        if (CollectionUtils.isEmpty(fileIdList)) {
//            return;
//        }
//        List<driveHarborUserFile> allRecords = iUserFileService.findAllFileRecordsByFileIdList(fileIdList);
//        List<Long> allAvailableFileIdList = allRecords.stream()
//                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
//                .map(driveHarborUserFile::getFileId)
//                .collect(Collectors.toList());
//        allAvailableFileIdList.addAll(fileIdList);
//        iShareService.refreshShareStatus(allAvailableFileIdList);
//    }
//
//}
