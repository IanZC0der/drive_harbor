package com.imooc.pan.bloom.filter.local;

import lombok.Data;

/**
 * local bloom filter config item
 */
@Data
public class LocalBloomFilterConfigItem {

    /**
     * bloom filter name
     */
    private String name;

    /**
     * funnel name
     */
    private String funnelTypeName = FunnelType.LONG.name();

    /**
     * expected insertions
     */
    private long expectedInsertions = 1000000L;

    /**
     * FPP
     */
    private double fpp = 0.01D;

}
