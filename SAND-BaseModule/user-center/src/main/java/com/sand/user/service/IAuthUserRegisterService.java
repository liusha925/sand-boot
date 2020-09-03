/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/9/3/003   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.user.service;

import com.sand.user.entity.AuthUser;

import java.util.Map;

/**
 * 功能说明：用户注册服务 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/9/3/003 <br>
 * 功能描述：用户注册服务，注册三步曲 <br>
 */
public interface IAuthUserRegisterService {
  /**
   * 1、注册前校验
   *
   * @param params 注册信息
   */
  void registerBeforeValid(Map<String, Object> params);

  /**
   * 2、注册逻辑
   *
   * @param params 注册信息
   * @return 注册信息
   */
  AuthUser register(Map<String, Object> params);

  /**
   * 3、注册后处理
   *
   * @param authUser 注册信息
   * @return 注册结果集
   */
  Map<String, Object> registerAfterHandle(AuthUser authUser);
}
