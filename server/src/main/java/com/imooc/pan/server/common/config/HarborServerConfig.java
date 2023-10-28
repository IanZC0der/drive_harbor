package com.imooc.pan.server.common.config;

import com.imooc.pan.core.constants.driveHarborConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.imooc.pan.server")
@Data
public class HarborServerConfig {

    /**
     * chunk file expiration days.
     * chunk will be expired.
     */
    private Integer chunkFileExpirationDays = driveHarborConstants.ONE_INT;

    /**
     * share link prefix
     */
    private String sharePrefix = "http://127.0.0.1:8080/share/";

}
