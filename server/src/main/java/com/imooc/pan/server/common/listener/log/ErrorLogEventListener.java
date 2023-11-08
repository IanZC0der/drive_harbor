//package com.imooc.pan.server.common.listener.log;
//
//import com.imooc.pan.core.utils.IdUtil;
//import com.imooc.pan.server.common.event.log.ErrorLogEvent;
//import com.imooc.pan.server.modules.log.entity.driveHarborErrorLog;
//import com.imooc.pan.server.modules.log.service.IErrorLogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * error log event listener
// */
//@Component
//public class ErrorLogEventListener {
//
//    @Autowired
//    private IErrorLogService iErrorLogService;
//
//    /**
//     * listen the error and save it to the database
//     *
//     * @param event
//     */
//    @EventListener(ErrorLogEvent.class)
//    @Async(value = "eventListenerTaskExecutor")
//    public void saveErrorLog(ErrorLogEvent event) {
//        driveHarborErrorLog record = new driveHarborErrorLog();
//        record.setId(IdUtil.get());
//        record.setLogContent(event.getErrorMsg());
//        record.setLogStatus(0);
//        record.setCreateUser(event.getUserId());
//        record.setCreateTime(new Date());
//        record.setUpdateUser(event.getUserId());
//        record.setUpdateTime(new Date());
//        iErrorLogService.save(record);
//    }
//
//}
