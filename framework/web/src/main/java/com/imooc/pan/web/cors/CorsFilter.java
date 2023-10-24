package com.imooc.pan.web.cors;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.Order;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "corsFilter")
@Order(1)
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        addCorsResponseHeader(response);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * add response header
     *
     * @param response
     */
    private void addCorsResponseHeader(HttpServletResponse response) {
        response.setHeader(CorsConfigEnum.CORS_ORIGIN.getKey(), CorsConfigEnum.CORS_ORIGIN.getValue());
        response.setHeader(CorsConfigEnum.CORS_CREDENTIALS.getKey(), CorsConfigEnum.CORS_CREDENTIALS.getValue());
        response.setHeader(CorsConfigEnum.CORS_METHODS.getKey(), CorsConfigEnum.CORS_METHODS.getValue());
        response.setHeader(CorsConfigEnum.CORS_MAX_AGE.getKey(), CorsConfigEnum.CORS_MAX_AGE.getValue());
        response.setHeader(CorsConfigEnum.CORS_HEADERS.getKey(), CorsConfigEnum.CORS_HEADERS.getValue());
    }

    /**
     * config enums
     */
    @AllArgsConstructor
    @Getter
    public enum CorsConfigEnum {
        CORS_ORIGIN("Access-Control-Allow-Origin", "*"),
        CORS_CREDENTIALS("Access-Control-Allow-Credentials", "true"),
        CORS_METHODS("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT"),
        CORS_MAX_AGE("Access-Control-Max-Age", "3600"),
        CORS_HEADERS("Access-Control-Allow-Headers", "*");

        private String key;
        private String value;

    }
}
