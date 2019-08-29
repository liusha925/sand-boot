/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sand.base.util.tree.Tree;
import com.sand.sys.entity.SysMenu;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 16:08
 * 功能描述：系统菜单
 */
public interface ISysMenuService extends IService<SysMenu> {
  /**
   * 构建菜单树
   *
   * @param needButton 是否需要按钮菜单
   * @return
   */
  Tree buildMenuTree(boolean needButton);

  /**
   * 新增菜单
   *
   * @param menu dto
   * @return
   */
  int add(SysMenu menu);

  /**
   * 修改菜单
   *
   * @param menu dto
   * @return
   */
  int edit(SysMenu menu);
}
