package com.imooc.pan.schedule.test.task;

import com.imooc.pan.schedule.ScheduleTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SimpleScheduleTask implements ScheduleTask {

    @Override
    public String getName() {
        return "testimg schedule task";
    }

    @Override
    public void run() {
        log.info(getName() + " is being executed...");
    }
}
