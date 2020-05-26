/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.sand.common.vo.ResultVO;
import com.sand.common.exception.BusinessException;
import com.sand.common.util.ParamUtil;
import com.sand.common.util.ResultUtil;
import com.sand.common.util.crypt.des.DesCryptUtil;
import com.sand.common.util.crypt.md5.Md5Util;
import com.sand.security.web.IUserAuthenticationService;
import com.sand.sys.entity.SysUser;
import com.sand.sys.service.ISysUserService;
import com.sand.web.config.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 功能说明：用户认证服务
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 10:05
 * 功能描述：用于安全登录认证：1、认证前校验；2、处理认证信息；3、认证后处理
 */
@Slf4j
@Component
public class UserAuthenticationService implements IUserAuthenticationService {
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
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private ISysUserService userService;

  @Override
  public void beforeValidate(Map<String, Object> param) {
    log.info("1、认证前校验");
    String username = ParamUtil.getStringValue(param, "username");
    String password = ParamUtil.getStringValue(param, "password");
    SysUser dbUser = userService.getOne(new QueryWrapper<SysUser>().eq("username", username));
    if (Objects.isNull(dbUser)) {
      log.info("{}用户不存在！", username);
      throw new UsernameNotFoundException("username not found");
    }
    // 先将前端DES加密的密码解密再做md5加密比对
    String md5Password = Md5Util.encryptStr(DesCryptUtil.decrypt(password));
    if (!md5Password.equals(dbUser.getPassword())) {
      log.info("{}用户密码错误！", username);
      throw new UsernameNotFoundException("password is error");
    }
  }

  @Override
  public Object handleAuthInfo(AbstractAuthenticationToken authenticationToken) {
    log.info("2、处理认证信息");
    // 1、开始发起认证，认证方式由com.sand.security.web.provider.MyAuthenticationProvider实现
    final Authentication authentication = authenticationManagerBean.authenticate(authenticationToken);
    // 2、认证成功后，将Authentication设置到SecurityContextHolder容器中
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // 3、获取用户信息，由MyAuthenticationProvider返回用户认证信息（包括身份信息、权限信息、详细信息等，但密码通常会被移除）
    return authentication.getPrincipal();
  }

  @Override
  public ResultVO authAfter(Object userDetails) {
    log.info("3、认证后处理");
    SysUser user = (SysUser) userDetails;
    // 1、存储用户信息至redis
    SysUser dbUser = userService.getById(user.getUserId());
    String accessToken = jwtTokenUtil.generateToken(dbUser);
    jwtTokenUtil.putUserToken(dbUser, accessToken);
    jwtTokenUtil.putUserDetail(dbUser);
    // TODO 2、保存登录日志
    // 3、将信息返回web端
    Map<String, Object> tokenMap = Maps.newHashMap();
    tokenMap.put("access_token", accessToken);
    tokenMap.put("user_id", dbUser.getUserId());
    tokenMap.put("real_name", dbUser.getRealName());
    tokenMap.put("authorities", user.getAuthorities());
    tokenMap.put("expiration", jwtTokenUtil.getExpiration());
    tokenMap.put("token_type", JwtTokenUtil.TOKEN_PREFIX);
    return ResultUtil.ok(tokenMap);
  }

  @Override
  public void checkAuthToken(String token) {
    boolean result = jwtTokenUtil.checkTokenEffective(token);
    if (!result) {
      throw new BusinessException(ResultVO.Code.TOKEN_FAIL);
    }
  }
}
