/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/20    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.exception.handler;

import com.sand.core.vo.ResultVO;
import com.sand.core.exception.BusinessException;
import com.sand.core.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 功能说明：全局异常处理
 * 开发人员：@author liusha
 * 开发日期：2019/11/20 16:54
 * 功能描述：全局异常处理
 */
@Slf4j
@Order(101)
@RestControllerAdvice
public class DefaultExceptionHandler {

  /**
   * 顶级异常处理
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(Throwable.class)
  public ResultVO handleThrowable(Throwable e) {
    errorLog(e);
    return ResultUtil.error();
  }

  /**
   * 顶级异常处理
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(Exception.class)
  public ResultVO handleException(Exception e) {
    errorLog(e);
    return ResultUtil.error();
  }

  /**
   * 自定义异常处理
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(BusinessException.class)
  public ResultVO handleBusinessException(BusinessException e) {
    errorLog(e);
    return ResultUtil.info(e.getCode(), e.getMessage());
  }

  /**
   * 参数缺失处理
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResultVO handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
    errorLog(e);
    return ResultUtil.info(ResultVO.Code.PARAM_MISSING_ERROR.getValue(), e.getMessage());
  }

  /**
   * JSON反序列化失败
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResultVO handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    errorLog(e);
    return ResultUtil.info(ResultVO.Code.DESERIALIZE_ERROR.getValue(), e.getMessage());
  }

  /**
   * 打印出错log
   *
   * @param e 异常
   */
  protected void errorLog(Throwable e) {
    StackTraceElement element = e.getStackTrace()[0];
    if (e instanceof BusinessException) {
      log.info("异常位置：{}.{}，第{}行，原因：{}", element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getMessage());
      if (!Objects.isNull(e.getCause())) {
        StackTraceElement cause = e.getCause().getStackTrace()[0];
        log.info("起因：{}.{}，第{}行，原因：{}", cause.getClassName(), cause.getMethodName(), cause.getLineNumber(), e.getCause().getMessage());
      }
    } else {
      log.error("错误位置：{}.{}，第{}行，错误原因：{}", element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getClass().getName());
    }
    log.error("错误信息：", e);
  }

}
