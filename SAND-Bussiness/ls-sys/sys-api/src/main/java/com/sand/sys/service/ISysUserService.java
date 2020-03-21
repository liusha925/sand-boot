/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sand.sys.entity.SysUser;

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
}
