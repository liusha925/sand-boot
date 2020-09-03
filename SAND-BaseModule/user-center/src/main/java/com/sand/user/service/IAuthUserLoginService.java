/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sand.user.entity.AuthUser;

import java.util.Map;

/**
 * 功能说明：用户登录服务
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 9:00
 * 功能描述：用户登录服务，登录三步曲
 */
public interface IAuthUserLoginService extends IService<AuthUser> {
  /**
   * 1、登录前校验
   *
   * @param params 登录参数
   */
  void loginBeforeValid(Map<String, Object> params);

  /**
   * 2、登录逻辑
   *
   * @param params 登录信息
   * @return 登录信息
   */
  Object login(Map<String, Object> params);

  /**
   * 3、登录后处理
   *
   * @param userDetails 用户基础信息
   * @return 登录结果集
   */
  Map<String, Object> loginAfterHandle(Object userDetails);

}
