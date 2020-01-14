/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.service.impl;

import com.sand.security.entity.AuthUser;
import com.sand.sys.entity.SysMenu;
import com.sand.sys.entity.SysRole;
import com.sand.sys.mapper.SysRoleMapper;
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

/**
 * 功能说明：自定义实现 UserDetailsService 接口
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 14:58
 * 功能描述：自定义实现 UserDetailsService 接口
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private SysRoleMapper roleMapper;
  @Override
  public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
    // 根据用户名查找用户
//    AuthUser user = userDao.getUserByUsername(username);
    AuthUser user = new AuthUser();
    if (user != null) {
      // 根据用户ID获取用户角色
//      List<SysRole> roles = roleMapper.getUserRoleByUserId(user.getUserId());
      List<SysRole> roles = new ArrayList<>();
      // 填充权限
      Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
      for (SysRole role : roles) {
        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
      }
      // 根据角色ID获取权限菜单
//      List<SysMenu> menus = menuDao.getRoleMenuByRoles(roles);
      List<SysMenu> menus = new ArrayList<>();
      return new AuthUser(username, user.getPassword(), authorities, menus);
    } else {
      System.out.println(username + " not found");
      throw new UsernameNotFoundException(username + " not found");
    }
  }
}
