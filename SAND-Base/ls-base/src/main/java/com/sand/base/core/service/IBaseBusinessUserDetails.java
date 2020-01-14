/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/25    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.core.service;

/**
 * 功能说明：业务对象接口
 * 开发人员：@author liusha
 * 开发日期：2019/11/25 15:10
 * 功能描述：业务对象接口，放入缓存时需要取用户名信息
 */
public interface IBaseBusinessUserDetails {
  /**
   * 获取用户标识
   *
   * @return
   */
  String getUserId();

  /**
   * 获取用户名
   *
   * @return
   */
  String getUserame();
}
