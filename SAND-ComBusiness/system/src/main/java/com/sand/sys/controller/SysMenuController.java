/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.controller;

import com.sand.common.constant.Constant;
import com.sand.common.util.ResultUtil;
import com.sand.common.util.tree.Tree;
import com.sand.common.vo.ResultVO;
import com.sand.log.annotation.LogAnnotation;
import com.sand.sys.entity.SysMenu;
import com.sand.sys.entity.SysUser;
import com.sand.sys.model.SysMenuModel;
import com.sand.sys.service.ISysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/27 13:23
 * 功能描述：系统菜单
 */
@Slf4j
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends SysBaseController {

  @Autowired
  private ISysMenuService menuService;

  @RequestMapping("/leftTree")
  public ResultVO leftTree() {
    log.info("SysMenuController leftTree");
    Tree leftTree = this.getMenuTree(false);

    return ResultUtil.ok(leftTree.getChildren());
  }

  @RequestMapping("/tree")
  public ResultVO tree() {
    log.info("SysMenuController tree");
    Tree menuTree = this.getMenuTree(true);

    return ResultUtil.ok(menuTree.getChildren());
  }

  @RequestMapping("/list")
  public ResultVO list() {
    log.info("SysMenuController list");
    List<SysMenu> menuList = menuService.getMenuList(true);

    return ResultUtil.ok(menuList);
  }

  @RequestMapping("/add")
  @LogAnnotation(symbol = "系统管理", description = "新增菜单", service = Constant.SYS_LOG_SERVICE)
  public ResultVO add(@RequestBody SysMenuModel model) {
    log.info("SysMenuController add params：{}", model);
    menuService.add(model);

    return ResultUtil.ok("新增成功");
  }

  @RequestMapping("/edit")
  @LogAnnotation(symbol = "系统管理", description = "编辑菜单", service = Constant.SYS_LOG_SERVICE)
  public ResultVO edit(@RequestBody SysMenuModel model) {
    log.info("SysMenuController edit params：{}", model);
    menuService.edit(model);

    return ResultUtil.ok("修改成功");
  }

  @RequestMapping("/getById/{menuId}")
  public ResultVO getById(@PathVariable String menuId) {
    log.info("SysMenuController getById params：{}", menuId);
    SysMenu dbMenu = menuService.getById(menuId);

    return ResultUtil.ok(dbMenu);
  }

  /**
   * 获取菜单树
   *
   * @param needButton 是否需要按钮菜单
   * @return 菜单树
   */
  private Tree getMenuTree(boolean needButton) {
    Tree menuTree;
    SysUser sysUser = super.getSysUser();
    // 如果是超级管理员则拥有所有菜单权限
    if (sysUser.isAdmin()) {
      menuTree = menuService.buildLeftTree(needButton);
    } else {
      // 其他用户需要根据角色来查询菜单权限
      menuTree = menuService.buildLeftTree(needButton, getRoleIds());
    }
    return menuTree;
  }

}
