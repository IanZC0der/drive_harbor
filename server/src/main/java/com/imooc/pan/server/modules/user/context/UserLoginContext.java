package com.imooc.pan.server.modules.user.context;

import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import lombok.Data;

import java.io.Serializable;

/**
 * user login context
 */
@Data
public class UserLoginContext implements Serializable {

    private static final long serialVersionUID = -3754570303177237029L;

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String password;

    /**
     * user entity
     */
    private driveHarborUser entity;

    /**
     * accessToken
     */
    private String accessToken;

}
