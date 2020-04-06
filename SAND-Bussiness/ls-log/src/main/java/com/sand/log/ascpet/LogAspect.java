/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/24    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.log.ascpet;

import com.sand.common.enums.CodeEnum;
import com.sand.common.enums.LogStatusEnum;
import com.sand.common.exception.BusinessException;
import com.sand.common.util.lang3.AnnotationUtil;
import com.sand.common.util.spring.SpringUtil;
import com.sand.log.service.IBaseLogService;
import com.sand.log.annotation.LogAnnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 功能说明：日志切面类
 * 开发人员：@author liusha
 * 开发日期：2019/9/24 13:30
 * 功能描述：切面处理日志信息
 */
@Aspect
@Component
public class LogAspect {
  /**
   * 定义横切点：横切带有@Log的方法
   */
  @Pointcut("@annotation(com.sand.log.annotation.LogAnnotation)")
  public void aopMethod() {
  }

  /**
   * 定义环绕通知：前置获取日志信息，后置保存日志信息
   *
   * @return
   */
  @Around("aopMethod()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    Object obj;
    Method method = AnnotationUtil.getAnnotationMethod(point);
    LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
    IBaseLogService logService = (IBaseLogService) SpringUtil.getBean(logAnnotation.service());
    // 获取日志对象
    Object log = logService.init();
    // 获取参数信息
    Object[] args = point.getArgs();
    // 获取基础信息
    logService.beforeProceed(log, args);
    // 执行开始
    long startTime = System.currentTimeMillis();
    int status = LogStatusEnum.INIT.getStatus();
    try {
      obj = point.proceed(args);
      status = LogStatusEnum.SUCCESS.getStatus();
    } catch (Throwable e) {
      status = LogStatusEnum.EXCEPTION.getStatus();
      // 处理异常信息
      logService.exceptionProceed(log, e);
      if (e instanceof BusinessException) {
        throw new BusinessException(e.getMessage());
      }
      throw new Throwable(CodeEnum.ERROR.getMsg());
    } finally {
      // 执行结束
      long endTime = System.currentTimeMillis();
      // 获取注解信息
      logService.afterProceed(log, method);
      // 保存日志记录
      logService.save(log, (endTime - startTime), status);
    }
    return obj;
  }
}
