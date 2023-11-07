package com.imooc.pan.bloom.filter.local;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "com.imooc.pan.bloom.filter.local")
@Data
public class LocalBloomFilterConfig {

    // every module can have a bloom filter
    private List<LocalBloomFilterConfigItem> items;

}
