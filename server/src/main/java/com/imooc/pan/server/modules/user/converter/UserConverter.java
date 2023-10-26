package com.imooc.pan.server.modules.user.converter;

import com.imooc.pan.server.modules.user.context.*;
import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import com.imooc.pan.server.modules.user.po.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserRegisterContext userRegisterPO2UserRegisterContext(UserRegisterPO userRegisterPO);

    @Mapping(target = "password", ignore = true)
    driveHarborUser userRegisterContext2DriveHarborUser(UserRegisterContext userRegisterContext);

    UserLoginContext userLoginPO2UserLoginContext(UserLoginPO userLoginPO);

    CheckUsernameContext checkUsernamePO2CheckUsernameContext(CheckUsernamePO checkUsernamePO);

    CheckAnswerContext checkAnswerPO2CheckAnswerContext(CheckAnswerPO checkAnswerPO);

    ResetPasswordContext resetPasswordPO2ResetPasswordContext(ResetPasswordPO resetPasswordPO);

    ChangePasswordContext changePasswordPO2ChangePasswordContext(ChangePasswordPO changePasswordPO);
}
