/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/30   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.controller.sys;

import com.sand.common.util.http.OkHttp3Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明：
 * 开发人员：@author liusha
 * 开发日期：2019/8/30 17:48
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
public class SysRoleControllerTest {
  private static final String ACTIVE_LOCAL = "http://localhost:8080";

  @Test
  public void page() {
    Map<String, Object> roleMap = new HashMap<>();
    roleMap.put("createTime", new Date());
    roleMap.put("updateTime", new Date());
    String result = OkHttp3Util.httpPostJson(ACTIVE_LOCAL + "/sys/role/page", roleMap);
    log.info("返回结果：{}", result);
  }
}
