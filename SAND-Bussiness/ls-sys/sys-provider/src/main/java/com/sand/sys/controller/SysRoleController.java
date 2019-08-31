/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sand.base.core.controller.BaseController;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.util.ParamUtil;
import com.sand.base.util.ResultUtil;
import com.sand.sys.entity.SysRole;
import com.sand.sys.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能说明：系统角色
 * 开发人员：@author liusha
 * 开发日期：2019/8/30 17:15
 * 功能描述：写系统角色
 */
@Slf4j
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

  @Autowired
  ISysRoleService roleService;

  @RequestMapping("/page")
  public ResultEntity page(@RequestBody SysRole role) {
    log.info("SysRoleController page：{}", role);
    Page<SysRole> rolePage = ParamUtil.pageParam(role);
    QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
    Page<SysRole> page = (Page<SysRole>) roleService.page(rolePage, queryWrapper);
    System.out.println(page);

    return ResultUtil.ok(page2map(page));
  }
}
