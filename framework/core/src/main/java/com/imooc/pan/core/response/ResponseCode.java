package com.imooc.pan.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    /**
     * success
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * error
     */
    ERROR(1, "ERROR"),
    /**
     * token expired
     */
    TOKEN_EXPIRE(2, "TOKEN_EXPIRE"),
    /**
     * parameter error
     */
    ERROR_PARAM(3, "ERROR_PARAM"),
    /**
     * access denied
     */
    ACCESS_DENIED(4, "ACCESS_DENIED"),
    /**
     * shared file lost
     */
    SHARE_FILE_MISS(5, "SHARED_FILE_LOST"),
    /**
     * file sharing cancelled
     */
    SHARE_CANCELLED(6, "FILE_SHARING_CANCELLED"),
    /**
     * expired file sharing
     */
    SHARE_EXPIRE(7, "EXPIRED_FILE_SHARING"),
    /**
     * login required
     */
    NEED_LOGIN(10, "NEED_LOGIN");
    //state code
    private Integer code;
    //description
    private String desc;
}
