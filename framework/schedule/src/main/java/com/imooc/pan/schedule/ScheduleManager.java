package com.imooc.pan.schedule;

import com.imooc.pan.core.exception.driveHarborFrameworkException;
import com.imooc.pan.core.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Schedule manager
 * 1. create and start a task
 * 2. stop a task
 * 3. update a task
 */
@Component
@Slf4j
public class ScheduleManager {
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    //tasks cache
    private Map<String, Object> cache = new ConcurrentHashMap<>();

    /**
     * starte a task and put it to the cache
     * @param scheduleTask
     * @param cron
     * @return
     */
    public String startTask(ScheduleTask scheduleTask, String cron) {
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(scheduleTask, new CronTrigger(cron));
        String key= UUIDUtil.getUUID();
        ScheduleTaskHolder holder = new ScheduleTaskHolder(scheduleTask, scheduledFuture);
        cache.put(key, holder);
        log.info("{} started successfully, the identifier is {}", scheduleTask.getName(), key);
        return key;
    }

    public void stopTask(String key) {
        if(StringUtils.isBlank(key)){
            return;
        }
        ScheduleTaskHolder holder = (ScheduleTaskHolder) cache.get(key);
        if(Objects.isNull(holder)){
            return;
        }

        ScheduledFuture scheduledFuture = holder.getScheduledFuture();
        boolean cancel = scheduledFuture.cancel(true);
        if(cancel){
            log.info("{} stopped successfully, identifier {}", holder.getScheduleTask().getName(), key);
        } else {
            log.error("{} failed to stop, identifier{}", holder.getScheduleTask().getName(), key);
        }

    }

    public String changeTask(String key, String cron){
        if(StringUtils.isAnyBlank(key, cron)){
            throw new driveHarborFrameworkException("key/cron cannot be null.");
        }
        ScheduleTaskHolder holder = (ScheduleTaskHolder) cache.get(key);
        if(Objects.isNull(holder)){
            throw new driveHarborFrameworkException(key + " identifier doesn't exist.");
        }
        stopTask(key);
        return startTask(holder.getScheduleTask(), cron);

    }

}
