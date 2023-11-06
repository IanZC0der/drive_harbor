package com.imooc.pan.server.common.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * thread pool config
 */
@SpringBootConfiguration
public class TreadPoolConfig {

    @Bean(name = "eventListenerTaskExecutor")
    public ThreadPoolTaskExecutor eventListenerTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // core pool size
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setKeepAliveSeconds(200);
        // queue
        taskExecutor.setQueueCapacity(2048);
        taskExecutor.setThreadNamePrefix("event-listener-thread");
        // when the max capacity is attained, calls runs the thread
        // we can also abort or discard the oldest one in the queue and append this new one
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return taskExecutor;
    }


}
