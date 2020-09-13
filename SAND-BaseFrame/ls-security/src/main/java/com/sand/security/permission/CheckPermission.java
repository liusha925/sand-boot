/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/9/13/013   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.security.permission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能说明：自定义实现权限控制体系 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/9/13/013 <br>
 * 功能描述：自定义实现权限控制体系 <br>
 */
//注解作用在方法上
@Target({ElementType.METHOD})
//注解的生命周期：不仅被保存到class文件中，jvm加载class文件之后，仍然存在
@Retention(RetentionPolicy.RUNTIME)
//允许被继承
@Inherited
//注解包含在Javadoc中
@Documented
public @interface CheckPermission {
  /**
   * 实现类
   *
   * @return 实现类
   */
  String service();

  /**
   * 权限标识
   *
   * @return 标识
   */
  String authKey() default "";

  /**
   * 权限名称
   *
   * @return 名称
   */
  String authName() default "";
}
