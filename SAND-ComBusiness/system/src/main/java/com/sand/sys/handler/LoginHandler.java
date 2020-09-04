/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/9/4    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.handler;

import com.sand.user.entity.AuthUser;
import com.sand.user.service.impl.AuthUserLoginServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 功能说明：登录处理器
 * 开发人员：@author liusha
 * 开发日期：2020/9/4 13:42
 * 功能描述：重写父类登录三步曲
 */
@Component
public class LoginHandler extends AuthUserLoginServiceImpl {

  @Override
  public void loginBeforeValid(Map<String, Object> params) {
    super.loginBeforeValid(params);
    // TODO 验证码校验等
  }

  @Override
  public Object login(Map<String, Object> params) {
    AuthUser authUser = (AuthUser) super.login(params);
    // TODO 其它登录逻辑
    return authUser;
  }

  @Override
  public Map<String, Object> loginAfterHandle(Object userDetails) {
    Map<String, Object> loginResult = super.loginAfterHandle(userDetails);
    // TODO 返回其它结果集
    return loginResult;
  }
}
