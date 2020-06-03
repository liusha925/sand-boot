/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.security;

import com.sand.common.exception.BusinessException;
import com.sand.common.util.ServletUtil;
import com.sand.sys.entity.SysMenu;
import com.sand.sys.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 功能说明：用户授权服务
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 11:08
 * 功能描述：用于安全授权，判断用户是否有访问权限
 */
@Slf4j
@Component("userAuthorizationService")
public class UserAuthorizationService {
  /**
   * URLs匹配：1、？匹配一个字符；2、*匹配0个或多个字符；3、**匹配0个或多个目录。
   */
  private AntPathMatcher antPathMatcher = new AntPathMatcher();

  /**
   * 校验权限
   *
   * @param authentication 授权信息
   * @param authName       权限名称
   */
  public void hasPermission(Authentication authentication, String authName) {
    log.info("获取的Authentication信息：{}", authentication);
    HttpServletRequest request = ServletUtil.getRequest();
    Object principal = authentication.getPrincipal();
    boolean hasPermission = false;
    if (principal instanceof SysUser) {
      // 读取用户所拥有的权限菜单
      List<SysMenu> menus = ((SysUser) principal).getUserMenus();
      for (SysMenu menu : menus) {
        log.info("(menu.menuUrl={}，request.requestURI={}", menu.getMenuUrl(), request.getRequestURI());
        if (antPathMatcher.match(menu.getMenuUrl(), request.getRequestURI())) {
          hasPermission = true;
          break;
        }
      }
    }
    if (!hasPermission) {
      throw new BusinessException("没有【" + authName + "】的访问权限");
    }
  }

}
