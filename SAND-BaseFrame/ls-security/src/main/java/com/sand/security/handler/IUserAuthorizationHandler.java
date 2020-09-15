/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/9/3    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.handler;

/**
 * 功能说明：用户安全认证服务接口
 * 开发人员：@author liusha
 * 开发日期：2020/9/3 13:25
 * 功能描述：处理token
 */
public interface IUserAuthorizationHandler {
  /**
   * 处理token是否有效
   *
   * @param token token
   */
  void handleAuthToken(String token);
}
