/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.controller;

import com.sand.base.annotation.LogAnnotation;
import com.sand.base.core.controller.BaseController;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.util.ResultUtil;
import com.sand.base.util.tree.Tree;
import com.sand.sys.entity.SysMenu;
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
public class SysMenuController extends BaseController {

  @Autowired
  private ISysMenuService menuService;

  @RequestMapping("/leftTree")
  public ResultEntity leftTree() {
    log.info("SysMenuController leftTree");
    Tree menuTree;
    // TODO 如果是超级管理员则拥有所有菜单权限
    if (true) {
      menuTree = menuService.buildLeftTree(false);
    } else {
      // TODO 其他用户需要根据角色来查询菜单权限
      String[] roleIds = new String[0];
      menuTree = menuService.buildLeftTree(false, roleIds);
    }

    return ResultUtil.ok(menuTree.getChildren());
  }

  @RequestMapping("/tree")
  public ResultEntity tree() {
    log.info("SysMenuController tree");
    Tree menuTree = menuService.buildMenuTree(true);

    return ResultUtil.ok(menuTree.getChildren());
  }

  @RequestMapping("/list")
  public ResultEntity list() {
    log.info("SysMenuController list");
    List<SysMenu> menuList = menuService.getMenuList(true);

    return ResultUtil.ok(menuList);
  }

  @RequestMapping("/add")
  @LogAnnotation(symbol = "系统管理", description = "新增菜单")
  public ResultEntity add(@RequestBody SysMenuModel model) {
    log.info("SysMenuController add params：{}", model);
    menuService.add(model);

    return ResultUtil.ok("新增成功");
  }

  @RequestMapping("/edit")
  public ResultEntity edit(@RequestBody SysMenuModel model) {
    log.info("SysMenuController edit params：{}", model);
    menuService.edit(model);

    return ResultUtil.ok("修改成功");
  }

  @RequestMapping("/getById/{menuId}")
  public ResultEntity getById(@PathVariable String menuId) {
    log.info("SysMenuController getById params：{}", menuId);
    SysMenu dbMenu = menuService.getById(menuId);

    return ResultUtil.ok(dbMenu);
  }

}
