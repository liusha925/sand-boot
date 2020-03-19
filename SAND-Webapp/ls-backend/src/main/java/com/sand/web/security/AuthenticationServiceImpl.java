/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.security;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.sand.base.util.ResultUtil;
import com.sand.base.web.entity.ResultEntity;
import com.sand.security.web.IUserSecurityService;
import com.sand.sys.entity.SysUser;
import com.sand.web.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 功能说明：用户认证服务
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 10:05
 * 功能描述：用于安全登录认证
 */
@Service
public class AuthenticationServiceImpl implements IUserSecurityService {
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
  @Autowired
  private SecurityConfig securityConfig;

  @Override
  public UserDetails init() {
    return new SysUser();
  }

  @Override
  public void validateUser(Map<String, Object> param) {

  }

  @Override
  public void handleAuthentication(Object userDetails, AbstractAuthenticationToken authenticationToken) {
    // 保存认证信息
    final Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // 保存授权信息
    SysUser user = (SysUser) userDetails;

  }

  @Override
  public ResultEntity handleUser(Object userDetails) {
    SysUser user = (SysUser) userDetails;
    // TODO 1、存储用户信息至redis
    // TODO 2、保存登录日志
    // 3、将信息发送给web端
    // TODO 从数据库里读取权限标识
    String permissions = "AUTH:TOKEN:LOGIN";
    // 生成有效token
   final String accessToken = securityConfig.generateToken(user);
    // 登录信息存储
    Map<String, Object> tokenMap = Maps.newHashMap();
    tokenMap.put("user_id", user.getUserId());
    tokenMap.put("access_token", accessToken);
    tokenMap.put("real_name", user.getRealName());
    tokenMap.put("authorities", user.getAuthorities());
    tokenMap.put("expiration", securityConfig.getExpiration());
    tokenMap.put("token_type", SecurityConfig.TOKEN_TYPE_BEARER);
    tokenMap.put("permissions", new Gson().fromJson(permissions, List.class));
    return ResultUtil.ok(tokenMap);
  }
}
