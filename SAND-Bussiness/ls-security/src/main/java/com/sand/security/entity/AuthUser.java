/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sand.sys.entity.SysMenu;
import com.sand.sys.entity.SysRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 14:21
 * 功能描述：对应用户信息表auth_user
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("auth_" + "user")
public class AuthUser implements UserDetails {
  /**
   * 菜单ID
   */
  @TableId
  private String userId;
  /**
   * 用户名
   */
  private String username;
  /**
   * 密码
   */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  /**
   * 用户角色集合
   */
  @TableField(exist = false)
  private List<SysRole> userRoles;
  /**
   * 角色菜单集合
   */
  @TableField(exist = false)
  private List<SysMenu> roleMenus;
  /**
   * 用户权限集合
   */
  @TableField(exist = false)
  private Collection<? extends GrantedAuthority> authorities;

  public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, List<SysMenu> roleMenus) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.roleMenus = roleMenus;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
