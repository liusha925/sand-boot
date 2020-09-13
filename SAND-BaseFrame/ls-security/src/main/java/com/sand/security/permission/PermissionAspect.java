/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/9/13/013   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.security.permission;

import com.sand.common.exception.BusinessException;
import com.sand.common.util.lang3.AnnotationUtil;
import com.sand.common.util.lang3.StringUtil;
import com.sand.common.util.spring.SpringUtil;
import com.sand.common.vo.ResultVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 功能说明：大致描述 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/9/13/013 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Aspect
@Component
public class PermissionAspect {

  /**
   * 定义横切点：横切带有@LogAnnotation的方法
   */
  @Pointcut("@annotation(com.sand.security.permission.CheckPermission)")
  public void aopMethod() {
  }

  /**
   * 定义环绕通知
   */
  @Around("aopMethod()")
  public void around(ProceedingJoinPoint point) throws Throwable {
    Method method = AnnotationUtil.getAnnotationMethod(point);
    CheckPermission checkPermission;
    Object serviceBean;
    try {
      checkPermission = method.getAnnotation(CheckPermission.class);
      String service = checkPermission.service();
      if (StringUtil.isBlank(service)) {
        throw new BusinessException("请联系系统管理员配置您的权限实现模块");
      }
      serviceBean = SpringUtil.getBean(service);
      // 强制使用IPermissionService的实现类
      if (!(serviceBean instanceof IPermissionService)) {
        throw new BusinessException("您的权限实现模块配置错误，请联系系统管理员");
      }
    } catch (Exception e) {
      String exceptionMsg = (e instanceof BusinessException) ? e.getMessage() : "您的权限实现模块配置错误，请联系系统管理员。";
      throw new BusinessException(exceptionMsg);
    }

    try {
      IPermissionService permissionService = (IPermissionService) serviceBean;
      permissionService.hasPermission(checkPermission.authKey(), checkPermission.authName());
      Object[] args = point.getArgs();
      point.proceed(args);
    } catch (Throwable e) {
      if (e instanceof BusinessException) {
        throw new BusinessException(e.getMessage());
      }
      throw new Throwable("权限处理失败");
    }
  }
}
