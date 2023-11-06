package com.imooc.pan.server.common.utils;

import com.imooc.pan.core.constants.driveHarborConstants;

import java.util.Objects;

/**
 * share id util
 */
public class ShareIdUtil {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * share id of the current thread
     *
     * @param shareId
     */
    public static void set(Long shareId) {
        threadLocal.set(shareId);
    }

    /**
     * get the share id of the current thread
     *
     * @return
     */
    public static Long get() {
        Long shareId = threadLocal.get();
        if (Objects.isNull(shareId)) {
            return driveHarborConstants.ZERO_LONG;
        }
        return shareId;
    }

}
