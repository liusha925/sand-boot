/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sand.common.base.BaseEntity;
import com.sand.common.constant.Constant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 13:38
 * 功能描述：系统菜单
 */
@Getter
@Setter
@ToString(exclude = {"password"}, callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(Constant.TABLE_SYS + "user")
public class SysUser extends BaseEntity implements UserDetails {
  private static final long serialVersionUID = -8324234380114962669L;
  /**
   * 菜单ID
   */
  @TableId
  private String userId;
  /**
   * 用户名
   */
  @NotBlank(message = "[用户名]不能为空哟！")
  @Length(max = 16, message = "[用户名]不能超过16个字符呢！")
  private String username;
  /**
   * 密码
   */
  @Length(max = 64, message = "[密码]加密超过了限制长度64！")
  private String password;
  /**
   * 真实姓名
   */
  @NotBlank(message = "[真实姓名]不能为空哟！")
  @Length(max = 64, message = "[真实姓名]不能超过64个字符呢！")
  private String realName;
  /**
   * 是否为超级管理员
   */
  private boolean isAdmin;
  /**
   * 用户权限集合
   */
  @TableField(exist = false)
  private List<SysRole> userRoles;
  /**
   * 用户菜单集合
   */
  @TableField(exist = false)
  private List<SysMenu> userMenus;
  /**
   * 用户权限
   */
  @JsonIgnore
  @TableField(exist = false)
  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
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
