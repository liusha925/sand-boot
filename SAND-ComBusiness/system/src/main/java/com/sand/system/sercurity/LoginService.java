/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/9/4    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.sercurity;

import com.sand.user.entity.AuthUser;
import com.sand.user.service.impl.AuthUserLoginServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 功能说明：登录处理器
 * 开发人员：@author liusha
 * 开发日期：2020/9/4 13:42
 * 功能描述：重写父类登录三步曲
 */
@Component
public class LoginService extends AuthUserLoginServiceImpl {

  @Override
  public void loginBeforeValid(Map<String, Object> params) {
    super.loginBeforeValid(params);
    // TODO 验证码校验等
  }

  @Override
  public Object login(Map<String, Object> params) {
    AuthUser authUser = (AuthUser) super.login(params);
    // TODO 其它登录逻辑
    return authUser;
  }

  @Override
  public Map<String, Object> loginAfterHandle(Object userDetails) {
    Map<String, Object> loginResult = super.loginAfterHandle(userDetails);
//    // 1、存储用户信息至redis
//    jwtTokenUtil.putUserToken(dbUser, accessToken);
//    jwtTokenUtil.putUserDetail(dbUser);
//    // TODO 2、保存登录日志
//    String userId = MapUtil.getStringValue(loginResult, "user_id");
//    List<SysUserRole> userRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", userId));
//    Set<String> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
//    List<SysRole> roles = new ArrayList<>(roleService.listByIds(roleIds));
//    // 根据用角色ID获取角色菜单
//    List<SysRoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<SysRoleMenu>().in("role_id", roleIds));
//    Set<String> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
//    // 菜单权限
//    List<SysMenu> menus = new ArrayList<>(menuService.listByIds(menuIds));
//    loginResult.put("roles", roles);
//    loginResult.put("menus", menus);

    return loginResult;
  }
}
