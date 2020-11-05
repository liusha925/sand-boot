/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sand.common.util.ResultUtil;
import com.sand.common.vo.ResultVO;
import com.sand.mybatisplus.util.PageUtil;
import com.sand.system.entity.SysUser;
import com.sand.system.sercurity.LoginService;
import com.sand.system.sercurity.RegisterService;
import com.sand.system.model.SysUserModel;
import com.sand.system.service.ISysUserService;
import com.sand.user.entity.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
  private LoginService loginService;
  @Autowired
  private RegisterService registerService;
  @Autowired
  private ISysUserService sysUserService;

  /**
   * 用户登录
   *
   * @param params 登录参数
   * @return 登录结果
   */
  @RequestMapping(value = "/login")
  public ResultVO login(@RequestParam Map<String, Object> params) {
    // 1、登录前校验
    loginService.loginBeforeValid(params);
    // 2、登录逻辑
    Object userDetails = loginService.login(params);
    // 3、登录后处理
    Map<String, Object> loginResult = loginService.loginAfterHandle(userDetails);

    return ResultUtil.ok(loginResult, "登录成功");
  }

  /**
   * 用户注册
   *
   * @param params 注册信息
   * @return 注册结果
   */
  @RequestMapping(value = "/register")
  public ResultVO register(@RequestParam Map<String, Object> params) {
    // 1、注册前校验
    registerService.registerBeforeValid(params);
    // 2、注册逻辑
    AuthUser authUser = registerService.register(params);
    // 3、注册后处理
    Map<String, Object> registerResult = registerService.registerAfterHandle(authUser);

    return ResultUtil.ok(registerResult, "注册成功");
  }

  /**
   * 分页查询
   *
   * @param model 查询参数
   * @return 查询结果
   */
  @RequestMapping("/page")
  public ResultVO page(@RequestBody SysUserModel model) {
    log.info("SysUserController page params：{}", model);
    Page<SysUser> userPage = PageUtil.pageParam(model);
    QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
    Page<SysUser> page = (Page<SysUser>) sysUserService.page(userPage, queryWrapper);
    // 查找角色信息
    sysUserService.findRoleInfo(page.getRecords());

    return ResultUtil.ok(PageUtil.page2map(page));
  }

}
