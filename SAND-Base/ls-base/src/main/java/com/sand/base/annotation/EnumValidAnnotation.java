/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.annotation;

import com.sand.base.util.validator.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能说明：验证枚举类
 * 开发人员：@author liusha
 * 开发日期：2019/9/26 13:46
 * 功能描述：对表单中存在枚举类型的字段进行校验
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValidator.class})
@Documented
public @interface EnumValidAnnotation {
  String message() default "";

  Class<?>[] groups() default {};

  Class<?>[] target() default {};

  Class<? extends Payload>[] payload() default {};
}
