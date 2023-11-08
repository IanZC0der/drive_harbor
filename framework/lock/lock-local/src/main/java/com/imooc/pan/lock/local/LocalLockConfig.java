package com.imooc.pan.lock.local;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

/**
 * local lock config
 */
@SpringBootConfiguration
@Slf4j
public class LocalLockConfig {

    /**
     * local lock registry config
     *
     * @return
     */
    @Bean
    public LockRegistry localLockRegistry() {
        LockRegistry lockRegistry = new DefaultLockRegistry();
        log.info("local lock loaded successfully!");
        return lockRegistry;
    }

}
