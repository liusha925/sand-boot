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
import com.sand.base.constant.Constant;
import com.sand.base.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 13:38
 * 功能描述：系统菜单
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
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
  private String parentId;

  /**
   * 父菜单名称
   */
  @TableField(exist = false)
  private String parentName;

  /**
   * 菜单名称
   */
  private String menuName;

  /**
   * 菜单类型（M目录 C菜单 F按钮）
   */
  private String menuType;

  /**
   * 菜单URL
   */
  private String menuUrl;

  /**
   * 显示顺序
   */
  private int orderNum;

  /**
   * 打开方式（_item 页签中打开，_blank 新窗口打开，_current 当前窗口打开）
   */
  private String target;

  /**
   * 菜单状态（0显示 1隐藏）
   */
  private String visible;

  /**
   * 权限字符串
   */
  private String permission;

  /**
   * 菜单图标
   */
  private String icon;

  /**
   * 子菜单
   */
  @TableField(exist = false)
  private List<SysMenu> children;
}
