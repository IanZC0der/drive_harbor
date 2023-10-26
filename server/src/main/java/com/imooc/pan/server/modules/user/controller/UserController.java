package com.imooc.pan.server.modules.user.controller;

import com.imooc.pan.core.response.R;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.common.annotation.LoginIgnore;
import com.imooc.pan.server.common.utils.UserIdUtil;
import com.imooc.pan.server.modules.user.context.*;
import com.imooc.pan.server.modules.user.converter.UserConverter;
import com.imooc.pan.server.modules.user.po.*;
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
    @LoginIgnore
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
    @LoginIgnore
    @PostMapping("login")
    public R login(@Validated @RequestBody UserLoginPO userLoginPO) {
        UserLoginContext userLoginContext = userConverter.userLoginPO2UserLoginContext(userLoginPO);
        String accessToken = iUserService.login(userLoginContext);
        return R.data(accessToken);
    }

    @ApiOperation(
            value = "user logout interface",
            notes = "this interface provides logout functionality",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("exit")
    public R exit() {
        iUserService.exit(UserIdUtil.get());
        return R.success();
    }

    @ApiOperation(
            value = "User forgot the password. Verify the user name",
            notes = "this interface verifies the username while user forgets the password and requests to change the password",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @LoginIgnore
    @PostMapping("username/check")
    public R checkUsername(@Validated @RequestBody CheckUsernamePO checkUsernamePO) {
        CheckUsernameContext checkUsernameContext = userConverter.checkUsernamePO2CheckUsernameContext(checkUsernamePO);
        String question = iUserService.checkUsername(checkUsernameContext);
        return R.data(question);
    }
    @ApiOperation(
            value = "User forgot the password. Verify the answer to the security question",
            notes = "this interface verifies the answer the user puts",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @LoginIgnore
    @PostMapping("answer/check")
    public R checkAnswer(@Validated @RequestBody CheckAnswerPO checkAnswerPO) {
        CheckAnswerContext checkAnswerContext = userConverter.checkAnswerPO2CheckAnswerContext(checkAnswerPO);
        String token = iUserService.checkAnswer(checkAnswerContext);
        return R.data(token);
    }

    @ApiOperation(
            value = "User forgot the password. Reset the password",
            notes = "this interface provides the functionality of resetting the password",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("password/reset")
    @LoginIgnore
    public R resetPassword(@Validated @RequestBody ResetPasswordPO resetPasswordPO) {
        ResetPasswordContext resetPasswordContext = userConverter.resetPasswordPO2ResetPasswordContext(resetPasswordPO);
        iUserService.resetPassword(resetPasswordContext);
        return R.success();
    }

    @ApiOperation(
            value = "user updates the password",
            notes = "this interface provides the functionality of updating the password",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("password/change")
    public R changePassword(@Validated @RequestBody ChangePasswordPO changePasswordPO) {
        ChangePasswordContext changePasswordContext = userConverter.changePasswordPO2ChangePasswordContext(changePasswordPO);
        changePasswordContext.setUserId(UserIdUtil.get());
        iUserService.changePassword(changePasswordContext);
        return R.success();
    }

}
