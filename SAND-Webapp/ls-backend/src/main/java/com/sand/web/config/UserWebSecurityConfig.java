/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/23    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.config;

import com.sand.security.web.config.WebSecurityConfig;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 功能说明：Spring Security配置
 * 开发人员：@author liusha
 * 开发日期：2020/3/23 17:49
 * 功能描述：处理一些个性化的配置问题
 */
public class UserWebSecurityConfig extends WebSecurityConfig {
  /**
   * 自定义配置系统需要的URL白名单
   *
   * @param httpSecurity 安全配置
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    super.configure(httpSecurity);
    httpSecurity.authorizeRequests()
        .antMatchers("/**/*")
        .authenticated().and();
  }
}
