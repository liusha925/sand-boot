/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sand.common.util.ResultUtil;
import com.sand.common.vo.ResultVO;
import com.sand.mybatisplus.util.PageUtil;
import com.sand.sys.entity.SysUser;
import com.sand.sys.model.SysUserModel;
import com.sand.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能说明：系统用户
 * 开发人员：@author liusha
 * 开发日期：2019/8/27 13:23
 * 功能描述：系统用户
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends SysBaseController {

  @Autowired
  private ISysUserService userService;

  @RequestMapping("/page")
  public ResultVO page(@RequestBody SysUserModel model) {
    log.info("SysUserController page params：{}", model);
    Page<SysUser> userPage = PageUtil.pageParam(model);
    QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
    Page<SysUser> page = (Page<SysUser>) userService.page(userPage, queryWrapper);

    return ResultUtil.ok(page2map(page));
  }

}
