package com.imooc.pan.server.common.stream.consumer.log;

import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.common.stream.event.log.ErrorLogEvent;
import com.imooc.pan.server.common.stream.channel.DriveHarborChannels;
import com.imooc.pan.server.modules.log.entity.driveHarborErrorLog;
import com.imooc.pan.server.modules.log.service.IErrorLogService;
import com.imooc.pan.stream.core.AbstractConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * error log event listener
 */
@Component
public class ErrorLogEventConsumer extends AbstractConsumer {

    @Autowired
    private IErrorLogService iErrorLogService;

    /**
     * listen the error and save it to the database
     *
     * @param message
     */
    @StreamListener(DriveHarborChannels.ERROR_LOG_INPUT)
    public void saveErrorLog(Message<ErrorLogEvent> message) {
        if (isEmptyMessage(message)) {
            return;
        }
        printLog(message);
        ErrorLogEvent event = message.getPayload();
        driveHarborErrorLog record = new driveHarborErrorLog();
        record.setId(IdUtil.get());
        record.setLogContent(event.getErrorMsg());
        record.setLogStatus(0);
        record.setCreateUser(event.getUserId());
        record.setCreateTime(new Date());
        record.setUpdateUser(event.getUserId());
        record.setUpdateTime(new Date());
        iErrorLogService.save(record);
    }

}
