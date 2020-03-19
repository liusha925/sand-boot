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
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

/**
 * 功能说明：用户安全认证服务接口
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 9:00
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
public interface IUserSecurityService {
  /**
   * 1、初始化用户信息
   *
   * @return 用户实体
   */
  <T extends UserDetails> T init();

  /**
   * 2、登录前校验
   *
   * @param param
   */
  void validateUser(Map<String, Object> param);

  /**
   * 3、处理用户认证信息
   *
   * @param userDetails         用户基础信息
   * @param authenticationToken 用户认证信息
   */
  void handleAuthentication(Object userDetails, AbstractAuthenticationToken authenticationToken);

  /**
   * 4、登录后处理
   *
   * @param userDetails 用户基础信息
   * @return 登录结果与登录信息
   */
  ResultEntity handleUser(Object userDetails);

}
