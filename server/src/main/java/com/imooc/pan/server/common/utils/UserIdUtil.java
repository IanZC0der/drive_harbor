package com.imooc.pan.server.common.utils;
import com.imooc.pan.core.constants.driveHarborConstants;

import java.util.Objects;


public class UserIdUtil {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * set userId in the current thread
     *
     * @param userId
     */
    public static void set(Long userId) {
        threadLocal.set(userId);
    }

    /**
     * get the userId in the current thread
     *
     * @return
     */
    public static Long get() {
        Long userId = threadLocal.get();
        if (Objects.isNull(userId)) {
            return driveHarborConstants.ZERO_LONG;
        }
        return userId;
    }

}
