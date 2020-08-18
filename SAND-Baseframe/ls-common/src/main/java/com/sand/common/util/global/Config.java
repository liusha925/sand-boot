/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/26   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.global;

import com.sand.common.util.lang3.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 功能说明：配置中心
 * 开发人员：@author liusha
 * 开发日期：2020/4/26 15:13
 * 功能描述：全局配置
 */
@Slf4j
public class Config {
  private static final Properties PROPS = new Properties();
  private static final Map<String, Object> CONFIG = new HashMap<>();

  /**
   * 获取配置
   *
   * @param key key
   * @return value
   */
  public static String getProperty(String key) {
    return getProperty(key, StringUtil.EMPTY);
  }

  /**
   * 获取配置
   *
   * @param key          key
   * @param defaultValue defaultValue
   * @return value or defaultValue
   */
  public static String getProperty(String key, String defaultValue) {
    String value = PROPS.getProperty(key);
    if (StringUtil.isNotBlank(value)) {
      return value.trim();
    }
    return defaultValue;
  }

  /**
   * 添加配置
   *
   * @param key   key
   * @param value value
   */
  public static void setProperty(String key, Object value) {
    PROPS.put(key, value);
  }

  /**
   * 获取配置
   *
   * @param key key
   * @return value
   */
  public static Object getConfig(String key) {
    return CONFIG.get(key);
  }

  /**
   * 添加配置
   *
   * @param key   key
   * @param value value
   */
  public static void setConfig(String key, Object value) {
    CONFIG.put(key, value);
  }

}
