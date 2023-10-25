package com.imooc.pan.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.ScheduledFuture;

/**
 * cached tasks
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTaskHolder implements Serializable {
    //task
    private ScheduleTask scheduleTask;
    //result of the executed task
    private ScheduledFuture scheduledFuture;

}
