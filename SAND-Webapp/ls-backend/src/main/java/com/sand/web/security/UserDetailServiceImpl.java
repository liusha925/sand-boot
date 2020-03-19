/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sand.sys.entity.SysRole;
import com.sand.sys.entity.SysUser;
import com.sand.sys.entity.SysUserRole;
import com.sand.sys.service.ISysRoleService;
import com.sand.sys.service.ISysUserRoleService;
import com.sand.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
public class UserDetailServiceImpl implements UserDetailsService {
  @Autowired
  private ISysUserService userService;
  @Autowired
  private ISysRoleService roleService;
  @Autowired
  private ISysUserRoleService userRoleService;

  @Override
  public SysUser loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("自定义实现 UserDetailsService 接口");
    SysUser user = userService.getOne(new QueryWrapper<SysUser>().eq("username", username));
    if (Objects.nonNull(user)) {
      // 根据用户ID获取用户角色
      List<SysUserRole> userRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", user.getUserId()));
      Set<String> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
      Collection<SysRole> roles = roleService.listByIds(roleIds);
      // 填充角色权限
      Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
      roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
      // 填充菜单权限
    } else {
      log.info("{} not found", username);
      throw new UsernameNotFoundException(username + " not found");
    }
    return null;
  }
}
