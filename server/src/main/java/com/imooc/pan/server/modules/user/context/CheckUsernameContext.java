package com.imooc.pan.server.modules.user.context;

import lombok.Data;

import java.io.Serializable;

@Data
public class CheckUsernameContext implements Serializable {

    private static final long serialVersionUID = -7117844539768126736L;

    /**
     * Username
     */
    private String username;

}
