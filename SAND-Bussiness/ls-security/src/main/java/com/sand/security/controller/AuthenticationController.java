/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.controller;

import com.google.gson.Gson;
import com.sand.base.annotation.LogAnnotation;
import com.sand.base.core.controller.BaseController;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.core.service.IBaseUserDetailsService;
import com.sand.base.util.ParamUtil;
import com.sand.base.util.ResultUtil;
import com.sand.base.util.crypt.des.DesCryptUtil;
import com.sand.security.web.bean.AuthCustomUserDetails;
import com.sand.security.entity.AuthUser;
import com.sand.security.web.util.AbstractTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：登录认证授权
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 15:57
 * 功能描述：登录认证授权
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController extends BaseController {
  /**
   * Token工具
   */
  @Autowired
  private AbstractTokenUtil jwtTokenUtil;
  /**
   * 用户基础服务接口
   */
  @Autowired
  private IBaseUserDetailsService baseUserDetailsService;
  /**
   * AuthenticationManager 接口是认证相关的核心接口，也是发起认证的入口。
   * 但它一般不直接认证，其常用实现类 ProviderManager 内部会维护一个 List<AuthenticationProvider> 列表，
   * 存放里多种认证方式，默认情况下，只需要通过一个 AuthenticationProvider 的认证，就可被认为是登录成功。
   * <p>
   * 负责验证、认证成功后，返回一个填充了用户认证信息（包括权限信息、身份信息、详细信息等，但密码通常会被移除）的 Authentication 实例。
   * 然后再将 Authentication 设置到 SecurityContextHolder 容器中。
   */
  @Autowired
  private AuthenticationManager authenticationManager;

  /**
   * 输入用户名密码，获得token
   *
   * @param param
   * @return 封装好的token，过期时间，token的类型map
   */
  @PostMapping(value = "/token/login")
  @LogAnnotation(description = "用户登录")
  public ResultEntity tokenLogin(@RequestBody Map<String, Object> param) {
    String username = ParamUtil.getStringValue(param, "username");
    String password = ParamUtil.getStringValue(param, "password");
    password = DesCryptUtil.decrypt(password);

    return authentication(param, new UsernamePasswordAuthenticationToken(username, password));
  }

  /**
   * 用户名密码登录方式
   *
   * @param param
   * @param authenticationToken
   * @return
   */
  public ResultEntity authentication(Map<String, Object> param, AbstractAuthenticationToken authenticationToken) {
    // 1、登录前校验
    baseUserDetailsService.validateUser(param);
    // 获取认证信息
    final Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    final AuthCustomUserDetails userDetails = (AuthCustomUserDetails) authentication.getPrincipal();
    // 生成有效token
    final String accessToken = jwtTokenUtil.generateToken(userDetails.getUserDetails());
    AuthUser user = (AuthUser) userDetails.getUserDetails();
    // 基础权限获取
    baseUserService.setAuthorities(user);
    // 3、登录后处理
    baseUserDetailsService.handleUser(user);
    // TODO 登录日志
    // TODO 从数据库里读取权限标识
    String permissions = "AUTH:TOKEN:LOGIN";
    // 登录信息存储
    Map<String, Object> tokenMap = new HashMap<>();
    tokenMap.put("userId", user.getUserId());
    tokenMap.put("access_token", accessToken);
    tokenMap.put("expiration", jwtTokenUtil.getExpiration());
    tokenMap.put("token_type", AbstractTokenUtil.TOKEN_TYPE_BEARER);
    tokenMap.put("realName", userDetails.getRealName());
    tokenMap.put("customAuthorities", userDetails.getAuthorities());
    tokenMap.put("permissions", new Gson().fromJson(permissions, List.class));
    return ResultUtil.ok(tokenMap);
  }
}
