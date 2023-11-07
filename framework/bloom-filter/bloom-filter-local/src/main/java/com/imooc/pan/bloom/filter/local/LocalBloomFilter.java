package com.imooc.pan.bloom.filter.local;

import com.google.common.hash.Funnel;
import com.imooc.pan.bloom.filter.core.BloomFilter;

/**
 * local bloom filter implementation
 *
 * @param <T>
 */
public class LocalBloomFilter<T> implements BloomFilter<T> {

    // delegate to com.google.common.hash.BloomFilter
    private com.google.common.hash.BloomFilter delegate;

    /**
     * funnel
     */
    private Funnel funnel;

    /**
     * expected insertions
     */
    private long expectedInsertions;

    /**
     * fpp
     */
    private double fpp;

    public LocalBloomFilter(Funnel funnel, long expectedInsertions, double fpp) {
        this.funnel = funnel;
        this.expectedInsertions = expectedInsertions;
        this.fpp = fpp;
        this.delegate = com.google.common.hash.BloomFilter.create(funnel, expectedInsertions, fpp);
    }

    /**
     * put object
     *
     * @param object
     * @return
     */
    @Override
    public boolean put(T object) {
        return delegate.put(object);
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean mightContain(T object) {
        return delegate.mightContain(object);
    }

    @Override
    public void clear() {
        this.delegate = com.google.common.hash.BloomFilter.create(funnel, expectedInsertions, fpp);
    }

}
