package com.imooc.pan.storage.engine.oss.initializer;

import com.aliyun.oss.OSSClient;
import com.imooc.pan.core.exception.driveHarborFrameworkException;
import com.imooc.pan.storage.engine.oss.config.OssStorageEngineConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * oss bucket initializer
 */

@Component
@Slf4j
public class OssBucketInitializer implements CommandLineRunner {
    @Autowired
    private OssStorageEngineConfig config;
    @Autowired
    private OSSClient client;
    public void run(String... args) throws Exception {

        boolean bucketExist = client.doesBucketExist(config.getBucketName());
        if(!bucketExist && config.getAutoCreateBucket()){
            client.createBucket(config.getBucketName());

        }
        if(!bucketExist && !config.getAutoCreateBucket()){
            throw new driveHarborFrameworkException("the bucket " +config.getBucketName()+" is not available.");
        }

        log.info("the bucket " +config.getBucketName()+" has been created.");
    }
}
