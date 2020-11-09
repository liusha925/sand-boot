/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.provider;

import com.sand.core.vo.ResultVO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 功能说明：自定义登录认证方式
 * 开发人员：@author liusha
 * 开发日期：2019/11/26 10:38
 * 功能描述：此认证方式通过了，就可被认为是登录成功，返回一个填充了用户认证信息（包括身份信息、权限信息、详细信息等，但密码通常会被移除）的Authentication实例。
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MyAuthenticationProvider implements AuthenticationProvider {
  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // 与登录认证接口保持一致，此处使用框架自带的UsernamePasswordAuthenticationToken，也可以自行定义
    UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
    // 负责从数据库或者内存映射中加载用户信息，加载方法自行实现
    UserDetails userDetails = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
    if (userDetails == null) {
      throw new UsernameNotFoundException(ResultVO.Code.USERNAME_NOT_FOUND.getName());
    } else if (!userDetails.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException(ResultVO.Code.CREDENTIALS_EXPIRED.getName());
    } else if (!userDetails.isAccountNonExpired()) {
      throw new AccountExpiredException(ResultVO.Code.ACCOUNT_EXPIRED.getName());
    } else if (!userDetails.isEnabled()) {
      throw new DisabledException(ResultVO.Code.DISABLED.getName());
    } else if (!userDetails.isAccountNonLocked()) {
      throw new LockedException(ResultVO.Code.LOCKED.getName());
    }
    //
    UsernamePasswordAuthenticationToken authenticationResult = new UsernamePasswordAuthenticationToken(
        userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    // 设置用户详细信息，用于记录ip、sessionId、证书序列号等值
    authenticationResult.setDetails(authenticationToken.getDetails());
    // 设置权限信息列表，默认是 GrantedAuthority 接口的一些实现类，通常是代表权限信息的一系列字符串
    return authenticationResult;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
