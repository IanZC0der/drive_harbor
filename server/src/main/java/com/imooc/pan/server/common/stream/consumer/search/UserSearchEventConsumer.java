package com.imooc.pan.server.common.stream.consumer.search;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.common.stream.event.search.UserSearchEvent;
import com.imooc.pan.server.common.stream.channel.DriveHarborChannels;
import com.imooc.pan.server.modules.user.entity.driveHarborUserSearchHistory;
import com.imooc.pan.server.modules.user.service.IUserSearchHistoryService;
import com.imooc.pan.stream.core.AbstractConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * user search event listener
 */
@Component
public class UserSearchEventConsumer extends AbstractConsumer {

    @Autowired
    private IUserSearchHistoryService iUserSearchHistoryService;

    /**
     * listen the search event and save it to the search history
     *
     * @param message
     */
    @StreamListener(DriveHarborChannels.USER_SEARCH_INPUT)
    public void saveSearchHistory(Message<UserSearchEvent> message) {
        if (isEmptyMessage(message)) {
            return;
        }
        printLog(message);
        UserSearchEvent event = message.getPayload();
        driveHarborUserSearchHistory record = new driveHarborUserSearchHistory();

        record.setId(IdUtil.get());
        record.setUserId(event.getUserId());
        record.setSearchContent(event.getKeyword());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());

        try {
            iUserSearchHistoryService.save(record);
        } catch (DuplicateKeyException e) {
            UpdateWrapper updateWrapper = Wrappers.update();
            updateWrapper.eq("user_id", event.getUserId());
            updateWrapper.eq("search_content", event.getKeyword());
            updateWrapper.set("update_time", new Date());
            iUserSearchHistoryService.update(updateWrapper);
        }

    }

}
