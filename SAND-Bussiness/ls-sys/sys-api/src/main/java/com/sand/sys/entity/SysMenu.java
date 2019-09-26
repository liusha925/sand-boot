/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sand.base.annotation.EnumValidAnnotation;
import com.sand.base.constant.Constant;
import com.sand.base.core.entity.BaseEntity;
import com.sand.sys.enums.MenuEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 13:38
 * 功能描述：系统菜单
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(Constant.TABLE_PREFIX_SYS + "menu")
public class SysMenu extends BaseEntity {
  private static final long serialVersionUID = -2854114810573968874L;
  /**
   * 菜单ID
   */
  @TableId
  private String menuId;
  /**
   * 父菜单ID
   */
  @NotBlank(message = "[父级菜单ID]不能为空哟！")
  private String parentId;
  /**
   * 菜单名称
   */
  @NotBlank(message = "[菜单名称]不能为空哟！")
  @Length(max = 64, message = "[菜单名称]不能超过64个字符呢！")
  private String menuName;
  /**
   * 菜单类型（M目录 C菜单 F按钮）
   */
  @NotBlank(message = "[菜单类型]不能为空哟！")
  @EnumValidAnnotation(message = "[菜单类型]不存在！", target = MenuEnum.MenuType.class)
  private String menuType;
  /**
   * 菜单URL
   */
  @Length(max = 128, message = "[菜单URL]不能超过128个字符呢！")
  private String menuUrl;
  /**
   * 显示顺序
   */
  private int orderNum;
  /**
   * 打开方式（_item 页签中打开，_blank 新窗口打开，_current 当前窗口打开）
   */
  @EnumValidAnnotation(message = "[打开方式]不存在！", target = MenuEnum.Target.class)
  private String target;
  /**
   * 菜单状态（0显示 1隐藏）
   */
  @EnumValidAnnotation(message = "[菜单状态]不存在！", target = MenuEnum.Visible.class)
  private String visible;
  /**
   * 权限标识
   */
  @Length(max = 128, message = "[权限标识]不能超过128个字符呢！")
  private String purview;
  /**
   * 菜单图标
   */
  private String icon;

}
