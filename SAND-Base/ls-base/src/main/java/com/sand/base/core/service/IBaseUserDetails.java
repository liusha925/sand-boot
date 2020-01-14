/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/25    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.core.service;

import java.util.Collection;

/**
 * 功能说明：自定义自己的登录方法
 * 开发人员：@author liusha
 * 开发日期：2019/11/25 15:13
 * 功能描述：自定义自己的登录方法
 */
public interface IBaseUserDetails extends IBaseBusinessUserDetails {
  /**
   * 获取用户标识
   *
   * @return
   */
  @Override
  String getUserId();

  /**
   * 获取权限集合
   *
   * @return
   */
  Collection<? extends IBaseAuthority> getAuthorities();

  /**
   * 获取密码
   *
   * @return
   */
  String getPassword();

  /**
   * 获取真实姓名
   *
   * @return
   */
  String getRealName();

  /**
   * 账户是否过期
   *
   * @return
   */
  boolean isAccountNonExpired();

  /**
   * 账户是否锁定
   *
   * @return
   */
  boolean isAccountNonLocked();

  /**
   * isCredentialsNonExpired
   *
   * @return
   */
  boolean isCredentialsNonExpired();

  /**
   * isEnabled
   *
   * @return
   */
  boolean isEnabled();

  /**
   * @return
   */
  default String getSalt() {
    return null;
  }
}
