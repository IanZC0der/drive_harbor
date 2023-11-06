package com.imooc.pan.server.modules.test.controller;

import com.imooc.pan.core.response.R;
import com.imooc.pan.server.common.annotation.LoginIgnore;
import com.imooc.pan.server.common.event.test.TestEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test async events controller
 */
@RestController
public class TestController implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * test publish event
     *
     * @return
     */
    @GetMapping("test")
    @LoginIgnore
    public R test() {
        applicationContext.publishEvent(new TestEvent(this, "test"));
        return R.success();
    }

}
