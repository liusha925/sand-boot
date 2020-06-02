/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/5/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.business.parent.util;

import com.sand.common.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * 功能说明：用户认证工具类
 * 开发人员：@author liusha
 * 开发日期：2020/5/27 10:41
 * 功能描述：获取用户认证信息
 */
public class AuthenticationUtil {
  /**
   * 从SecurityContextHolder获取用户认证信息
   *
   * @return 用户认证信息（一般为org.springframework.security.core.userdetails.UserDetails类型）
   */
  public static Object getUser() {
    // 需注意的是，当过滤链执行完时会调用SecurityContextHolder.clearContext()把SecurityContextHolder清空
    // 因此需在自定义过滤器中重新SecurityContextHolder.getContext().setAuthentication(authentication)保存用户认证信息，如MyAuthenticationTokenGenericFilter
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object user = authentication.getPrincipal();
    if (Objects.isNull(user)) {
      throw new BusinessException("用户不存在！");
    }
    return user;
  }
}
