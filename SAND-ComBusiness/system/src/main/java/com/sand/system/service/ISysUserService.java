/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sand.system.entity.SysUser;

import java.util.List;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 16:58
 * 功能描述：用户信息
 */
public interface ISysUserService extends IService<SysUser> {
  /**
   * 根据用户名加载用户信息
   *
   * @param username 用户名
   * @return
   */
  SysUser loadUserByUsername(String username);

  /**
   * 查询用户所拥有的角色信息
   *
   * @param userList 用户列表
   */
  void findRoleInfo(List<SysUser> userList);
}
