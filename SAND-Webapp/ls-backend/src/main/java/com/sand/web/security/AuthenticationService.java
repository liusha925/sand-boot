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
import com.sand.security.web.IUserAuthenticationService;
import com.sand.sys.entity.SysUser;
import com.sand.web.config.TokenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 功能说明：用户认证服务
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 10:05
 * 功能描述：用于安全登录认证：1、认证前校验；2、处理认证信息；3、认证后处理
 */
@Slf4j
@Component
public class AuthenticationService implements IUserAuthenticationService {
  /**
   * AuthenticationManager 接口是认证相关的核心接口，也是发起认证的入口。
   * 但它一般不直接认证，其常用实现类ProviderManager内部会维护一个List<AuthenticationProvider>认证列表，
   * 存放里多种认证方式，默认情况下，只需要通过一个AuthenticationProvider的认证，就可被认为是登录成功。
   * 此系统认证方式由com.sand.security.web.provider.SaltAuthenticationProvider实现。
   * <p>
   * 负责验证、认证成功后，返回一个填充了用户认证信息（包括身份信息、权限信息、详细信息等，但密码通常会被移除）的Authentication实例。
   * 然后再将Authentication设置到SecurityContextHolder容器中。
   */
  @Autowired
  private AuthenticationManager authenticationManager;
  /**
   * 从application.yml配置文件中读取token配置，如加密密钥，token有效期等值
   */
  @Autowired
  private TokenConfig tokenConfig;

  @Override
  public void validateUser(Map<String, Object> param) {
    log.info("1、认证前校验");
  }

  @Override
  public Object handleAuthentication(AbstractAuthenticationToken authenticationToken) {
    log.info("2、处理认证信息");
    // 1、开始发起认证，认证方式由com.sand.security.web.provider.SaltAuthenticationProvider实现
    final Authentication authentication = authenticationManager.authenticate(authenticationToken);
    // 2、认证成功后，将Authentication设置到SecurityContextHolder容器中
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // 3、获取用户信息，由SaltAuthenticationProvider返回用户认证信息（包括身份信息、权限信息、详细信息等，但密码通常会被移除）
    return authentication.getPrincipal();
  }

  @Override
  public ResultEntity handleUser(Object userDetails) {
    log.info("3、认证后处理");
    SysUser user = (SysUser) userDetails;
    // TODO 1、存储用户信息至redis
    // TODO 2、保存登录日志
    // 3、将信息返回web端
    // TODO 从redis或数据库里读取权限标识
    String permissions = "AUTH:TOKEN:LOGIN";
    // 生成有效token
    final String accessToken = tokenConfig.generateToken(user);
    // 登录信息存储
    Map<String, Object> tokenMap = Maps.newHashMap();
    tokenMap.put("user_id", user.getUserId());
    tokenMap.put("access_token", accessToken);
    tokenMap.put("real_name", user.getRealName());
    tokenMap.put("authorities", user.getAuthorities());
    tokenMap.put("expiration", tokenConfig.getExpiration());
    tokenMap.put("token_type", TokenConfig.TOKEN_TYPE_BEARER);
//    tokenMap.put("permissions", new Gson().fromJson(permissions, List.class));
    return ResultUtil.ok(tokenMap);
  }
}
