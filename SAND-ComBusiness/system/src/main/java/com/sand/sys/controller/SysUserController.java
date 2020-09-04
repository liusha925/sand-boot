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
import com.sand.sys.handler.LoginHandler;
import com.sand.sys.handler.RegisterHandler;
import com.sand.sys.model.SysUserModel;
import com.sand.sys.service.ISysUserService;
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
  private LoginHandler loginHandler;
  @Autowired
  private RegisterHandler registerHandler;
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
    loginHandler.loginBeforeValid(params);
    // 2、登录逻辑
    Object userDetails = loginHandler.login(params);
    // 3、登录后处理
    Map<String, Object> loginResult = loginHandler.loginAfterHandle(userDetails);

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
    registerHandler.registerBeforeValid(params);
    // 2、注册逻辑
    AuthUser authUser = registerHandler.register(params);
    // 3、注册后处理
    Map<String, Object> registerResult = registerHandler.registerAfterHandle(authUser);

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
