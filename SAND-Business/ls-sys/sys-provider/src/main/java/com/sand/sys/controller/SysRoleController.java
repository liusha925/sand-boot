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
import com.sand.common.util.ResultUtil;
import com.sand.common.util.poi.ExcelUtil;
import com.sand.common.util.tree.Tree;
import com.sand.common.vo.ResultVO;
import com.sand.mybatisplus.util.PageUtil;
import com.sand.sys.entity.SysRole;
import com.sand.sys.model.SysRoleModel;
import com.sand.sys.service.ISysMenuService;
import com.sand.sys.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * 功能说明：系统角色
 * 开发人员：@author liusha
 * 开发日期：2019/8/30 17:15
 * 功能描述：系统角色
 */
@Slf4j
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends SysBaseController {
  @Autowired
  private ISysRoleService roleService;
  @Autowired
  private ISysMenuService menuService;

  @RequestMapping("/page")
  public ResultVO page(@RequestBody SysRoleModel model) {
    log.info("SysRoleController page params：{}", model);
    Page<SysRole> rolePage = PageUtil.pageParam(model);
    QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
    Page<SysRole> page = (Page<SysRole>) roleService.page(rolePage, queryWrapper);

    return ResultUtil.ok(page2map(page));
  }

  @RequestMapping("/add")
  public ResultVO add(@RequestBody SysRoleModel model) {
    log.info("SysRoleController add params：{}", model);
    roleService.add(model);

    return ResultUtil.ok("新增成功");
  }

  @RequestMapping("/edit")
  public ResultVO edit(@RequestBody SysRoleModel model) {
    log.info("SysRoleController edit params：{}", model);
    roleService.edit(model);

    return ResultUtil.ok("修改成功");
  }

  @RequestMapping("/cancelAuthorize/{roleId}")
  public ResultVO cancelAuthorize(@PathVariable String roleId) {
    log.info("SysRoleController cancelAuthorize params：{}", roleId);
    roleService.cancelAuthorize(roleId);

    return ResultUtil.ok("取消成功");
  }

  @RequestMapping("/reauthorize")
  public ResultVO reauthorize(@RequestBody SysRoleModel model) {
    log.info("SysRoleController cancelAuthorize params：{}", model);
    roleService.reauthorize(model);

    return ResultUtil.ok("授权成功");
  }

  @RequestMapping("/menuTree")
  public ResultVO menuTree(String roleIds) {
    log.info("SysRoleController menuTree roleIds：{}", roleIds);
    Tree menuTree = menuService.buildMenuTree(true, roleIds.split(","));

    return ResultUtil.ok(menuTree.getChildren());
  }

  /**
   * 下载模板
   */
  @RequestMapping("/downTemplate")
  public void downTemplate() {
    ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole.class);
    util.downTemplate("系统角色信息");
  }

  /**
   * 导入数据
   */
  @RequestMapping("/imported")
  public ResultVO imported(MultipartFile file) {
    log.info("SysRoleController imported fileName：{}", Objects.nonNull(file) ? file.getOriginalFilename() : "空文件");
    ExcelUtil<SysRoleModel> util = new ExcelUtil<>(SysRoleModel.class);
    List<SysRoleModel> roleList;
    try {
      roleList = util.imported(file.getInputStream());
    } catch (Exception e) {
      log.error("文件导入异常：{}", e.getMessage());
      return ResultUtil.error("文件导入异常：" + e.getMessage());
    }
    return roleService.imported(roleList);
  }

  /**
   * 导出数据
   */
  @RequestMapping("/export")
  public void export(SysRoleModel model) {
    log.info("SysRoleController export params：{}", model);
    QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
    List<SysRole> roleList = roleService.list(queryWrapper);
    ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole.class);
    util.export("系统角色信息", roleList);
  }
}
