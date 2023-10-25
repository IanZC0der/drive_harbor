package com.imooc.pan.cache.redis.test.instance;

import com.imooc.pan.cache.core.constants.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheAnnotationTester {
    @Cacheable(cacheNames = CacheConstants.DRIVE_HARBOR_CACHE_NAME, key = "#name", sync = true)
    public String testCacheable(String name) {
        log.info("call com.imooc.pan.cache.caffeine.test.instance.CacheAnnotationTester.testCacheable, param is {}", name);
        return new StringBuilder("hello ").append(name).toString();
    }
}
