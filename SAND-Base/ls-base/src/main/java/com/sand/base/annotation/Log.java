/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/6   nevercoming      新增
 * =========  ===========  =====================
 */
package com.sand.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能说明：系统日志注解
 * 开发人员：@author nevercoming
 * 开发时间：2019/8/6 23:45
 * 功能描述：日志系统注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Log {
  String symbol() default "none";

  String service() default "logService";

  String description() default "default";
}
