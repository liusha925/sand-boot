/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/25    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web.handler;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.sand.common.vo.ResultVO;
import com.sand.common.exception.BusinessException;
import com.sand.common.exception.handler.DefaultExceptionHandler;
import com.sand.common.util.ResultUtil;
import com.sand.common.util.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能说明：权限认证异常处理
 * 开发人员：@author liusha
 * 开发日期：2019/11/25 16:07
 * 功能描述：权限认证异常处理
 */
@Slf4j
@Order(100)
@RestControllerAdvice
public class MyAuthExceptionHandler extends DefaultExceptionHandler {

  @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
  public ResultVO handleSecurityException(Exception e) {
    // 登录校验不通过：用户名或密码无效
    if (e instanceof BadCredentialsException || e instanceof InternalAuthenticationServiceException || e instanceof UsernameNotFoundException) {
      return handleBusinessException(new BusinessException(ResultVO.Code.USERNAME_NOT_FOUND));
    }
    // 凭证已过期
    if (e instanceof CredentialsExpiredException) {
      return handleBusinessException(new BusinessException(ResultVO.Code.CREDENTIALS_EXPIRED));
    }
    // 此账号已过期
    if (e instanceof AccountExpiredException) {
      return handleBusinessException(new BusinessException(ResultVO.Code.ACCOUNT_EXPIRED));
    }
    // 此账号已被禁用
    if (e instanceof DisabledException) {
      return handleBusinessException(new BusinessException(ResultVO.Code.DISABLED));
    }
    // 此账号已被锁定
    if (e instanceof LockedException) {
      return handleBusinessException(new BusinessException(ResultVO.Code.LOCKED));
    }
    // 单点登录：此账号已在他处登录，请重新登录
    if (e instanceof AccessDeniedException) {
      return handleBusinessException(new BusinessException(ResultVO.Code.SINGLE_LOGIN));
    }
    // 其它业务异常
    if (e.getCause() instanceof BusinessException) {
      return handleBusinessException((BusinessException) e.getCause());
    }
    super.errorLog(e);
    return ResultUtil.error();
  }

  /**
   * 注册消息转换器
   *
   * @return FastJsonHttpMessageConverter
   */
  @Bean
  public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
    return new FastJsonHttpMessageConverter();
  }

  /**
   * 拒绝访问
   * 验证不通过或验证异常
   *
   * @param e        Exception
   * @param response 响应信息
   * @throws IOException
   */
  public static void accessDeniedException(Exception e, ServletResponse response) throws IOException {
    ResultVO ResultVO = ResultUtil.error(com.sand.common.vo.ResultVO.Code.LOGIN_EXPIRED);
    if (e instanceof BadCredentialsException || e instanceof InternalAuthenticationServiceException || e instanceof UsernameNotFoundException) {
      ResultVO = ResultUtil.error(com.sand.common.vo.ResultVO.Code.USERNAME_NOT_FOUND);
    }
    if (e instanceof BusinessException) {
      ResultVO = ResultUtil.info(null, ((BusinessException) e).getCode(), e.getMessage());
    }
    HttpMessageConverter httpMessageConverter = SpringUtil.getBean(FastJsonHttpMessageConverter.class);
    httpMessageConverter.write(ResultVO, MediaType.APPLICATION_JSON, new ServletServerHttpResponse((HttpServletResponse) response));
  }
}
