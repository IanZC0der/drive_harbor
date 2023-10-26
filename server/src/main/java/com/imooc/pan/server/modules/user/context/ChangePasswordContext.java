package com.imooc.pan.server.modules.user.context;

import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import lombok.Data;

import java.io.Serializable;

/**
 * the context of user updating the password
 */
@Data
public class ChangePasswordContext implements Serializable {

    /**
     * userId
     */
    private Long userId;

    /**
     * old password
     */
    private String oldPassword;

    /**
     * new password
     */
    private String newPassword;

    /**
     * info of the user currently logged in
     */
    private driveHarborUser entity;

}
