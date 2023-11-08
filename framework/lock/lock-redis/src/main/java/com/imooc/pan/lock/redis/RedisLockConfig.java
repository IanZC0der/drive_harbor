package com.imooc.pan.lock.redis;

import com.imooc.pan.lock.core.LockConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

/**
 * distributed lock based on redis
 * integrating spring-data-redis
 */
@SpringBootConfiguration
@Slf4j
public class RedisLockConfig {

    @Bean
    public LockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        RedisLockRegistry lockRegistry = new RedisLockRegistry(redisConnectionFactory, LockConstants.DRIVE_HARBOR_LOCK);
        log.info("redis lock loaded successfully!");
        return lockRegistry;
    }

}
