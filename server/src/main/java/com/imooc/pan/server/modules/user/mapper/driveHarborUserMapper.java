package com.imooc.pan.server.modules.user.mapper;

import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author benchi
* @description 针对表【r_pan_user(user information table)】的数据库操作Mapper
* @createDate 2023-10-23 20:56:15
* @Entity com.imooc.pan.server.modules.user.entity.driveHarborUser
*/
public interface driveHarborUserMapper extends BaseMapper<driveHarborUser> {

    /**
     * look up user security question by the username
     * @param username
     * @return
     */
    String selectQuestionByUsername(@Param("username") String username);
}




