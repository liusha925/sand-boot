/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.controller.sys;

import com.sand.common.util.http.OkHttp3Util;
import com.sand.common.util.lang3.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明：系统菜单测试类
 * 开发人员：@author liusha
 * 开发日期：2019/8/27 14:18
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
public class SysMenuControllerTest {
  private static final String ACTIVE_LOCAL = "http://localhost:8080";

  @Test
  public void leftTree() {
    String result = OkHttp3Util.httpPostJson(ACTIVE_LOCAL + "/sys/menu/leftTree", new HashMap<>());
    log.info("返回结果：{}", result);
  }

  @Test
  public void page() {
    String result = OkHttp3Util.httpPostJson(ACTIVE_LOCAL + "/sys/menu/page", new HashMap<>());
    log.info("返回结果：{}", result);
  }

  @Test
  public void add() {
    Map<String,Object> requestMap = new HashMap<>();
    requestMap.put("parentName", "父级菜单名称");
    requestMap.put("parentId", "treeRoot");
    requestMap.put("menuName", "测试菜单1");
    requestMap.put("menuType", "M");
    requestMap.put("target", "_item");
    requestMap.put("visible", "0");
    requestMap.put("createTime", DateUtil.addDays(new Date(), 2));
    String result = OkHttp3Util.httpPostJson(ACTIVE_LOCAL + "/sys/menu/add", requestMap);
    log.info("返回结果：{}", result);
  }
}
