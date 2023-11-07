package com.imooc.pan.server.modules.user.service.cache;

import com.imooc.pan.cache.core.constants.CacheConstants;
import com.imooc.pan.server.common.cache.AnnotationCacheService;
import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import com.imooc.pan.server.modules.user.mapper.driveHarborUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * cache service for user module
 */
@Component(value = "userAnnotationCacheService")
public class UserCacheService implements AnnotationCacheService<driveHarborUser> {

    @Autowired
    private driveHarborUserMapper mapper;

    /**
     *
     * @param id
     * @return
     */
    @Cacheable(cacheNames = CacheConstants.DRIVE_HARBOR_CACHE_NAME, keyGenerator = "userIdKeyGenerator", sync = true)
    @Override
    public driveHarborUser getById(Serializable id) {
        return mapper.selectById(id);
    }

    /**
     *
     * @param id
     * @param entity
     * @return
     */
    @CacheEvict(cacheNames = CacheConstants.DRIVE_HARBOR_CACHE_NAME, keyGenerator = "userIdKeyGenerator")
    @Override
    public boolean updateById(Serializable id,driveHarborUser entity) {
        return mapper.updateById(entity) == 1;
    }

    /**
     *
     * @param id
     * @return
     */
    @CacheEvict(cacheNames = CacheConstants.DRIVE_HARBOR_CACHE_NAME, keyGenerator = "userIdKeyGenerator")
    @Override
    public boolean removeById(Serializable id) {
        return mapper.deleteById(id) == 1;
    }

}
