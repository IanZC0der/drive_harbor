package com.imooc.pan.swagger2;

import lombok.Data;
import com.imooc.pan.core.constants.driveHarborConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Swagger2 configuration entities
 */
@Data
@Component
@ConfigurationProperties(prefix = "swagger2")
public class Swagger2ConfigProperties {
    private boolean show = true;

    private String groupName = "r-pan";

    private String basePackage = driveHarborConstants.BASE_COMPONENT_SCAN_PATH;

    private String title = "r-pan-server";

    private String description = "r-pan-server";

    private String termsOfServiceUrl = "http://127.0.0.1:${server.port}";

    private String contactName = "Ian";

    private String contactUrl = "https://github.com/IanZC0der";

    private String contactEmail = "ianzhangbc@outlook.com";

    private String version = "1.0";
}
