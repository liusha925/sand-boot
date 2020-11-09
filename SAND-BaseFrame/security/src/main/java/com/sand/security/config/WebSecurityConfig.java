/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.config;

import com.sand.security.filter.MyAuthenticationTokenGenericFilter;
import com.sand.security.handler.MyAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 功能说明：自定义Spring Security配置
 * 开发人员：@author liusha
 * 开发日期：2019/11/26 10:34
 * 功能描述：安全认证基础配置，开启 Spring Security
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  /**
   * 认证管理器：使用spring自带的验证密码的流程
   * <p>
   * 负责验证、认证成功后，AuthenticationManager 返回一个填充了用户认证信息（包括权限信息、身份信息、详细信息等，但密码通常会被移除）的 Authentication 实例。
   * 然后再将 Authentication 设置到 SecurityContextHolder 容器中。
   * AuthenticationManager 接口是认证相关的核心接口，也是发起认证的入口。
   * 但它一般不直接认证，其常用实现类 ProviderManager 内部会维护一个 List<AuthenticationProvider> 列表，
   * 存放里多种认证方式，默认情况下，只需要通过一个 AuthenticationProvider 的认证，就可被认为是登录成功
   *
   * @return 认证信息
   * @throws Exception Exception
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   * 密码验证方式
   * 默认加密方式为BCryptPasswordEncoder
   *
   * @return 密码验证方式
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(6);
  }

  /**
   * 加载自定义的验证失败处理方式
   *
   * @return 处理方式
   */
  @Bean
  public MyAccessDeniedHandler myAccessDeniedHandler() {
    return new MyAccessDeniedHandler();
  }

  /**
   * 加载自定义的token校验过滤器
   *
   * @return 过滤器
   */
  @Bean
  public MyAuthenticationTokenGenericFilter myAuthenticationTokenGenericFilter() {
    return new MyAuthenticationTokenGenericFilter();
  }

  /**
   * 静态资源
   * 不拦截静态资源，所有用户均可访问的资源
   */
  @Override
  public void configure(WebSecurity webSecurity) {
    webSecurity.ignoring().antMatchers("/", "/css/**", "/js/**", "/images/**");
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        // 关闭crsf攻击，允许跨越访问
        .csrf().disable()
        // 自定义验证处理器
        .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler()).and()
        // 不创建HttpSession，不使用HttpSession来获取SecurityContext
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
    httpSecurity.addFilterBefore(myAuthenticationTokenGenericFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}
