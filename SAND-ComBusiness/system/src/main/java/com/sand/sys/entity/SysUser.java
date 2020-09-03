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
import com.sand.common.base.BaseEntity;
import com.sand.common.constant.Constant;
import com.sand.user.entity.AuthUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 13:38
 * 功能描述：系统菜单
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(Constant.TABLE_SYS + "user")
public class SysUser extends BaseEntity {
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
   * 真实姓名
   */
  @NotBlank(message = "[真实姓名]不能为空哟！")
  @Length(max = 64, message = "[真实姓名]不能超过64个字符呢！")
  private String realName;
  /**
   * 证件号码
   */
  @Length(max = 32, message = "[证件号码]不能超过32个字符呢！")
  private String idNumber;
  /**
   * 电话
   */
  @Length(max = 16, message = "[电话]不能超过16个字符呢！")
  private String phone;
  /**
   * 邮箱
   */
  @Length(max = 32, message = "[邮箱]不能超过32个字符呢！")
  private String email;
  /**
   * 证件类型（0身份证）
   */
  private int idType;
  /**
   * 是否为超级管理员
   */
  private int isAdmin;
  /**
   * 认证信息
   */
  @TableField(exist = false)
  private AuthUser authUser;
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
}
