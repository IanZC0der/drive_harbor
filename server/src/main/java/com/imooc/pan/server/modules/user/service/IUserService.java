package com.imooc.pan.server.modules.user.service;

import com.imooc.pan.server.modules.user.context.*;
import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author benchi
* @description 针对表【r_pan_user(user information table)】的数据库操作Service
* @createDate 2023-10-23 20:56:15
*/
public interface IUserService extends IService<driveHarborUser> {

    Long register(UserRegisterContext userRegisterContext);

    String login(UserLoginContext userLoginContext);

    void exit(Long aLong);

    String checkUsername(CheckUsernameContext checkUsernameContext);

    String checkAnswer(CheckAnswerContext checkAnswerContext);

    void resetPassword(ResetPasswordContext resetPasswordContext);

    void changePassword(ChangePasswordContext changePasswordContext);
}
