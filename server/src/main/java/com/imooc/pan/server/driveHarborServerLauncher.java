package com.imooc.pan.server;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.response.R;
import io.swagger.annotations.Api;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@SpringBootApplication(scanBasePackages = driveHarborConstants.BASE_COMPONENT_SCAN_PATH)
@ServletComponentScan(basePackages = driveHarborConstants.BASE_COMPONENT_SCAN_PATH)
@EnableTransactionManagement
@MapperScan(basePackages = driveHarborConstants.BASE_COMPONENT_SCAN_PATH + ".server.modules.**.mapper")
@EnableAsync
public class driveHarborServerLauncher {
    public static void main(String[] args){
        SpringApplication.run(driveHarborServerLauncher.class);
    }


}
