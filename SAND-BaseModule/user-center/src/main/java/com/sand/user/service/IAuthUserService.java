/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sand.common.vo.ResultVO;
import com.sand.user.entity.AuthUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Map;

/**
 * 功能说明：用户安全认证信息
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 9:00
 * 功能描述：用户安全认证信息
 */
public interface IAuthUserService extends IService<AuthUser> {
  /**
   * 1、认证前校验
   *
   * @param params 登录参数
   */
  void beforeValidate(Map<String, Object> params);

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
  ResultVO authAfter(Object userDetails);

  /**
   * 注册接口
   *
   * @param params 注册信息
   * @return 返回注册信息
   */
  ResultVO register(Map<String, Object> params);
}
