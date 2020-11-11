/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.sand.core.util.ParamUtil;
import com.sand.core.util.ServletUtil;
import com.sand.core.util.crypt.des.DesCryptUtil;
import com.sand.core.util.crypt.md5.Md5Util;
import com.sand.core.util.lang3.DateUtil;
import com.sand.core.util.lang3.StringUtil;
import com.sand.user.entity.AuthUser;
import com.sand.user.mapper.AuthUserMapper;
import com.sand.user.service.IAuthUserLoginService;
import com.sand.user.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 功能说明：用户登录服务
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 16:59
 * 功能描述：用户登录服务，登录三步曲
 */
@Slf4j(topic = "user-login")
@Service
public class AuthUserLoginServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements IAuthUserLoginService {
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
  private AuthenticationManager authenticationManagerBean;
  /**
   * 从application.yml配置文件中读取token配置，如加密密钥，token有效期等值
   */
  @Autowired
  protected JwtTokenUtil jwtTokenUtil;

  @Override
  public void loginBeforeValid(Map<String, Object> params) {
    String username = ParamUtil.getStringValue(params, "username");
    String password = ParamUtil.getStringValue(params, "password");
    AuthUser dbUser = this.getOne(new QueryWrapper<AuthUser>().eq("username", username));
    if (Objects.isNull(dbUser)) {
      throw new UsernameNotFoundException("username not found");
    }
    // 先将前端DES加密的密码解密再做md5加密比对
    String md5Password = Md5Util.encryptStr(DesCryptUtil.decrypt(password));
    if (!md5Password.equals(dbUser.getPassword())) {
      throw new UsernameNotFoundException("password is error");
    }
  }

  @Override
  public Object login(Map<String, Object> params) {
    String username = ParamUtil.getStringValue(params, "username");
    String password = ParamUtil.getStringValue(params, "password");
    AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
    // 1、开始发起认证，认证方式由com.sand.security.provider.MyAuthenticationProvider实现
    final Authentication authentication = authenticationManagerBean.authenticate(authenticationToken);
    // 2、认证成功后，将Authentication设置到SecurityContextHolder容器中
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // 3、获取用户信息，由MyAuthenticationProvider返回用户认证信息（包括身份信息、权限信息、详细信息等，但密码通常会被移除）
    return authentication.getPrincipal();
  }

  @Override
  public Map<String, Object> loginAfterHandle(Object userDetails) {
    AuthUser user = (AuthUser) userDetails;
    AuthUser dbUser = this.getById(user.getUserId());
    String accessToken = jwtTokenUtil.generateToken(dbUser);

    Map<String, Object> loginResult = Maps.newHashMap();
    loginResult.put("access_token", accessToken);
    loginResult.put("user_id", dbUser.getUserId());
    loginResult.put("user_name", dbUser.getUsername());
    loginResult.put("real_name", dbUser.getRealName());
    loginResult.put("authorities", user.getAuthorities());
    loginResult.put("expiration", jwtTokenUtil.getExpiration());
    loginResult.put("token_type", JwtTokenUtil.TOKEN_PREFIX);
    // 记录登录日志
    log.info("用户【" + (StringUtil.isBlank(dbUser.getUsername()) ? "【匿名用户" : dbUser.getUsername()) +
        "】于" + DateUtil.getNow(DateUtil.Format.F1_YYYY_MM_DD_HH_MM_SS_SSS) +
        "通过【" + ServletUtil.getOSAndBrowser() + "】进行登录，IP：" + ServletUtil.getRemoteAddress());

    return loginResult;
  }

}
