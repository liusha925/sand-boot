/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sand.core.exception.BusinessException;
import com.sand.core.util.ServletUtil;
import com.sand.core.util.lang3.StringUtil;
import com.sand.core.vo.ResultVO;
import com.sand.security.handler.IUserAuthorizationHandler;
import com.sand.security.util.SecurityUtil;
import com.sand.system.entity.SysMenu;
import com.sand.system.entity.SysRoleMenu;
import com.sand.system.enums.MenuEnum;
import com.sand.system.sercurity.LoginService;
import com.sand.system.service.ISysMenuService;
import com.sand.system.service.ISysRoleMenuService;
import com.sand.system.service.ISysUserRoleService;
import com.sand.user.entity.AuthUser;
import com.sand.user.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能说明：用户授权服务
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 11:08
 * 功能描述：用于安全授权，判断用户是否有访问权限
 */
@Slf4j
@Component("userAuthorizationService")
public class UserAuthorizationService implements IUserAuthorizationHandler {
  /**
   * 从application.yml配置文件中读取token配置，如加密密钥，token有效期等值
   */
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  /**
   * 用户登录服务
   */
  @Autowired
  private LoginService loginService;
  /**
   * 用户-角色服务
   */
  @Autowired
  private ISysUserRoleService userRoleService;
  /**
   * 角色-菜单服务
   */
  @Autowired
  private ISysRoleMenuService roleMenuService;
  /**
   * 菜单服务
   */
  @Autowired
  private ISysMenuService sysMenuService;
  /**
   * URLs匹配：1、？匹配一个字符；2、*匹配0个或多个字符；3、**匹配0个或多个目录。
   */
  private AntPathMatcher antPathMatcher = new AntPathMatcher();

  @Override
  public void handleAuthToken(String token) {
    boolean result = jwtTokenUtil.checkTokenEffective(token);
    if (!result) {
      throw new BusinessException(ResultVO.Code.TOKEN_FAIL);
    }
    String userId = jwtTokenUtil.getUserIdFromToken(token);
    // 1、当过滤链执行完时会调用SecurityContextHolder.clearContext()把SecurityContextHolder清空
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    log.info("token验证通过，开始存储用户信息userId：{}，authentication：{}", userId, authentication);
    if (StringUtil.isNotBlank(userId) && authentication == null) {
      AuthUser user = loginService.getById(userId);
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null);
      // 2、重新SecurityContextHolder.getContext().setAuthentication(authentication)存储用户认证信息
      authenticationToken.setDetails(user);
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
  }

  public void hasPermission(String authKey, String authName) {
    Object principal = SecurityUtil.getUser();
    log.info("获取的principal信息：{}", principal);
    boolean hasPermission = false;
    if (principal instanceof AuthUser) {
      String userId = ((AuthUser) principal).getUserId();
      HttpServletRequest request = ServletUtil.getRequest();
      // 读取用户所拥有的权限菜单
      List<Object> roleIds = userRoleService.findRoleIdsByUserId(userId);
      List<Object> menuIds = roleMenuService.listObjs(new QueryWrapper<SysRoleMenu>().select("menu_id").in("role_id", roleIds));
      // 过滤掉重复的菜单ID
      menuIds = new ArrayList<>(menuIds.stream().collect(Collectors.groupingBy(Object::toString, Collectors.toList())).keySet());
      List<SysMenu> menus = sysMenuService.list(new QueryWrapper<SysMenu>().in("menu_id", menuIds));
      for (SysMenu menu : menus) {
        // 控制菜单权限
        if (MenuEnum.Type.C.getValue().equals(menu.getMenuType())) {
          log.info("menu.menuUrl={}|——|request.requestURI={}", menu.getMenuUrl(), request.getRequestURI());
          if (antPathMatcher.match(menu.getMenuUrl(), request.getRequestURI())) {
            hasPermission = true;
            break;
          }
          // 控制按钮权限
        } else if (MenuEnum.Type.F.getValue().equals(menu.getMenuType())) {
          log.info("menu.purview={}|——|request.purview={}|", menu.getPurview(), authKey);
          if (authKey.equals(menu.getPurview())) {
            hasPermission = true;
            break;
          }
        } else {
          log.info("此菜单类型【{}】不做权限控制", menu.getMenuType());
        }
      }
    }
    if (!hasPermission) {
      throw new BusinessException(String.format("没有【%s】的访问权限", authName));
    }
  }

}
