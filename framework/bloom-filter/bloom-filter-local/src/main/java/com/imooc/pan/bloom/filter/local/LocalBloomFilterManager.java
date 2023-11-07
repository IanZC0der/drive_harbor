package com.imooc.pan.bloom.filter.local;

import com.google.common.collect.Maps;
import com.imooc.pan.bloom.filter.core.BloomFilter;
import com.imooc.pan.bloom.filter.core.BloomFilterManager;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * local bloom filter manager
 */
@Component
public class LocalBloomFilterManager implements BloomFilterManager, InitializingBean {

    @Autowired
    private LocalBloomFilterConfig config;

    /**
     * map of bloom filters
     * concurrent
     */
    private final Map<String, BloomFilter> bloomFilterContainer = Maps.newConcurrentMap();

    /**
     * get the bloom filter by name
     *
     * @param name
     * @return
     */
    @Override
    public BloomFilter getFilter(String name) {
        return bloomFilterContainer.get(name);
    }

    /**
     * get filter names
     *
     * @return
     */
    @Override
    public Collection<String> getFilterNames() {
        return bloomFilterContainer.keySet();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<LocalBloomFilterConfigItem> items = config.getItems();
        if (CollectionUtils.isNotEmpty(items)) {
            items.stream().forEach(item -> {
                String funnelTypeName = item.getFunnelTypeName();
                try {
                    FunnelType funnelType = FunnelType.valueOf(funnelTypeName);
                    if (Objects.nonNull(funnelType)) {
                        bloomFilterContainer.putIfAbsent(item.getName(), new LocalBloomFilter(funnelType.getFunnel(), item.getExpectedInsertions(), item.getFpp()));
                    }
                } catch (Exception e) {

                }
            });
        }
    }

}
