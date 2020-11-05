/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/9/5/005   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.system.sercurity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sand.system.entity.SysRole;
import com.sand.system.entity.SysUser;
import com.sand.system.entity.SysUserRole;
import com.sand.system.service.ISysRoleService;
import com.sand.system.service.ISysUserRoleService;
import com.sand.system.service.ISysUserService;
import com.sand.user.entity.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能说明：用户信息加载器 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/9/5/005 <br>
 * 功能描述：自定义实现 UserDetailsService 接口 <br>
 */
@Slf4j
@Component
public class UserDetailsLoader implements UserDetailsService {
  @Autowired
  private ISysUserService sysUserService;
  @Autowired
  private ISysUserRoleService userRoleService;
  @Autowired
  private ISysRoleService roleService;
  @Autowired
  private LoginService loginService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 此处可定义多种登录方式
    SysUser dbSysUser = sysUserService.loadUserByUsername(username);
    if (Objects.isNull(dbSysUser)) {
      // TODO 手机号、邮箱等其它登录方式
    }
    if (Objects.nonNull(dbSysUser)) {
      // 根据用户ID获取用户角色
      List<SysUserRole> userRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", dbSysUser.getUserId()));
      Set<String> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
      List<SysRole> roles = new ArrayList<>(roleService.listByIds(roleIds));
      // 自定义的用户认证权限信息
      Collection<UserGrantedAuthority> authorities = new HashSet<>();
      roles.forEach(role -> authorities.add(UserGrantedAuthority
          .builder()
          .authName(role.getRoleName())
          .build())
      );
      // 填充用户认证信息
      AuthUser dbAuthUser = loginService.getById(dbSysUser.getAuthUser().getUserId());
      dbAuthUser.setAuthorities(authorities);
      return dbAuthUser;
    } else {
      log.info("此{}用户不存在！", username);
      throw new UsernameNotFoundException(username + " not found");
    }
  }
}
