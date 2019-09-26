/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.validator;

import com.sand.base.annotation.EnumValidAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 功能说明：枚举验证器
 * 开发人员：@author liusha
 * 开发日期：2019/9/26 13:51
 * 功能描述：枚举验证器
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
            Method method = clz.getMethod("name");
            for (Object obj : objs) {
              Object code = method.invoke(obj, null);
              if (value.equals(code.toString())) {
                return true;
              }
            }
          }
        }
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    } else {
      return true;
    }
    return false;
  }
}
