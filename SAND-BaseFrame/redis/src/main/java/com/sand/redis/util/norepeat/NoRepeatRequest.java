package com.sand.redis.util.norepeat;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能说明：防重复请求注解类 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/3/27/027 21:33 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoRepeatRequest {
    /**
     * 防重复请求限时标记（存储redis限时标记数值）
     */
    String value() default "no_repeat_request";

    /**
     * 防重复请求过期时间，默认3s（借助redis实现限时控制）
     */
    long expireSeconds() default 3;
}
