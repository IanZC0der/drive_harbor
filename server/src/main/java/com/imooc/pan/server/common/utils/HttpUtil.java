package com.imooc.pan.server.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

/**
 * Http utils
 */
public class HttpUtil {

    /**
     * add cross-region response header
     *
     * @param response
     */
    public static void addCorsResponseHeaders(HttpServletResponse response) {
        for (CorsConfigEnum corsConfigEnum : CorsConfigEnum.values()) {
            response.setHeader(corsConfigEnum.getKey(), corsConfigEnum.getValue());
        }
    }

    /**
     * cors enums
     */
    @AllArgsConstructor
    @Getter
    public enum CorsConfigEnum {
        /**
         * allow cross region
         */
        CORS_ORIGIN("Access-Control-Allow-Origin", "*"),
        /**
         * allow credentials
         */
        CORS_CREDENTIALS("Access-Control-Allow-Credentials", "true"),
        /**
         * allow all remote requests
         */
        CORS_METHODS("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT"),
        /**
         * validity duration in secs
         */
        CORS_MAX_AGE("Access-Control-Max-Age", "3600"),
        /**
         * allow all request headers
         */
        CORS_HEADERS("Access-Control-Allow-Headers", "*");

        private String key;
        private String value;

    }

}
