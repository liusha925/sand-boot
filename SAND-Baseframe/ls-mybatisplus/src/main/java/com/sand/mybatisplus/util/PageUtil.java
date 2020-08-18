/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.mybatisplus.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sand.common.util.ParamUtil;
import com.sand.common.util.lang3.StringUtil;
import com.sand.common.util.convert.SandConvert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 功能说明：参数获取工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 14:29
 * 功能描述：参数获取工具类
 */
public class PageUtil extends ParamUtil {
  /**
   * 默认分页条数
   */
  private static final int DEFAULT_PAGE_SIZE = 10;
  /**
   * 默认当前页面
   */
  private static final int DEFAULT_PAGE_CURRENT = 1;
  /**
   * 默认分页最大条数
   **/
  private static final int DEFAULT_MAX_PAGE_SIZE = 100;

  /**
   * 获取分页参数，默认一页10
   *
   * @param entity
   * @return
   */
  public static Page pageParam(Object entity) {
    return pageParam(SandConvert.obj2Map(entity), DEFAULT_PAGE_SIZE);
  }

  /**
   * 获取分页参数，默认一页10
   *
   * @param map
   * @return
   */
  public static Page pageParam(Map<String, Object> map) {
    return pageParam(map, DEFAULT_PAGE_SIZE);
  }

  /**
   * 获取分页参数
   *
   * @param map
   * @param defaultSize
   * @return
   */
  public static Page pageParam(Map<String, Object> map, int defaultSize) {
    return pageParam(map, defaultSize, DEFAULT_MAX_PAGE_SIZE);
  }

  /**
   * 获取分页参数
   *
   * @param map
   * @param defaultSize
   * @param maxSize
   * @return
   */
  public static Page pageParam(Map<String, Object> map, int defaultSize, int maxSize) {
    if (Objects.isNull(map)) {
      map = new HashMap<>();
    }
    // 每页条数
    int size;
    String pageSize = SandConvert.obj2Str(map.get("size"));
    if (StringUtil.isBlank(pageSize)) {
      size = DEFAULT_PAGE_SIZE;
    } else {
      size = getIntValue(map, "size", defaultSize);
    }
    // 当前页码
    int current;
    String pageCurrent = SandConvert.obj2Str(map.get("current"));
    if (StringUtil.isBlank(pageCurrent)) {
      current = DEFAULT_PAGE_CURRENT;
    } else {
      current = getIntValue(map, "current", DEFAULT_PAGE_CURRENT);
    }
    size = Math.min(size, maxSize);
    Page page = new Page(current, size);
    return page;
  }

}
