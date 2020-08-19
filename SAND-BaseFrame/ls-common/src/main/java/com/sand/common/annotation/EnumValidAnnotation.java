/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.annotation;

import com.sand.common.util.lang3.StringUtil;
import com.sand.common.util.validator.EnumValidator;

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
  /**
   * 提示消息
   *
   * @return
   */
  String message() default StringUtil.EMPTY;

  /**
   * 对应的枚举类
   *
   * @return
   */
  Class<?>[] target() default {};

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
