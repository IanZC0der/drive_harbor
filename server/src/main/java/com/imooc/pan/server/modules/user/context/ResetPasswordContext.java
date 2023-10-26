package com.imooc.pan.server.modules.user.context;

import lombok.Data;

import java.io.Serializable;

/**
 * resetpassword context
 */
@Data
public class ResetPasswordContext implements Serializable {

    private static final long serialVersionUID = 6483482439489859204L;

    /**
     * username
     */
    private String username;

    /**
     * new password
     */
    private String password;

    /**
     * token after resetting the password
     */
    private String token;

}
