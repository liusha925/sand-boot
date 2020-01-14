/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web.provider;

import com.sand.base.enums.CodeEnum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 功能说明：自定义盐值加密方式
 * 开发人员：@author liusha
 * 开发日期：2019/11/26 10:38
 * 功能描述：自定义盐值加密方式
 */
@Data
public class SaltAuthenticationProvider implements AuthenticationProvider {
  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails userDetails = userDetailsService.loadUserByUsername("");
    if (userDetails == null) {
      throw new UsernameNotFoundException(CodeEnum.USERNAME_NOT_FOUND.getMsg());
    } else if (!userDetails.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException(CodeEnum.CREDENTIALS_EXPIRED.getMsg());
    } else if (!userDetails.isAccountNonExpired()) {
      throw new AccountExpiredException(CodeEnum.ACCOUNT_EXPIRED.getMsg());
    } else if (!userDetails.isEnabled()) {
      throw new DisabledException(CodeEnum.DISABLED.getMsg());
    } else if (!userDetails.isAccountNonLocked()) {
      throw new LockedException(CodeEnum.LOCKED.getMsg());
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return false;
  }
}
