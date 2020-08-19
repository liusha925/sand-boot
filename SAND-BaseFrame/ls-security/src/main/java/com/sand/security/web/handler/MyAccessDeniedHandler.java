/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/25    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 功能说明：自定义无权处理器
 * 开发人员：@author liusha
 * 开发日期：2019/11/25 16:31
 * 功能描述：用来处理认证授权过程中用户访问无权限资源时的处理方案
 */
@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {
  private static final String REGEX = ".*(.js|.gif|.jpg|.png|.css|.ico)$";

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
    Pattern pattern = Pattern.compile(REGEX);
    if (!pattern.matcher(request.getRequestURI()).matches()) {
      log.error("用户认证异常：{}，URL：{}", e.getMessage(), request.getRequestURI());
    }
    MyAuthExceptionHandler.accessDeniedException(e, response);
  }
}
