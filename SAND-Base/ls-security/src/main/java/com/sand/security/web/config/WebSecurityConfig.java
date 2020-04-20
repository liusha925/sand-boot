/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web.config;

import com.sand.security.web.filter.MyAuthenticationTokenGenericFilter;
import com.sand.security.web.handler.MyAccessDeniedHandler;
import com.sand.security.web.provider.MyAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 功能说明：自定义Spring Security配置
 * 开发人员：@author liusha
 * 开发日期：2019/11/26 10:34
 * 功能描述：安全认证基础配置，开启 Spring Security
 * 方法级安全注解 @EnableGlobalMethodSecurity
 * prePostEnabled：决定Spring Security的前注解是否可用 [@PreAuthorize,@PostAuthorize,..]
 * secureEnabled：决定是否Spring Security的保障注解 [@Secured] 是否可用
 * jsr250Enabled：决定 JSR-250 annotations 注解[@RolesAllowed..] 是否可用.
 */
@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  /**
   * 用户信息服务
   */
  @Autowired
  private UserDetailsService userDetailsService;

  /**
   * 认证管理器：使用spring自带的验证密码的流程
   * <p>
   * 负责验证、认证成功后，AuthenticationManager 返回一个填充了用户认证信息（包括权限信息、身份信息、详细信息等，但密码通常会被移除）的 Authentication 实例。
   * 然后再将 Authentication 设置到 SecurityContextHolder 容器中。
   * AuthenticationManager 接口是认证相关的核心接口，也是发起认证的入口。
   * 但它一般不直接认证，其常用实现类 ProviderManager 内部会维护一个 List<AuthenticationProvider> 列表，
   * 存放里多种认证方式，默认情况下，只需要通过一个 AuthenticationProvider 的认证，就可被认为是登录成功
   *
   * @return
   * @throws Exception
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
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(6);
  }

  /**
   * 加载自定义的验证失败处理方式
   *
   * @return
   */
  @Bean
  public MyAccessDeniedHandler myAccessDeniedHandler() {
    return new MyAccessDeniedHandler();
  }

  /**
   * 加载自定义的token校验过滤器
   *
   * @return
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

  /**
   * 密码验证方式
   * 将用户信息和密码加密方式进行注入
   *
   * @param auth
   * @throws Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
    // 关闭密码验证方式
//        .passwordEncoder(NoOpPasswordEncoder.getInstance());
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    MyAuthenticationProvider authenticationProvider = new MyAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    httpSecurity
        // 关闭crsf攻击，允许跨越访问
        .csrf().disable()
        // 自定义登录认证方式
        .authenticationProvider(authenticationProvider)
        // 自定义验证处理器
        .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler()).and()
        // 不创建HttpSession，不使用HttpSession来获取SecurityContext
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        // 允许登录接口post访问
        .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
        // 允许验证码接口post访问
        .antMatchers(HttpMethod.POST, "/valid/code/*").permitAll().and();
//        // 任何尚未匹配的URL只需要验证用户即可访问
//        .anyRequest().authenticated()
    httpSecurity.addFilterBefore(myAuthenticationTokenGenericFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}
