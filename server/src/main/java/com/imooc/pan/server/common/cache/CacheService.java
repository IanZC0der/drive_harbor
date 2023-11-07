package com.imooc.pan.server.common.cache;

import java.io.Serializable;

/**
 * abstract cache service interface
 * @param <V>
 */
public interface CacheService<V> {


    /**
     * get entity by id
     */
    V getById(Serializable id);


    /**
     * update the cache by id
     *
     * @param id
     * @param entity
     * @return
     */
    boolean updateById(Serializable id, V entity);

    /**
     * remove the entity in the cache by id
     *
     * @param id
     * @return
     */
    boolean removeById(Serializable id);



}
