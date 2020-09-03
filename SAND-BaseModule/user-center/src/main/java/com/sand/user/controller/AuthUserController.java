/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.user.controller;

import com.sand.common.util.ParamUtil;
import com.sand.common.vo.ResultVO;
import com.sand.user.service.IAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
   * 用户基础服务接口
   */
  @Autowired
  private IAuthUserService authUserService;

  /**
   * 输入用户名密码，获得token
   *
   * @param params 用户名和密码
   * @return 封装好的token，过期时间，token的类型map
   */
  @RequestMapping(value = "/login")
  public ResultVO login(@RequestParam Map<String, Object> params) {
    String username = ParamUtil.getStringValue(params, "username");
    String password = ParamUtil.getStringValue(params, "password");
    // 1、认证前校验
    authUserService.beforeValidate(params);
    // 2、处理认证信息
    AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
    Object userDetails = authUserService.handleAuthInfo(authenticationToken);
    // 3、认证后处理
    return authUserService.authAfter(userDetails);
  }

  /**
   * 用户注册
   *
   * @param params 注册信息
   * @return 返回注册信息
   */
  @RequestMapping(value = "/register")
  public ResultVO register(@RequestParam Map<String, Object> params) {
    return authUserService.register(params);
  }

}
