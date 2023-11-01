package com.imooc.pan.storage.engine.gcs.config;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.imooc.pan.core.exception.driveHarborFrameworkException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * GCS file engine config
 */
@Component
@Data
@ConfigurationProperties(prefix = "com.imooc.pan.storage.engine.gcs")
public class GCSStorageConfig {

    /**
     * end point of the bucket
     */
    private String projectId;
    private String credentialsPath;
    private String bucketName;

    @Bean
    public Storage gcsClient() throws IOException {
        if (StringUtils.isAnyBlank(projectId, credentialsPath, bucketName)) {
            throw new driveHarborFrameworkException("The GCS config is missed!");
        }

        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(Files.newInputStream(Paths.get(credentialsPath))))
                .build()
                .getService();

        return storage;
    }
}
