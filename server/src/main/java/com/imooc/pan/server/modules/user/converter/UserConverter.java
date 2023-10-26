package com.imooc.pan.server.modules.user.converter;

import com.imooc.pan.server.modules.user.context.UserLoginContext;
import com.imooc.pan.server.modules.user.context.UserRegisterContext;
import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import com.imooc.pan.server.modules.user.po.UserLoginPO;
import com.imooc.pan.server.modules.user.po.UserRegisterPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserRegisterContext userRegisterPO2UserRegisterContext(UserRegisterPO userRegisterPO);

    @Mapping(target = "password", ignore = true)
    driveHarborUser userRegisterContext2DriveHarborUser(UserRegisterContext userRegisterContext);

    UserLoginContext userLoginPO2UserLoginContext(UserLoginPO userLoginPO);
}
