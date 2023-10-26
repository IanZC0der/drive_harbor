package com.imooc.pan.server.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pan.cache.core.constants.CacheConstants;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.core.response.ResponseCode;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.core.utils.JwtUtil;
import com.imooc.pan.core.utils.PasswordUtil;
import com.imooc.pan.server.modules.file.constants.FileConstants;
import com.imooc.pan.server.modules.file.context.CreateFolderContext;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.server.modules.user.constants.UserConstants;
import com.imooc.pan.server.modules.user.context.*;
import com.imooc.pan.server.modules.user.converter.UserConverter;
import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import com.imooc.pan.server.modules.user.service.IUserService;
import com.imooc.pan.server.modules.user.mapper.driveHarborUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
* @author benchi
* @description 针对表【r_pan_user(user information table)】的数据库操作Service实现
* @createDate 2023-10-23 20:56:15
*/
@Service(value = "userService")
public class UserServiceImpl extends ServiceImpl<driveHarborUserMapper, driveHarborUser> implements IUserService {
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private IUserFileService iUserFileService;
    @Autowired
    private CacheManager cacheManager;

    /**
     * 1. user info registration
     * 2. create user root foler
     * 3. Idempotence: add unique indexing in the database
     * 4. user name globally unique:
     * 5.
     * @param userRegisterContext
     * @return
     */
    @Override
    public Long register(UserRegisterContext userRegisterContext) {
        assembleUserEntity(userRegisterContext);
        doRegister(userRegisterContext);
        createUserRootFolder(userRegisterContext);
        return userRegisterContext.getEntity().getUserId();
    }

    /**
     * user login
     * 1. user login info verification
     * 2. access token
     * 3. access token cached
     * @param userLoginContext
     * @return
     */
    @Override
    public String login(UserLoginContext userLoginContext) {
        checkLoginInfo(userLoginContext);
        generateAndSaveAccessToken(userLoginContext);
        return userLoginContext.getAccessToken();
    }

    /**
     * user logout
     * clear the user's login cache
     * @param userId
     */
    @Override
    public void exit(Long userId) {
        try {
            Cache cache = cacheManager.getCache(CacheConstants.DRIVE_HARBOR_CACHE_NAME);
            cache.evict(UserConstants.USER_LOGIN_PREFIX + userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new driveHarborBusinessException("User logout failed");
        }
    }

    @Override
    /**
     * check username
     */
    public String checkUsername(CheckUsernameContext checkUsernameContext) {

        String question = baseMapper.selectQuestionByUsername(checkUsernameContext.getUsername());
        if (StringUtils.isBlank(question)) {
            throw new driveHarborBusinessException("Username doesn't exist");
        }
        return question;
    }

    /**
     * user forgot the password, check the answer
     * @param checkAnswerContext
     * @return
     */
    @Override
    public String checkAnswer(CheckAnswerContext checkAnswerContext) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", checkAnswerContext.getUsername());
        queryWrapper.eq("question", checkAnswerContext.getQuestion());
        queryWrapper.eq("answer", checkAnswerContext.getAnswer());

        int count = count(queryWrapper);
        if (count == 0) {
            throw new driveHarborBusinessException("Incorrect answer");
        }

        return generateCheckAnswerToken(checkAnswerContext);

    }

    @Override
    public void resetPassword(ResetPasswordContext resetPasswordContext) {
        checkForgetPasswordToken(resetPasswordContext);
        checkAndResetUserPassword(resetPasswordContext);
    }

    /**
     * user updates the password
     * @param changePasswordContext
     */
    @Override
    public void changePassword(ChangePasswordContext changePasswordContext) {
        checkOldPassword(changePasswordContext);
        doChangePassword(changePasswordContext);
        exitLoginStatus(changePasswordContext);
    }


    /**
     * check old password
     * @param changePasswordContext
     */
    private void checkOldPassword(ChangePasswordContext changePasswordContext) {
        Long userId = changePasswordContext.getUserId();
        String oldPassword = changePasswordContext.getOldPassword();

        driveHarborUser entity = getById(userId);
        if (Objects.isNull(entity)) {
            throw new driveHarborBusinessException("User doesn't exist");
        }
        changePasswordContext.setEntity(entity);

        String encOldPassword = PasswordUtil.encryptPassword(entity.getSalt(), oldPassword);
        String dbOldPassword = entity.getPassword();
        if (!Objects.equals(encOldPassword, dbOldPassword)) {
            throw new driveHarborBusinessException("incorrect old password");
        }
    }

    /**
     * change the password after verifying the old password
     * @param changePasswordContext
     */
    private void doChangePassword(ChangePasswordContext changePasswordContext) {
        String newPassword = changePasswordContext.getNewPassword();
        driveHarborUser entity = changePasswordContext.getEntity();
        String salt = entity.getSalt();

        String encNewPassword = PasswordUtil.encryptPassword(salt, newPassword);

        entity.setPassword(encNewPassword);

        if (!updateById(entity)) {
            throw new driveHarborBusinessException("failed to change the password");
        }
    }

    /**
     * exit login status after updating the password
     * @param changePasswordContext
     */
    private void exitLoginStatus(ChangePasswordContext changePasswordContext) {
        exit(changePasswordContext.getUserId());
    }
    /**
     * generate the token when the verification of the answer to the security question is correct
     * @param checkAnswerContext
     * @return
     */
    private String generateCheckAnswerToken(CheckAnswerContext checkAnswerContext) {
        String token = JwtUtil.generateToken(checkAnswerContext.getUsername(), UserConstants.FORGET_USERNAME, checkAnswerContext.getUsername(), UserConstants.FIVE_MINUTES_LONG);
        return token;
    }

    /**
     * Verify user info and reset the password
     *
     * @param resetPasswordContext
     */
    private void checkAndResetUserPassword(ResetPasswordContext resetPasswordContext) {
        String username = resetPasswordContext.getUsername();
        String password = resetPasswordContext.getPassword();
        driveHarborUser entity = getDriveHarborUserByUsername(username);
        if (Objects.isNull(entity)) {
            throw new driveHarborBusinessException("user doesn't exist");
        }

        String newDbPassword = PasswordUtil.encryptPassword(entity.getSalt(), password);
        entity.setPassword(newDbPassword);
        entity.setUpdateTime(new Date());

        if (!updateById(entity)) {
            throw new driveHarborBusinessException("Password resetting failed");
        }
    }

    /**
     * Validate the token for resetting password
     *
     * @param resetPasswordContext
     */
    private void checkForgetPasswordToken(ResetPasswordContext resetPasswordContext) {
        String token = resetPasswordContext.getToken();
        Object value = JwtUtil.analyzeToken(token, UserConstants.FORGET_USERNAME);
        if (Objects.isNull(value)) {
            throw new driveHarborBusinessException(ResponseCode.TOKEN_EXPIRE);
        }
        String tokenUsername = String.valueOf(value);
        if (!Objects.equals(tokenUsername, resetPasswordContext.getUsername())) {
            throw new driveHarborBusinessException("token error");
        }
    }
    private void checkLoginInfo(UserLoginContext userLoginContext) {
        String username = userLoginContext.getUsername();
        String password = userLoginContext.getPassword();

        driveHarborUser entity = getDriveHarborUserByUsername(username);
        if (Objects.isNull(entity)) {
            throw new driveHarborBusinessException("Username doesn't exist");
        }

        String salt = entity.getSalt();
        String encPassword = PasswordUtil.encryptPassword(salt, password);
        String dbPassword = entity.getPassword();
        if (!Objects.equals(encPassword, dbPassword)) {
            throw new driveHarborBusinessException("Incorrect password");
        }

        userLoginContext.setEntity(entity);
    }

    /**
     * get user entity by username
     * @param username
     * @return
     */
    private driveHarborUser getDriveHarborUserByUsername(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }

    private void generateAndSaveAccessToken(UserLoginContext userLoginContext) {
        driveHarborUser entity = userLoginContext.getEntity();

        String accessToken = JwtUtil.generateToken(entity.getUsername(), UserConstants.LOGIN_USER_ID, entity.getUserId(), UserConstants.ONE_DAY_LONG);

        Cache cache = cacheManager.getCache(CacheConstants.DRIVE_HARBOR_CACHE_NAME);
        cache.put(UserConstants.USER_LOGIN_PREFIX + entity.getUserId(), accessToken);

        userLoginContext.setAccessToken(accessToken);


    }

    /**
     * create user root folder
     * @param userRegisterContext
     */
    private void createUserRootFolder(UserRegisterContext userRegisterContext) {
        CreateFolderContext createFolderContext = new CreateFolderContext();
        createFolderContext.setParentId(FileConstants.TOP_PARENT_ID);
        createFolderContext.setUserId(userRegisterContext.getEntity().getUserId());
        createFolderContext.setFolderName(FileConstants.ALL_FILE_STR);
        iUserFileService.createFolder(createFolderContext);
    }

    /**
     * user registration
     * user name globally unique
     * capture the index conflicts in the database
     * @param userRegisterContext
     */
    private void doRegister(UserRegisterContext userRegisterContext) {
        driveHarborUser entity = userRegisterContext.getEntity();
        if (Objects.nonNull(entity)) {
            try {
                if (!save(entity)) {
                    throw new driveHarborBusinessException("Registration Failure");
                }
            } catch (DuplicateKeyException duplicateKeyException) {
                throw new driveHarborBusinessException("Username Exists");
            }
            return;
        }
        throw new driveHarborBusinessException(ResponseCode.ERROR);
    }

    /**
     * based on the context, convert the info  to user entity
     * @param userRegisterContext
     */
    private void assembleUserEntity(UserRegisterContext userRegisterContext) {
        driveHarborUser entity = userConverter.userRegisterContext2DriveHarborUser(userRegisterContext);
        String salt = PasswordUtil.getSalt(),
                dbPassword = PasswordUtil.encryptPassword(salt, userRegisterContext.getPassword());
        entity.setUserId(IdUtil.get());
        entity.setSalt(salt);
        entity.setPassword(dbPassword);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        userRegisterContext.setEntity(entity);

    }

}




