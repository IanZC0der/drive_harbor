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
import com.imooc.pan.server.modules.user.context.UserLoginContext;
import com.imooc.pan.server.modules.user.context.UserRegisterContext;
import com.imooc.pan.server.modules.user.converter.UserConverter;
import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import com.imooc.pan.server.modules.user.service.IUserService;
import com.imooc.pan.server.modules.user.mapper.driveHarborUserMapper;
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




