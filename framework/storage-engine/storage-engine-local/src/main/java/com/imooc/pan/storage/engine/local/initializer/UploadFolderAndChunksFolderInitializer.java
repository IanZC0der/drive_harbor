package com.imooc.pan.storage.engine.local.initializer;

import com.imooc.pan.storage.engine.local.config.LocalStorageEngineConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * upload folder and chunks folder initializer
 */

@Component
@Slf4j
public class UploadFolderAndChunksFolderInitializer implements CommandLineRunner {
    @Autowired
    private LocalStorageEngineConfig config;

    @Override
    public void run(String... args) throws Exception {
        FileUtils.forceMkdir(new File(config.getRootFilePath()));
        log.info("the root file path has been created successfully!");
        FileUtils.forceMkdir(new File(config.getRootFileChunkPath()));
        log.info("the root file chunk path has been created successfully!");
    }
}
