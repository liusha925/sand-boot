/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/5/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.controller;

import com.sand.business.parent.base.BaseController;
import com.sand.sys.entity.SysUser;

import java.util.Objects;

/**
 * 功能说明：系统模块基础业务
 * 开发人员：@author liusha
 * 开发日期：2020/5/26 14:37
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
public class SysBaseController extends BaseController {

  /**
   * 从SecurityContextHolder获取用户信息
   *
   * @return 用户信息
   */
  public SysUser getSysUserInfo() {
    Object user = super.getUserInfo();
    if (Objects.isNull(user)) {
      super.newBusinessException("用户不存在！");
    }
    if (!(user instanceof SysUser)) {
      super.newBusinessException("用户类型非法！");
    }

    return (SysUser) user;
  }

  /**
   * 从SecurityContextHolder获取用户ID
   *
   * @return 用户ID
   */
  public String getUserId() {
    return this.getSysUserInfo().getUserId();
  }

  /**
   * 从SecurityContextHolder获取用户名
   *
   * @return 用户名
   */
  public String getUserName() {
    return this.getSysUserInfo().getUsername();
  }

  /**
   * 从SecurityContextHolder获取用户姓名
   *
   * @return 用户姓名
   */
  public String getRealName() {
    return this.getSysUserInfo().getRealName();
  }
}
