/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/23    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.security.config;

import com.sand.security.config.WebSecurityConfig;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

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
   * 自定义配置系统需要的URL白名单
   *
   * @param httpSecurity 安全配置
   * @throws Exception Exception
   */
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    super.configure(httpSecurity);
    httpSecurity.authorizeRequests()
        // 允许验证码接口访问
        .antMatchers("/valid/code/*").permitAll()
        // 允许登录、注册接口post访问
        .antMatchers(HttpMethod.POST, "/auth/user/login", "/auth/user/register").permitAll();
//        // 调试可以开放【全部URL白名单】
//        .antMatchers("/**/*").permitAll()
//        // 调试可以开放【任何尚未匹配的URL只需要验证用户即可访问】
//        .anyRequest().authenticated();
  }
}
