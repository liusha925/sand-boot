/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.base.util.tree.Tree;
import com.sand.base.util.tree.TreeUtil;
import com.sand.base.util.tree.builder.ITreeBuilder;
import com.sand.sys.entity.SysMenu;
import com.sand.sys.mapper.SysMenuMapper;
import com.sand.sys.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 16:12
 * 功能描述：系统菜单
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
  @Override
  public Tree buildMenuTree() {
    List<SysMenu> menuList = this.list();
    Tree menuTree = buildTree(menuList);
    TreeUtil.addRoot(menuTree, "菜单树");
    return menuTree;
  }

  /**
   * 构建树
   *
   * @param menuList
   * @return
   */
  private Tree buildTree(List<SysMenu> menuList) {
    return new Tree().buildTree(menuList, new ITreeBuilder<SysMenu>() {
      @Override
      public String getId(SysMenu menu) {
        return menu.getMenuId();
      }

      @Override
      public String getPid(SysMenu menu) {
        return menu.getParentId();
      }

      @Override
      public String getName(SysMenu menu) {
        return menu.getMenuName();
      }
    });
  }
}
