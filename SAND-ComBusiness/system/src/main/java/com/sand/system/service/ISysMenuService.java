/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sand.core.util.tree.Tree;
import com.sand.system.entity.SysMenu;
import com.sand.system.model.SysMenuModel;

import java.util.List;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 16:08
 * 功能描述：系统菜单
 */
public interface ISysMenuService extends IService<SysMenu> {
  /**
   * 获取菜单列表
   *
   * @param needButton 是否需要按钮菜单
   * @return 菜单列表
   */
  List<SysMenu> getMenuList(boolean needButton);

  /**
   * 构建左侧菜单树
   *
   * @param needButton 是否需要按钮菜单
   * @return 左侧菜单树
   */
  Tree buildLeftTree(boolean needButton);

  /**
   * 构建左侧菜单树
   *
   * @param needButton 是否需要按钮菜单
   * @param roleIds    角色ID集合
   * @return 左侧菜单树
   */
  Tree buildLeftTree(boolean needButton, Object[] roleIds);

  /**
   * 构建菜单树
   *
   * @param needButton 是否需要按钮菜单
   * @return 菜单树
   */
  Tree buildMenuTree(boolean needButton);

  /**
   * 构建菜单树
   *
   * @param needButton 是否需要按钮菜单
   * @param roleIds    角色ID集合
   * @return 菜单树
   */
  Tree buildMenuTree(boolean needButton, Object[] roleIds);

  /**
   * 构建菜单树
   *
   * @param needButton 是否需要按钮菜单
   * @param isAdmin    是否是超级管理员
   * @param isLeft     是否为左侧菜单树
   * @param roleIds    角色ID集合
   * @return 菜单树
   */
  Tree buildMenuTree(boolean needButton, boolean isAdmin, boolean isLeft, Object[] roleIds);

  /**
   * 新增菜单
   *
   * @param model dto
   * @return 新增结果
   */
  int add(SysMenuModel model);

  /**
   * 修改菜单
   *
   * @param model dto
   * @return 修改结果
   */
  int edit(SysMenuModel model);
}
