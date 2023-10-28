package com.imooc.pan.storage.engine.local.config;

import com.imooc.pan.core.utils.FileUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.imooc.pan.storage.engine.local")
@Data
public class LocalStorageEngineConfig {

    /**
     * real file path
     */
    private String rootFilePath = FileUtil.generateDefaultStoreFileRealPath();

    /**
     * chunk file path
     */
    private String rootFileChunkPath = FileUtil.generateDefaultStoreFileChunkRealPath();

}
