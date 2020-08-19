/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/5/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.controller;

import com.sand.business.parent.base.BaseController;
import com.sand.business.parent.util.AuthenticationUtil;
import com.sand.sys.entity.SysUser;
import com.sand.sys.service.ISysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 功能说明：系统模块基础业务
 * 开发人员：@author liusha
 * 开发日期：2020/5/26 14:37
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
public class SysBaseController extends BaseController {
  @Autowired
  private ISysUserRoleService userRoleService;

  /**
   * 从SecurityContextHolder获取用户信息
   *
   * @return 用户信息
   */
  public SysUser getSysUser() {
    Object user = AuthenticationUtil.getUser();
    if (!(user instanceof SysUser)) {
      super.newBusinessException("用户类型非法！");
    }
    log.info("当前登录用户信息：{}", user.toString());
    return (SysUser) user;
  }

  /**
   * 从SecurityContextHolder获取用户ID
   *
   * @return 用户ID
   */
  public String getUserId() {
    log.info("当前登录用户userId：{}", this.getSysUser().getUserId());
    return this.getSysUser().getUserId();
  }

  /**
   * 从SecurityContextHolder获取用户名
   *
   * @return 用户名
   */
  public String getUsername() {
    log.info("当前登录用户username：{}", this.getSysUser().getUsername());
    return this.getSysUser().getUsername();
  }

  /**
   * 从SecurityContextHolder获取用户姓名
   *
   * @return 用户姓名
   */
  public String getRealName() {
    log.info("当前登录用户realName：{}", this.getSysUser().getRealName());
    return this.getSysUser().getRealName();
  }

  /**
   * 获取当前登录用户的角色id集合
   *
   * @return 角色id集合
   */
  public String[] getRoleIds() {
    List<Object> roleIds = userRoleService.findRoleIdsByUserId(this.getUserId());
    log.info("当前登录用户roleIds：{}", roleIds);
    return ArrayUtils.toStringArray(roleIds.toArray());
  }
}
