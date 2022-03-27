package com.sand.core.util.norepeat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能说明：防重复提交 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/3/27/027 21:09 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatRequest {
}
