package com.imooc.pan.web.log;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Map;

/**
 * http request log print
 */
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Slf4j
public class HttpLogEntity {

    private String requestUri;

    private String method;

    private String remoteAddr;

    private String ip;

    private Map<String, String> requestHeaders;

    private String requestParams;

    private Integer status;

    private Map<String, String> responseHeaders;

    private String responseData;

    private String resolveTime;

    /**
     * print log
     */
    public void print() {
        log.info("====================HTTP CALL START====================");
        log.info("callTime: {}", DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(new Date()));
        log.info("requestUri: {}", getRequestUri());
        log.info("method: {}", getMethod());
        log.info("remoteAddr: {}", getRemoteAddr());
        log.info("ip: {}", getIp());
        log.info("requestHeaders: {}", getRequestHeaders());
        log.info("requestParams: {}", getRequestParams());
        log.info("status: {}", getStatus());
        log.info("responseHeaders: {}", getResponseHeaders());
        log.info("responseData: {}", getResponseData());
        log.info("resolveTime: {}", getResolveTime());
        log.info("====================HTTP CALL FINISH====================");
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("====================HTTP CALL START====================");
        stringBuilder.append("callTime: ");
        stringBuilder.append(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(new Date()));
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("requestUri: ");
        stringBuilder.append(getRequestUri());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("method: ");
        stringBuilder.append(getMethod());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("remoteAddr: ");
        stringBuilder.append(getRemoteAddr());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("ip: ");
        stringBuilder.append(getIp());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("requestHeaders: ");
        stringBuilder.append(getRequestHeaders());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("requestParams: ");
        stringBuilder.append(getRequestParams());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("status: ");
        stringBuilder.append(getStatus());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("responseHeaders: ");
        stringBuilder.append(getResponseHeaders());
        stringBuilder.append(System.lineSeparator());


        stringBuilder.append("responseData: ");
        stringBuilder.append(getResponseData());
        stringBuilder.append(System.lineSeparator());


        stringBuilder.append("resolveTime: ");
        stringBuilder.append(getResolveTime());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("====================HTTP CALL FINISH====================");
        return stringBuilder.toString();
    }

}
