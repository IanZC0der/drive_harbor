package com.imooc.pan.server;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.response.R;
import io.swagger.annotations.Api;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@SpringBootApplication(scanBasePackages = driveHarborConstants.BASE_COMPONENT_SCAN_PATH)
@ServletComponentScan(basePackages = driveHarborConstants.BASE_COMPONENT_SCAN_PATH)
@RestController
@Api("Test Swagger2 Interface Class")
@Validated
@EnableTransactionManagement
@MapperScan(basePackages = driveHarborConstants.BASE_COMPONENT_SCAN_PATH + ".server.modules.**.mapper")
public class driveHarborServerLauncher {
    public static void main(String[] args){
        SpringApplication.run(driveHarborServerLauncher.class);
    }

    @GetMapping("hello")
    public R<String> hello(@NotBlank(message = "name cannot be blank") String name) {
        System.out.println(Thread.currentThread().getContextClassLoader());
        return R.success("hello" + name + "! changed again again");

    }
}
