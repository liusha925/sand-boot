/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sand.sys.entity.SysMenu;
import com.sand.sys.entity.SysRole;
import com.sand.sys.entity.SysRoleMenu;
import com.sand.sys.entity.SysUser;
import com.sand.sys.entity.SysUserRole;
import com.sand.sys.service.ISysMenuService;
import com.sand.sys.service.ISysRoleMenuService;
import com.sand.sys.service.ISysRoleService;
import com.sand.sys.service.ISysUserRoleService;
import com.sand.sys.service.ISysUserService;
import com.sand.user.entity.AuthUser;
import com.sand.user.service.IAuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能说明：自定义实现 UserDetailsService 接口
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 16:20
 * 功能描述：自定义实现 UserDetailsService 接口
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private IAuthUserService authUserService;
  @Autowired
  private ISysUserService sysUserService;
  @Autowired
  private ISysRoleService roleService;
  @Autowired
  private ISysUserRoleService userRoleService;
  //  @Autowired
  ////  private ISysMenuService menuService;
//  @Autowired
//  private ISysRoleMenuService roleMenuService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 此处可定义多种登录方式，如用户名、手机号等
    SysUser dbSysUser = sysUserService.loadUserByUsername(username);
    if (Objects.isNull(dbSysUser)) {
      // TODO 手机号登录
    }
    if (Objects.nonNull(dbSysUser)) {
      // 根据用户ID获取用户角色
      List<SysUserRole> userRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", dbSysUser.getUserId()));
      Set<String> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
      List<SysRole> roles = new ArrayList<>(roleService.listByIds(roleIds));
      // 认证权限
      Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
      roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
      /*// 根据用角色ID获取角色菜单
      List<SysRoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<SysRoleMenu>().in("role_id", roleIds));
      Set<String> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
      // 菜单权限
      List<SysMenu> menus = new ArrayList<>(menuService.listByIds(menuIds));*/
      // 填充用户认证信息
      AuthUser dbAuthUser = authUserService.getById(dbSysUser.getAuthUser().getUserId());
      dbAuthUser.setAuthorities(authorities);
      return dbAuthUser;
    } else {
      log.info("此{}用户不存在！", username);
      throw new UsernameNotFoundException(username + " not found");
    }
  }
}
