package com.imooc.pan.server.common.aspect;

import com.imooc.pan.cache.core.constants.CacheConstants;
import com.imooc.pan.core.response.R;
import com.imooc.pan.core.response.ResponseCode;
import com.imooc.pan.core.utils.JwtUtil;
import com.imooc.pan.server.common.annotation.LoginIgnore;
import com.imooc.pan.server.common.utils.UserIdUtil;
import com.imooc.pan.server.modules.user.constants.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class CommonLoginAspect {
    /**
     * login auto param name
     */
    private static final String LOGIN_AUTH_PARAM_NAME = "authorization";

    /**
     * request header name
     */
    private static final String LOGIN_AUTH_REQUEST_HEADER_NAME = "Authorization";

    /**
     * point cut expression
     */
    private final static String POINT_CUT = "execution(* com.imooc.pan.server.modules.*.controller..*(..))";

    @Autowired
    private CacheManager cacheManager;

    @Pointcut(value = POINT_CUT)
    public void loginAuth() {

    }

    /**
     * point cut around
     * 1. check to see if login verification is needed
     * 2. verify login info
     *  a. get token from request header or params
     *  b. get token from the cache and compare them
     *  c.  analyze token
     *  d. save the userId in the context
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("loginAuth()")
    public Object loginAuthAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (checkNeedCheckLoginInfo(proceedingJoinPoint)) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String requestURI = request.getRequestURI();
            log.info("request captured, URI: {}", requestURI);
            if (!checkAndSaveUserId(request)) {
                log.warn("request captured, URI: {}. User didn't login. will jump to login page", requestURI);
                return R.fail(ResponseCode.NEED_LOGIN);
            }
            log.info("request captured, URI: {}. request passed", requestURI);
        }
        return proceedingJoinPoint.proceed();
    }

    /**
     * check and get/save user id
     * @param request
     * @return
     */
    private boolean checkAndSaveUserId(HttpServletRequest request) {
        String accessToken = request.getHeader(LOGIN_AUTH_REQUEST_HEADER_NAME);
        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter(LOGIN_AUTH_PARAM_NAME);
        }
        if (StringUtils.isBlank(accessToken)) {
            return false;
        }
        Object userId = JwtUtil.analyzeToken(accessToken, UserConstants.LOGIN_USER_ID);
        if (Objects.isNull(userId)) {
            return false;
        }

        Cache cache = cacheManager.getCache(CacheConstants.DRIVE_HARBOR_CACHE_NAME);
        String redisAccessToken = cache.get(UserConstants.USER_LOGIN_PREFIX + userId, String.class);

        if (StringUtils.isBlank(redisAccessToken)) {
            return false;
        }

        if (Objects.equals(accessToken, redisAccessToken)) {
            saveUserId(userId);
            return true;
        }

        return false;
    }

    /**
     * save the userId to the context
     * @param userId
     */
    private void saveUserId(Object userId) {
        UserIdUtil.set(Long.valueOf(String.valueOf(userId)));
    }

    /**
     * check to see if login verification is needed
     * @param proceedingJoinPoint
     * @return
     */
    private boolean checkNeedCheckLoginInfo(ProceedingJoinPoint proceedingJoinPoint) {
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return !method.isAnnotationPresent(LoginIgnore.class);
    }


}
