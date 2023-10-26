package com.imooc.pan.server.modules.user.context;

import lombok.Data;

import java.io.Serializable;

import com.imooc.pan.server.modules.user.entity.driveHarborUser;

/**
 * context
 */
@Data

public class UserRegisterContext implements Serializable {
    private static final long serialVersionUID = -4835860208501507531L;

    /**
     * Username
     */
    private String username;

    /**
     * Password
     */
    private String password;

    /**
     * security question
     */
    private String question;

    /**
     * answer
     */
    private String answer;

    /**
     * user entity
     */
    private driveHarborUser entity;
}
