/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.user.controller;

import com.sand.common.util.ResultUtil;
import com.sand.common.vo.ResultVO;
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
  private IAuthUserLoginService authUserLoginService;
  /**
   * 用户注册服务
   */
  @Autowired
  private IAuthUserRegisterService authUserRegisterService;

  /**
   * 输入用户名密码，获得token
   *
   * @param params 用户名和密码
   * @return 封装好的token，过期时间，token的类型map
   */
  @RequestMapping(value = "/login")
  public ResultVO login(@RequestParam Map<String, Object> params) {
    // 1、登录前校验
    authUserLoginService.loginBeforeValid(params);
    // 2、登录逻辑
    Object userDetails = authUserLoginService.login(params);
    // 3、登录后处理
    Map<String, Object> loginResult = authUserLoginService.loginAfterHandle(userDetails);

    return ResultUtil.ok(loginResult, "登录成功");
  }

  /**
   * 用户注册
   *
   * @param params 注册信息
   * @return 返回注册信息
   */
  @RequestMapping(value = "/register")
  public ResultVO register(@RequestParam Map<String, Object> params) {
    // 1、注册前校验
    authUserRegisterService.registerBeforeValid(params);
    // 2、注册逻辑
    AuthUser authUser = authUserRegisterService.register(params);
    // 3、注册后处理
    Map<String, Object> registerResult = authUserRegisterService.registerAfterHandle(authUser);

    return ResultUtil.ok(registerResult, "注册成功");
  }

}
