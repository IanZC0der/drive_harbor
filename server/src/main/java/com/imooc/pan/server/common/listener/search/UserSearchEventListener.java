package com.imooc.pan.server.common.listener.search;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.common.event.event.UserSearchEvent;
import com.imooc.pan.server.modules.user.entity.driveHarborUserSearchHistory;
import com.imooc.pan.server.modules.user.service.IUserSearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * user search event listener
 */
@Component
public class UserSearchEventListener {

    @Autowired
    private IUserSearchHistoryService iUserSearchHistoryService;

    /**
     * listen the search event and save it to the search history
     *
     * @param event
     */
    @EventListener(classes = UserSearchEvent.class)
    @Async(value = "eventListenerTaskExecutor")
    public void saveSearchHistory(UserSearchEvent event) {
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
