/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.filter;

import com.sand.common.util.lang3.StringUtil;
import com.sand.security.handler.IUserAuthHandler;
import com.sand.security.handler.MyAuthExceptionHandler;
import com.sand.security.util.AbstractTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.GenericFilterBean;

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
 * 功能描述：用户合法性校验
 */
@Slf4j
public class MyAuthenticationTokenGenericFilter extends GenericFilterBean {
  /**
   * MyAuthenticationTokenGenericFilter标记
   */
  private static final String FILTER_APPLIED = "__spring_security_myAuthenticationTokenGenericFilter_filterApplied";
  /**
   * TODO 过滤元数据，后续自己实现
   */
  private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
  /**
   * 用户认证服务接口
   */
  @Autowired
  private IUserAuthHandler userAuthHandler;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    // 确保每个请求仅应用一次过滤器：spring容器托管的GenericFilterBean的bean，都会自动加入到servlet的filter chain，
    // 而WebSecurityConfig中myAuthenticationTokenGenericFilter定义的bean还额外把filter加入到了spring security中，所以会出现执行两次的情况。
    if (httpRequest.getAttribute(FILTER_APPLIED) != null) {
      chain.doFilter(httpRequest, httpResponse);
      return;
    }
    httpRequest.setAttribute(FILTER_APPLIED, Boolean.TRUE);
    log.info("~~~~~~~~~用户合法性校验~~~~~~~~~");
    // 白名单直接验证通过
    if (isPermitUrl(httpRequest, httpResponse, chain)) {
      chain.doFilter(httpRequest, httpResponse);
      return;
    }
    try {
      // 非白名单需验证其合法性（非白名单请求必须带token）
      String authHeader = httpRequest.getHeader(AbstractTokenUtil.TOKEN_HEADER);
      final String authToken = StringUtil.substring(authHeader, 7);
      userAuthHandler.handleAuthToken(authToken);
      chain.doFilter(httpRequest, httpResponse);
    } catch (Exception e) {
      log.error("MyAuthenticationTokenGenericFilter异常", e);
      MyAuthExceptionHandler.accessDeniedException(e, httpResponse);
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
