/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sand.sys.entity.SysRole;
import com.sand.sys.model.SysRoleModel;

/**
 * 功能说明：系统角色
 * 开发人员：@author liusha
 * 开发日期：2019/8/30 17:12
 * 功能描述：系统角色
 */
public interface ISysRoleService extends IService<SysRole> {
  /**
   * 新增角色
   *
   * @param model dto
   * @return
   */
  int add(SysRoleModel model);

  /**
   * 修改角色
   *
   * @param model dto
   * @return
   */
  int edit(SysRoleModel model);

  /**
   * 取消权限
   *
   * @param roleId 需取消权限的角色ID
   */
  void cancelAuthorize(String roleId);

  /**
   * 重新授权
   *
   * @param model 需重新授权的角色
   */
  void reauthorize(SysRoleModel model);
}
