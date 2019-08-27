/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.controller;

import com.sand.base.core.controller.BaseController;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.util.result.ResultUtil;
import com.sand.base.util.tree.Tree;
import com.sand.sys.entity.SysMenu;
import com.sand.sys.service.ISysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @RequestMapping("/tree")
  public ResultEntity tree(@RequestBody SysMenu menu) {
    log.info("page params：{}", menu);
    Tree menuTree = menuService.buildMenuTree();

    return ResultUtil.ok(menuTree.getChildren());
  }

}
