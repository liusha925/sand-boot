/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.controller.sys;

import com.sand.base.util.http.OkHttp3Util;
import com.sand.util.JunitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;

/**
 * 功能说明：
 * 开发人员：@author liusha
 * 开发日期：2019/8/27 14:18
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
public class SysMenuControllerTest extends JunitTest {
  private static final String ACTIVE_LOCAL = "http://localhost:8080";

  @Test
  public void tree() {
    String result = OkHttp3Util.httpPostJson(ACTIVE_LOCAL + "/sys/menu/tree", new HashMap<>());
    log.info("返回结果：{}", result);
  }
}