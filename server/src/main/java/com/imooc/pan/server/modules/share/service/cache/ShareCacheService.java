package com.imooc.pan.server.modules.share.service.cache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pan.server.common.cache.AbstractManualCacheService;
import com.imooc.pan.server.modules.share.entity.driveHarborShare;
import com.imooc.pan.server.modules.share.mapper.driveHarborShareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * manual cache service applied to share service
 */
@Component(value = "shareManualCacheService")
public class ShareCacheService extends AbstractManualCacheService<driveHarborShare> {

    @Autowired
    private driveHarborShareMapper mapper;

    @Override
    protected BaseMapper<driveHarborShare> getBaseMapper() {
        return mapper;
    }

    /**
     * get cache key format
     *
     * @return
     */
    @Override
    public String getKeyFormat() {
        return "SHARE:ID:%s";
    }

}
