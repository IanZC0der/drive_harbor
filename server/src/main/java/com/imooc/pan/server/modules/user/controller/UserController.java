package com.imooc.pan.server.modules.user.controller;

import com.imooc.pan.core.response.R;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.modules.user.context.UserLoginContext;
import com.imooc.pan.server.modules.user.context.UserRegisterContext;
import com.imooc.pan.server.modules.user.converter.UserConverter;
import com.imooc.pan.server.modules.user.po.UserLoginPO;
import com.imooc.pan.server.modules.user.po.UserRegisterPO;
import com.imooc.pan.server.modules.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.restart.ConditionalOnInitializedRestarter;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("user")
@Api(tags = "user module")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private UserConverter userConverter;
    @ApiOperation(
            value = "user registration interface",
            notes = "this interface provides registration functionality with idempotence",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("register")
    public R register(@Validated @RequestBody UserRegisterPO userRegisterPO) {
        UserRegisterContext userRegisterContext = userConverter.userRegisterPO2UserRegisterContext(userRegisterPO);
        Long userId = iUserService.register(userRegisterContext);
        return R.data(IdUtil.encrypt(userId));
    }
    @ApiOperation(
            value = "user login interface",
            notes = "this interface provides login functionality. provides accessToken after successful login",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("login")
    public R login(@Validated @RequestBody UserLoginPO userLoginPO) {
        UserLoginContext userLoginContext = userConverter.userLoginPO2UserLoginContext(userLoginPO);
        String accessToken = iUserService.login(userLoginContext);
        return R.data(accessToken);
    }
}
