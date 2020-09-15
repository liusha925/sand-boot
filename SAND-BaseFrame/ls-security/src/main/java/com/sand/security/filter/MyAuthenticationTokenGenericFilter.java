/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.filter;

import com.sand.common.util.lang3.StringUtil;
import com.sand.security.handler.IUserAuthorizationHandler;
import com.sand.security.handler.MyAuthExceptionHandler;
import com.sand.security.util.AbstractTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 功能说明：token过滤器
 * 开发人员：@author liusha
 * 开发日期：2020/4/19 17:30
 * 功能描述：用户合法性校验，使用OncePerRequestFilter确保在一次请求只通过一次filter，而不需要重复执行，如果使用GenericFilterBean则会经过两次
 */
@Slf4j
public class MyAuthenticationTokenGenericFilter extends OncePerRequestFilter {
  /**
   * TODO 过滤元数据，后续自己实现
   */
  private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
  /**
   * 用户认证服务接口
   */
  @Autowired
  private IUserAuthorizationHandler userAuthorizationHandler;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    log.info("~~~~~~~~~用户合法性校验~~~~~~~~~");
    // 白名单直接验证通过
    if (isPermitUrl(request, response, chain)) {
      chain.doFilter(request, response);
      return;
    }
    try {
      // 非白名单需验证其合法性（非白名单请求必须带token）
      String authHeader = request.getHeader(AbstractTokenUtil.TOKEN_HEADER);
      final String authToken = StringUtil.substring(authHeader, 7);
      userAuthorizationHandler.handleAuthToken(authToken);
      chain.doFilter(request, response);
    } catch (Exception e) {
      log.error("MyAuthenticationTokenGenericFilter异常", e);
      MyAuthExceptionHandler.accessDeniedException(e, response);
    }
  }

  /**
   * 是否是白名单
   *
   * @param request  request
   * @param response response
   * @param chain    chain
   * @return true-是白名单 false-不是白名单
   */
  private boolean isPermitUrl(ServletRequest request, ServletResponse response, FilterChain chain) {
    if (Objects.isNull(filterInvocationSecurityMetadataSource)) {
      try {
        // 获取security配置的白名单信息
        Class clazz = chain.getClass();
        Field field = clazz.getDeclaredField("additionalFilters");
        field.setAccessible(true);
        List<Filter> filters = (List<Filter>) field.get(chain);
        for (Filter filter : filters) {
          if (filter instanceof FilterSecurityInterceptor) {
            filterInvocationSecurityMetadataSource = ((FilterSecurityInterceptor) filter).getSecurityMetadataSource();
          }
        }
      } catch (Exception e) {
        log.error("security过滤元数据获取异常，白名单验证失败", e);
        return false;
      }
    }
    FilterInvocation filterInvocation = new FilterInvocation(request, response, chain);
    Collection<ConfigAttribute> permitUrls = filterInvocationSecurityMetadataSource.getAttributes(filterInvocation);
    boolean isPermitUrl = false;
    if (!CollectionUtils.isEmpty(permitUrls)) {
      isPermitUrl = permitUrls.toString().contains("permitAll");
    }
    if (isPermitUrl) {
      log.info("白名单请求url：{}", ((HttpServletRequest) request).getRequestURI());
    } else {
      log.info("非白名单请求url：{}", ((HttpServletRequest) request).getRequestURI());
    }
    return isPermitUrl;
  }
}
