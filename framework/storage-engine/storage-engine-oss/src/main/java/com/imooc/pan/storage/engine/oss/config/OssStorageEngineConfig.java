package com.imooc.pan.storage.engine.oss.config;

import com.aliyun.oss.OSSClient;
import com.imooc.pan.core.exception.driveHarborFrameworkException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * OSS Engine config
 */
@Component
@Data
@ConfigurationProperties(prefix = "com.imooc.pan.storage.engine.oss")
public class OssStorageEngineConfig {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private Boolean autoCreateBucket = Boolean.TRUE;

    /**
     * create OSS client
     *
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public OSSClient ossClient() {
        if (StringUtils.isAnyBlank(getEndpoint(), getAccessKeyId(), getAccessKeySecret(), getBucketName())) {
            throw new driveHarborFrameworkException("the oss config is missed!");
        }
        return new OSSClient(getEndpoint(), getAccessKeyId(), getAccessKeySecret());
    }


}
