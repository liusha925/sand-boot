/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.system.entity.SysRole;
import com.sand.system.entity.SysUser;
import com.sand.system.mapper.SysUserMapper;
import com.sand.system.service.ISysRoleService;
import com.sand.system.service.ISysUserRoleService;
import com.sand.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 16:59
 * 功能描述：用户信息
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
  @Autowired
  private SysUserMapper userMapper;
  @Autowired
  private ISysRoleService roleService;
  @Autowired
  private ISysUserRoleService userRoleService;

  @Override
  public SysUser loadUserByUsername(String username) {
    return userMapper.loadUserByUsername(username);
  }

  @Override
  public void findRoleInfo(List<SysUser> userList) {
    userList.forEach(user -> {
      List<Object> roleIds = userRoleService.findRoleIdsByUserId(user.getUserId());
      List<SysRole> userRoles = new ArrayList<>(roleService.listByIds(roleIds.stream().map(Object::toString).collect(Collectors.toList())));
      user.setUserRoles(userRoles);
    });
  }
}
