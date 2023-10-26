package com.imooc.pan.server.modules.user;

import cn.hutool.core.lang.Assert;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.core.utils.JwtUtil;
import com.imooc.pan.server.driveHarborServerLauncher;
import com.imooc.pan.server.modules.user.constants.UserConstants;
import com.imooc.pan.server.modules.user.context.CheckAnswerContext;
import com.imooc.pan.server.modules.user.context.CheckUsernameContext;
import com.imooc.pan.server.modules.user.context.UserLoginContext;
import com.imooc.pan.server.modules.user.context.UserRegisterContext;
import com.imooc.pan.server.modules.user.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;

/**
 * user module unit test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = driveHarborServerLauncher.class)
@Transactional
public class UserTest {

    @Autowired
    private IUserService iUserService;

    @Test
    public void testRegisterUsr(){
        UserRegisterContext userRegisterContext = createUserRegisterContext();
        Long register = iUserService.register(userRegisterContext);
        Assert.isTrue(register.longValue() > 0L);
    }

    private UserRegisterContext createUserRegisterContext() {
        UserRegisterContext userRegisterContext = new UserRegisterContext();
        userRegisterContext.setUsername(USERNAME);
        userRegisterContext.setPassword(PASSWORD);
        userRegisterContext.setQuestion(QUESTION);
        userRegisterContext.setAnswer(ANSWER);
        return userRegisterContext;
    }

    @Test(expected = driveHarborBusinessException.class)
    public void testRegisterDuplicateUserName(){
        UserRegisterContext userRegisterContext = createUserRegisterContext();
        Long register = iUserService.register(userRegisterContext);
        Assert.isTrue(register.longValue() > 0L);
        iUserService.register(userRegisterContext);

    }

    /**
     * test login success
     */
    @Test
    public void loginSuccess() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = iUserService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        UserLoginContext userLoginContext = createUserLoginContext();

        String accessToken = iUserService.login(userLoginContext);

        Assert.isTrue(StringUtils.isNotBlank(accessToken));
    }

    private UserLoginContext createUserLoginContext() {
        UserLoginContext userLoginContext = new UserLoginContext();
        userLoginContext.setUsername(USERNAME);
        userLoginContext.setPassword(PASSWORD);
        return userLoginContext;
    }

    /**
     * username incorrect, login fails
     */
    @Test(expected = driveHarborBusinessException.class)
    public void wrongUsername() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = iUserService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        UserLoginContext userLoginContext = createUserLoginContext();
        userLoginContext.setUsername(userLoginContext.getUsername() + "_change");
        iUserService.login(userLoginContext);
    }

    /**
     * incorrect password, login fails
     */
    @Test(expected = driveHarborBusinessException.class)
    public void wrongPassword() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = iUserService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        UserLoginContext userLoginContext = createUserLoginContext();
        userLoginContext.setPassword(userLoginContext.getPassword() + "_change");
        iUserService.login(userLoginContext);
    }

    /**
     * logout test
     */
    @Test
    public void exitSuccess() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = iUserService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        UserLoginContext userLoginContext = createUserLoginContext();
        String accessToken = iUserService.login(userLoginContext);

        Assert.isTrue(StringUtils.isNotBlank(accessToken));

        Long userId = (Long) JwtUtil.analyzeToken(accessToken, UserConstants.LOGIN_USER_ID);

        iUserService.exit(userId);
    }

    /**
     * Verify correct username
     */
    @Test
    public void checkUsernameSuccess() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = iUserService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckUsernameContext checkUsernameContext = new CheckUsernameContext();
        checkUsernameContext.setUsername(USERNAME);
        String question = iUserService.checkUsername(checkUsernameContext);
        Assert.isTrue(StringUtils.isNotBlank(question));
    }

    /**
     * username doesn't exist
     */
    @Test(expected = driveHarborBusinessException.class)
    public void checkUsernameNotExist() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = iUserService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckUsernameContext checkUsernameContext = new CheckUsernameContext();
        checkUsernameContext.setUsername(USERNAME + "_change");
        iUserService.checkUsername(checkUsernameContext);
    }


    /**
     * pass answer check
     */
    @Test
    public void checkAnswerSuccess() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = iUserService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckAnswerContext checkAnswerContext = new CheckAnswerContext();
        checkAnswerContext.setUsername(USERNAME);
        checkAnswerContext.setQuestion(QUESTION);
        checkAnswerContext.setAnswer(ANSWER);

        String token = iUserService.checkAnswer(checkAnswerContext);

        Assert.isTrue(StringUtils.isNotBlank(token));
    }

    /**
     * check answer failure
     */
    @Test(expected = driveHarborBusinessException.class)
    public void checkAnswerFail() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = iUserService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckAnswerContext checkAnswerContext = new CheckAnswerContext();
        checkAnswerContext.setUsername(USERNAME);
        checkAnswerContext.setQuestion(QUESTION);
        checkAnswerContext.setAnswer(ANSWER + "_change");

        iUserService.checkAnswer(checkAnswerContext);
    }


    private final static String USERNAME = "ian";
    private final static String PASSWORD = "123456789";
    private final static String QUESTION = "question";
    private final static String ANSWER = "answer";



}
