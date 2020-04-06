/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.validator;

import com.sand.common.annotation.EnumValidAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 功能说明：枚举验证器
 * 开发人员：@author liusha
 * 开发日期：2019/9/26 13:51
 * 功能描述：自定义枚举验证器
 */
public class EnumValidator implements ConstraintValidator<EnumValidAnnotation, String> {
  /**
   * 枚举类
   */
  Class<?>[] clzs;

  @Override
  public void initialize(EnumValidAnnotation constraintAnnotation) {
    clzs = constraintAnnotation.target();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (clzs.length > 0) {
      try {
        for (Class<?> clz : clzs) {
          if (clz.isEnum()) {
            // 枚举类验证
            Object[] objs = clz.getEnumConstants();
            for (Object obj : objs) {
              Class<?> enumClz = obj.getClass();
              Field[] fields = enumClz.getDeclaredFields();
              for (Field field : fields) {
                // 访问私有成员属性开关
                field.setAccessible(true);
                EnumValidAnnotation enumValidAnnotation = field.getAnnotation(EnumValidAnnotation.class);
                if (Objects.nonNull(enumValidAnnotation)) {
                  // 获取成员属性的值
                  Object enumValue = field.get(obj);
                  if (value.equals(enumValue.toString())) {
                    return true;
                  }
                }
              }
            }
          }
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    } else {
      return true;
    }
    return false;
  }
}
