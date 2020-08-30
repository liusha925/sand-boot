/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.controller;

import com.sand.common.vo.ResultVO;
import com.sand.common.util.ParamUtil;
import com.sand.security.service.IUserAuthenticationService;
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
@RequestMapping("/auth")
public class AuthController {
  /**
   * 用户基础服务接口
   */
  @Autowired
  private IUserAuthenticationService userAuthenticationService;

  /**
   * 输入用户名密码，获得token
   *
   * @param param 用户名和密码
   * @return 封装好的token，过期时间，token的类型map
   */
  @RequestMapping(value = "/login")
  public ResultVO login(@RequestParam Map<String, Object> param) {
    String username = ParamUtil.getStringValue(param, "username");
    String password = ParamUtil.getStringValue(param, "password");

    return authentication(param, new UsernamePasswordAuthenticationToken(username, password));
  }

  /**
   * 登录认证
   *
   * @param param               登录信息
   * @param authenticationToken 认证信息
   * @return
   */
  private ResultVO authentication(Map<String, Object> param, AbstractAuthenticationToken authenticationToken) {
    // 1、认证前校验
    userAuthenticationService.beforeValidate(param);
    // 2、处理认证信息
    Object userDetails = userAuthenticationService.handleAuthInfo(authenticationToken);
    // 3、认证后处理
    return userAuthenticationService.authAfter(userDetails);
  }
}
