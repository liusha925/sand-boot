/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/20    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.controller.security;

import com.google.common.collect.Maps;
import com.sand.base.util.http.OkHttp3Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

/**
 * 功能说明：测试security框架
 * 开发人员：@author liusha
 * 开发日期：2020/3/20 21:36
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
public class SecurityControllerTest {
  private static final String ACTIVE_LOCAL = "http://localhost:8080";

  @Test
  public void loginTest() {
    Map<String, Object> loginMap = Maps.newHashMap();
    loginMap.put("username", "test");
//    loginMap.put("password", "123456");
    loginMap.put("password", "olCPKgwi0rk=");
    String result = OkHttp3Util.httpPostJson(ACTIVE_LOCAL + "/security/login", loginMap);
    log.info("返回结果：{}", result);
  }
}
