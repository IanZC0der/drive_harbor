package com.imooc.pan.server.common.aspect;

import com.imooc.pan.core.response.R;
import com.imooc.pan.core.response.ResponseCode;
import com.imooc.pan.core.utils.JwtUtil;
import com.imooc.pan.server.common.utils.ShareIdUtil;
import com.imooc.pan.server.modules.share.constants.ShareConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * share code aspect
 */
@Component
@Aspect
@Slf4j
public class ShareCodeAspect {

    /**
     * share token
     */
    private static final String SHARE_CODE_AUTH_PARAM_NAME = "shareToken";

    /**
     * request header key
     */
    private static final String SHARE_CODE_AUTH_REQUEST_HEADER_NAME = "Share-Token";

    /**
     * point cut expression
     */
    private final static String POINT_CUT = "@annotation(com.imooc.pan.server.common.annotation.NeedShareCode)";

    /**
     * share template
     */
    @Pointcut(value = POINT_CUT)
    public void shareCodeAuth() {

    }

    /**
     * 1. check to see if login verification is needed
     * 2. verify login info
     *  a. get token from request header or params
     *  b. get token from the cache and compare them
     *  c.  analyze token
     *  d. save the userId in the context
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("shareCodeAuth()")
    public Object shareCodeAuthAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String requestURI = request.getRequestURI();
        log.info("request captured, URI: {}", requestURI);
        if (!checkAndSaveShareId(request)) {
            log.warn("request captured, URI: {}. Share code expired, jumping to the share code verification page...", requestURI);
            return R.fail(ResponseCode.ACCESS_DENIED);
        }
        log.info("request captured, URI: {}, request passed", requestURI);
        return proceedingJoinPoint.proceed();
    }

    /**
     *
     * verify token and get share id
     * @param request
     * @return
     */
    private boolean checkAndSaveShareId(HttpServletRequest request) {
        String shareToken = request.getHeader(SHARE_CODE_AUTH_REQUEST_HEADER_NAME);
        if (StringUtils.isBlank(shareToken)) {
            shareToken = request.getParameter(SHARE_CODE_AUTH_PARAM_NAME);
        }
        if (StringUtils.isBlank(shareToken)) {
            return false;
        }
        Object shareId = JwtUtil.analyzeToken(shareToken, ShareConstants.SHARE_ID);
        if (Objects.isNull(shareId)) {
            return false;
        }
        saveShareId(shareId);
        return true;
    }

    /**
     * save share id to the context
     *
     * @param shareId
     */
    private void saveShareId(Object shareId) {
        ShareIdUtil.set(Long.valueOf(String.valueOf(shareId)));
    }

}
