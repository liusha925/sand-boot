/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/23    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.security.config;

import com.sand.security.config.WebSecurityConfig;
import com.sand.security.provider.MyAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 功能说明：Spring Security配置
 * 开发人员：@author liusha
 * 开发日期：2020/3/23 17:49
 * 功能描述：处理一些个性化的配置问题
 * 方法级安全注解 @EnableGlobalMethodSecurity
 * prePostEnabled：决定Spring Security的前注解是否可用 [@PreAuthorize,@PostAuthorize,..]
 * secureEnabled：决定是否Spring Security的保障注解 [@Secured] 是否可用
 * jsr250Enabled：决定 JSR-250 annotations 注解[@RolesAllowed..] 是否可用.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class UserWebSecurityConfig extends WebSecurityConfig {
  /**
   * 用户信息服务
   */
  @Autowired
  protected UserDetailsService userDetailsLoader;

  /**
   * 密码验证方式
   * 将用户信息和密码加密方式进行注入
   *
   * @param auth 授权信息
   * @throws Exception Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsLoader)
        .passwordEncoder(passwordEncoder());
    // 关闭密码验证方式
//        .passwordEncoder(NoOpPasswordEncoder.getInstance());
  }

  /**
   * anyRequest          |   匹配所有请求路径
   * access              |   SpringEl表达式结果为true时可以访问
   * anonymous           |   匿名可以访问
   * denyAll             |   用户不能访问
   * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
   * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
   * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
   * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
   * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
   * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
   * permitAll           |   用户可以任意访问
   * rememberMe          |   允许通过remember-me登录的用户访问
   * authenticated       |   用户登录后可访问
   *
   * @param httpSecurity 安全配置
   * @throws Exception Exception
   */
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    super.configure(httpSecurity);

    MyAuthenticationProvider authenticationProvider = new MyAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsLoader);
    httpSecurity
        // 自定义登录认证方式
        .authenticationProvider(authenticationProvider)
        .authorizeRequests()
        // 允许验证码接口访问
        .antMatchers("/valid/code/*").permitAll()
        // 允许登录、注册接口post访问
        .antMatchers(HttpMethod.POST, "/auth/user/login", "/auth/user/register").permitAll()
        // 允许登录、注册接口post访问
        .antMatchers(HttpMethod.POST, "/sys/user/login", "/sys/user/register").permitAll()
//        // 调试可以开放【全部URL白名单】
//        .antMatchers("/**/*").permitAll()
        // 除上面外的所有请求全部需要鉴权认证，即登录后才能访问（这行一定要加，否则鉴权报错，<==>.antMatchers("/**/*").authenticated().and()）
        .anyRequest().authenticated().and();
  }
}
