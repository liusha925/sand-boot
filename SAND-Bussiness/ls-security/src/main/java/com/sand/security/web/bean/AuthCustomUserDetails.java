/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/25    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web.bean;

import com.sand.base.web.service.IBaseAuthority;
import com.sand.base.web.service.IBaseUserDetails;
import com.sand.security.web.util.AuthorityUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 功能说明：用户权限信息
 * 开发人员：@author liusha
 * 开发日期：2019/11/25 15:07
 * 功能描述：获取用户权限信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthCustomUserDetails implements UserDetails {

  private IBaseUserDetails userDetails;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtil.base2Authority((List<IBaseAuthority>) userDetails.getAuthorities());
  }

  @Override
  public String getPassword() {
    return userDetails.getPassword();
  }

  @Override
  public String getUsername() {
    return userDetails.getUserame();
  }

  @Override
  public boolean isAccountNonExpired() {
    return userDetails.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return userDetails.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return userDetails.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return userDetails.isEnabled();
  }
}
