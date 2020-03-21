/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.controller;

import com.sand.base.util.ParamUtil;
import com.sand.base.web.controller.BaseController;
import com.sand.base.web.entity.ResultEntity;
import com.sand.security.web.IUserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 功能说明：登录认证授权
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 15:57
 * 功能描述：登录认证授权
 */
@RestController
@RequestMapping("/security")
public class SecurityController extends BaseController {
  /**
   * 用户基础服务接口
   */
  @Autowired
  private IUserSecurityService userSecurityService;

  /**
   * 输入用户名密码，获得token
   *
   * @param param
   * @return 封装好的token，过期时间，token的类型map
   */
  @PostMapping(value = "/login")
  public ResultEntity login(@RequestBody Map<String, Object> param) {
    String username = ParamUtil.getStringValue(param, "username");
    String password = ParamUtil.getStringValue(param, "password");

    return authentication(param, new UsernamePasswordAuthenticationToken(username, password));
  }

  /**
   * 安全登录
   *
   * @param param               登录信息
   * @param authenticationToken 认证信息
   * @return
   */
  private ResultEntity authentication(Map<String, Object> param, AbstractAuthenticationToken authenticationToken) {
    // 1、认证前校验
    userSecurityService.validateUser(param);
    // 2、处理认证信息
    Object userDetails = userSecurityService.handleAuthentication(authenticationToken);
    // 3、认证后处理
    return userSecurityService.handleUser(userDetails);
  }
}
