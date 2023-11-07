package com.imooc.pan.bloom.filter.core;

import java.util.Collection;

/**
 * bloom filter manager interface
 */
public interface BloomFilterManager {

    /**
     * get tthe filter by name
     *
     * @param name
     * @return
     */
    BloomFilter getFilter(String name);

    /**
     * get filter names in the current manager
     *
     * @return
     */
    Collection<String> getFilterNames();

}
