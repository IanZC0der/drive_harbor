package com.imooc.pan.server.common.annotation;

import java.lang.annotation.*;

/**
 * this annotation affects the interface where share code check is needed
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NeedShareCode {
}
