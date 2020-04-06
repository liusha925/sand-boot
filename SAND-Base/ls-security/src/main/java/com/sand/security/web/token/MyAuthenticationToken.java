/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 功能说明：提供盐值的加密验证方式
 * 开发人员：@author liusha
 * 开发日期：2019/11/26 14:33
 * 功能描述：提供盐值的加密验证方式
 */
public class MyAuthenticationToken extends AbstractAuthenticationToken {
  /**
   * 获取用户提交的密码凭证，用户输入的密码字符窜，在认证过后通常会被移除，用于保障安全
   */
  private final Object credentials;
  /**
   * 获取用户身份信息，大部分情况下返回的是 UserDetails 接口的实现类，是框架中最常用的接口之一
   */
  private final Object principal;

  public MyAuthenticationToken(Object principal, Object credentials) {
    super(null);
    this.credentials = credentials;
    this.principal = principal;
    super.setAuthenticated(false);
  }

  public MyAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.credentials = credentials;
    this.principal = principal;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    if (authenticated) {
      throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }
    super.setAuthenticated(false);
  }
}
