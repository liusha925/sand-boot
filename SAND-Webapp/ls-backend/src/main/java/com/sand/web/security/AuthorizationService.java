/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.security;

import com.sand.base.exception.LsException;
import com.sand.base.util.ServletUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能说明：用户授权服务
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 11:08
 * 功能描述：用于安全授权
 */
@Component("authorizationService")
public class AuthorizationService {

  /**
   * 校验权限
   *
   * @param authentication 授权信息
   * @param permission     权限标识
   * @param name           权限名称
   * @return true-有权访问；false-无权访问
   */
  public void checkPermission(Authentication authentication, String permission, String name) {
    boolean flag = hasPermission(authentication, permission);
    if (!flag) {
      throw new LsException("没有【" + name + "】的访问权限");
    }
  }

  /**
   * 遍历权限
   *
   * @param authentication 授权信息
   * @param permission     权限标识
   * @return true-有权访问；false-无权访问
   */
  private boolean hasPermission(Authentication authentication, String permission) {
//    String username = (String) authentication.getPrincipal();
    System.out.println("获取的用户信息：" + authentication);
    HttpServletRequest request = ServletUtil.getRequest();
    // TODO 缓存的用户权限：标识
    String permissions = (String) request.getAttribute("USER_UNIQUE_ID");
    if (permission.equals(permissions)) {
      return true;
    }
    return false;
  }
}
