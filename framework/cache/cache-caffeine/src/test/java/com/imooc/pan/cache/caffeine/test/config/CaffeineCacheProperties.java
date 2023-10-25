package com.imooc.pan.cache.caffeine.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Caffeine Cache Configurations Properties
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.imooc.pan.cache.caffeine")
public class CaffeineCacheProperties {

    /**
     * initial cache capacity
     * com.imooc.pan.cache.caffeine.init-cache-capacity
     */
    private Integer initCacheCapacity = 256;

    /**
     * max cache capacity. For oversized ones, release the cache based on LRU policy
     * com.imooc.pan.cache.caffeine.max-cache-capacity
     */
    private Long maxCacheCapacity = 10000L;

    /**
     * allow NULL as cache value
     * com.imooc.pan.cache.caffeine.allow-null-value
     */
    private Boolean allowNullValue = Boolean.TRUE;

}