package com.imooc.pan.bloom.filter.core;

/**
 * bloom filter top interface
 */
public interface BloomFilter<T> {

    /**
     * put
     *
     * @param object
     * @return
     */
    boolean put(T object);

    /**
     * check if the object is in the filter
     *
     * @param object
     * @return
     */
    boolean mightContain(T object);

    /**
     * clear filter
     */
    void clear();

}
