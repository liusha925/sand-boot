/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.mybatisplus.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sand.core.util.ParamUtil;
import com.sand.core.util.lang3.StringUtil;
import com.sand.core.util.convert.SandConvert;

import java.util.HashMap;
import java.util.List;
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
   * @param entity 分页对象
   * @return 分页信息
   */
  public static Page pageParam(Object entity) {
    Map<String, Object> entityMap = SandConvert.obj2Map(entity);
    return pageParam(entityMap, DEFAULT_PAGE_SIZE);
  }

  /**
   * 获取分页参数，默认一页10
   *
   * @param entityMap 分页对象
   * @return 分页信息
   */
  public static Page pageParam(Map<String, Object> entityMap) {
    return pageParam(entityMap, DEFAULT_PAGE_SIZE);
  }

  /**
   * 获取分页参数
   *
   * @param entityMap   分页对象
   * @param defaultSize 默认一页显示数据量
   * @return 分页信息
   */
  public static Page pageParam(Map<String, Object> entityMap, int defaultSize) {
    return pageParam(entityMap, defaultSize, DEFAULT_MAX_PAGE_SIZE);
  }

  /**
   * 获取分页参数
   *
   * @param entityMap   分页对象
   * @param defaultSize 默认一页显示数据量
   * @param maxSize     最多显示数据量
   * @return 分页信息
   */
  public static Page pageParam(Map<String, Object> entityMap, int defaultSize, int maxSize) {
    if (Objects.isNull(entityMap)) {
      entityMap = new HashMap<>();
    }
    // 每页条数
    int size;
    String pageSize = SandConvert.obj2Str(entityMap.get("size"));
    if (StringUtil.isBlank(pageSize)) {
      size = DEFAULT_PAGE_SIZE;
    } else {
      size = getIntValue(entityMap, "size", defaultSize);
    }
    // 当前页码
    int current;
    String pageCurrent = SandConvert.obj2Str(entityMap.get("current"));
    if (StringUtil.isBlank(pageCurrent)) {
      current = DEFAULT_PAGE_CURRENT;
    } else {
      current = getIntValue(entityMap, "current", DEFAULT_PAGE_CURRENT);
    }
    size = Math.min(size, maxSize);
    Page page = new Page(current, size);
    return page;
  }

  /**
   * 将mybatis-plus的分页插件转换成需要的展示格式
   *
   * @param page 分页信息
   * @return 分页信息
   */
  public static Map<String, Object> page2map(Page<?> page) {
    return page2map(page, page.getRecords());
  }

  /**
   * 将mybatis-plus的分页插件转换成需要的展示格式
   *
   * @param page    分页信息
   * @param records 分页数据
   * @return 分页信息
   */
  public static Map<String, Object> page2map(Page<?> page, List records) {
    Map<String, Object> map = new HashMap<>();
    map.put("records", records);
    map.put("total", page.getTotal());
    map.put("pages", page.getPages());
    map.put("current", page.getCurrent());
    map.put("size", page.getSize());
    return map;
  }

}
