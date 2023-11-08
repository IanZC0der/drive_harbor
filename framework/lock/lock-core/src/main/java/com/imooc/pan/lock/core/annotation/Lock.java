package com.imooc.pan.lock.core.annotation;

import com.imooc.pan.lock.core.key.KeyGenerator;
import com.imooc.pan.lock.core.key.StandardKeyGenerator;

import java.lang.annotation.*;

/**
 * custom lock annotation
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Lock {

    /**
     * lock name
     *
     * @return
     */
    String name() default "";

    /**
     * expired time to avoid dead lock
     *
     * @return
     */
    long expireSecond() default 60L;

    /**
     * custom key, EL expression supported
     *
     * @return
     */
    String[] keys() default {};

    /**
     * key generator
     *
     * @return
     */
    Class<? extends KeyGenerator> keyGenerator() default StandardKeyGenerator.class;

}
