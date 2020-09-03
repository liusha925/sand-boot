/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.user.entity.AuthUser;
import com.sand.user.mapper.AuthUserMapper;
import com.sand.user.service.IAuthUserRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 功能说明：用户注册服务
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 16:59
 * 功能描述：用户注册服务，注册三步曲
 */
@Slf4j
@Service
public class AuthUserRegisterServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements IAuthUserRegisterService {

  @Override
  public void registerBeforeValid(Map<String, Object> params) {
    log.info("1、注册前校验");

  }

  @Override
  public AuthUser register(Map<String, Object> params) {
    log.info("2、注册逻辑");
    return null;
  }

  @Override
  public Map<String, Object> registerAfterHandle(AuthUser authUser) {
    log.info("3、注册后处理");
    return null;
  }

}
