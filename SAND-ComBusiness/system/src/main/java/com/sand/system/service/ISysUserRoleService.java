/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sand.system.entity.SysUserRole;

import java.util.List;

/**
 * 功能说明：用户角色关系
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 17:03
 * 功能描述：用户角色关系
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
  /**
   * 根据用户id查找角色ids
   *
   * @param userId 用户id
   * @return 角色ids
   */
  List<Object> findRoleIdsByUserId(String userId);
}
