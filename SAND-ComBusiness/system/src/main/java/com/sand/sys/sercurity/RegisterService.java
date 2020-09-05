/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/9/4    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.sercurity;

import com.sand.user.entity.AuthUser;
import com.sand.user.service.impl.AuthUserRegisterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 功能说明：注册处理器
 * 开发人员：@author liusha
 * 开发日期：2020/9/4 14:06
 * 功能描述：重写父类注册三步曲
 */
@Slf4j
@Component
public class RegisterService extends AuthUserRegisterServiceImpl {

  @Override
  public void registerBeforeValid(Map<String, Object> params) {
    super.registerBeforeValid(params);
    // TODO 其它参数校验
  }

  @Override
  public AuthUser register(Map<String, Object> params) {
    AuthUser authUser = super.register(params);
    // TODO 其它注册逻辑
    return authUser;
  }

  @Override
  public Map<String, Object> registerAfterHandle(AuthUser authUser) {
    Map<String, Object> registerResult = super.registerAfterHandle(authUser);
    // TODO 其它注册处理
    return registerResult;
  }
}
