/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.web.service;

import java.util.Map;

/**
 * 功能说明：基础用户服务
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 17:22
 * 功能描述：基础用户服务
 */
public interface IBaseUserDetailsService {
  /**
   * 登录前校验
   *
   * @param param
   */
  void validateUser(Map<String, Object> param);

  /**
   * 处理其他权限
   *
   * @param userDetails
   */
  default void handleAuths(IBaseUserDetails userDetails) {
  }

  /**
   * 登录后处理
   *
   * @param userDetails
   */
  default void handleUser(IBaseUserDetails userDetails) {
  }
}
