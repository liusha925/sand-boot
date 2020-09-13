/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/9/13/013   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.security.permission;

/**
 * 功能说明：大致描述 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/9/13/013 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public interface IPermissionService {
  /**
   * 权限控制
   *
   * @param authKey  权限标识
   * @param authName 权限名称
   */
  void hasPermission(String authKey, String authName);
}
