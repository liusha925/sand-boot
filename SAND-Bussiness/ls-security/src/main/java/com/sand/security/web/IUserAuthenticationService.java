/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web;

import com.sand.base.web.entity.ResultEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Map;

/**
 * 功能说明：用户安全认证服务接口
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 9:00
 * 功能描述：用于安全登录认证：1、认证前校验；2、处理认证信息；3、认证后处理
 */
public interface IUserAuthenticationService {
  /**
   * 1、认证前校验
   *
   * @param param 登录参数
   */
  void beforeValidate(Map<String, Object> param);

  /**
   * 2、处理认证信息
   *
   * @param authenticationToken 用户认证信息
   * @return 用户基础信息
   */
  Object handleAuthInfo(AbstractAuthenticationToken authenticationToken);

  /**
   * 3、认证后处理
   *
   * @param userDetails 用户基础信息
   * @return 登录结果与登录信息
   */
  ResultEntity authAfter(Object userDetails);

}
