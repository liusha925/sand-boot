/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.user.controller;

import com.sand.core.util.ResultUtil;
import com.sand.core.vo.ResultVO;
import com.sand.user.entity.AuthUser;
import com.sand.user.service.IAuthUserLoginService;
import com.sand.user.service.IAuthUserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 功能说明：登录认证授权
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 15:57
 * 功能描述：登录认证授权
 */
@RestController
@RequestMapping("/auth/user")
public class AuthUserController {
  /**
   * 用户登录服务
   */
  @Autowired
  private IAuthUserLoginService authUserLoginServiceImpl;
  /**
   * 用户注册服务
   */
  @Autowired
  private IAuthUserRegisterService authUserRegisterServiceImpl;

  /**
   * 用户登录
   *
   * @param params 登录参数
   * @return 登录结果
   */
  @RequestMapping(value = "/login")
  public ResultVO login(@RequestParam Map<String, Object> params) {
    // 1、登录前校验
    authUserLoginServiceImpl.loginBeforeValid(params);
    // 2、登录逻辑
    Object userDetails = authUserLoginServiceImpl.login(params);
    // 3、登录后处理
    Map<String, Object> loginResult = authUserLoginServiceImpl.loginAfterHandle(userDetails);

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
    authUserRegisterServiceImpl.registerBeforeValid(params);
    // 2、注册逻辑
    AuthUser authUser = authUserRegisterServiceImpl.register(params);
    // 3、注册后处理
    Map<String, Object> registerResult = authUserRegisterServiceImpl.registerAfterHandle(authUser);

    return ResultUtil.ok(registerResult, "注册成功");
  }

}
