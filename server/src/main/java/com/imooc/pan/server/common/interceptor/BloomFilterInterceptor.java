package com.imooc.pan.server.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * bloom filter interceptor interface
 */
public interface BloomFilterInterceptor extends HandlerInterceptor {

    /**
     * interceptor name
     *
     * @return
     */
    String getName();

    /**
     * URIs to be intercepted
     *
     * @return
     */
    String[] getPathPatterns();

    /**
     *
     * URIs to be disintercepted
     * @return
     */
    String[] getExcludePatterns();

}
