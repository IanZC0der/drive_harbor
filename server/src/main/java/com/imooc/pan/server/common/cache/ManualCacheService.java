package com.imooc.pan.server.common.cache;

import org.springframework.cache.Cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * manual cache service interface
 *
 * @param <V>
 */
public interface ManualCacheService<V> extends CacheService<V> {

    /**
     * get list of entities by ids
     *
     * @param ids
     * @return
     */
    List<V> getByIds(Collection<? extends Serializable> ids);

    /**
     * update the entities in the cache by ids
     *
     * @param entityMap
     * @return
     */
    boolean updateByIds(Map<? extends Serializable, V> entityMap);

    /**
     * remove the entities by ids
     *
     * @param ids
     * @return
     */
    boolean removeByIds(Collection<? extends Serializable> ids);

    /**
     * get cache key format
     *
     * @return
     */
    String getKeyFormat();

    /**
     * get cache
     *
     * @return
     */
    Cache getCache();

}
