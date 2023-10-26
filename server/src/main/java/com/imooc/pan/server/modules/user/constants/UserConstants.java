package com.imooc.pan.server.modules.user.constants;

public interface UserConstants {
    /**
     * login user id key
     */
    String LOGIN_USER_ID = "LOGIN_USER_ID";

    /**
     * user login prefix
     */
    String USER_LOGIN_PREFIX = "USER_LOGIN_";

    /**
     * user forgets password
     * the key for the temporary token while they reset the password
     */
    String FORGET_USERNAME = "FORGET_USERNAME";

    /**
     * milli seconds for a day
     */
    Long ONE_DAY_LONG = 24L * 60L * 60L * 1000L;

    /**
     * milli seconds for 5 min
     */
    Long FIVE_MINUTES_LONG = 5L * 60L * 1000L;

}
