/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/14    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.global;

import com.sand.common.util.lang3.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * 功能说明：用于加载配置文件
 * 开发人员：@author liusha
 * 开发日期：2020/4/14 15:13
 * 功能描述：用于加载配置文件，key=value形式
 */
@Slf4j
public class Global {
  private Properties mainProp;
  private static Global global = null;

  private Global(String fileName) {
    mainProp = new PropertiesUtil(fileName).getProperties();
  }

  /**
   * DCL双检查锁机制确保多线程安全
   *
   * @param fileName 文件名称
   * @return
   */
  public static synchronized Global getInstance(String fileName) {
    if (global == null) {
      synchronized (Global.class) {
        if (global == null) {
          global = new Global(fileName);
        }
      }
    }
    return global;
  }

  /**
   * 根据key获取对应的value
   * <pre>
   *     例global.properties文件内容为：spring.profiles.active=test
   * </pre>
   * <pre>
   *     Global.getInstance("global.properties").getConfig("spring.profiles.active") = "test"
   *     Global.getInstance("global.properties").getConfig("spring.profiles.active123") = null
   * </pre>
   *
   * @param key key
   * @return value
   */
  public String getConfig(String key) {
    return getConfig(mainProp, key, null);
  }

  /**
   * 根据key获取对应的value
   * <pre>
   *     例global.properties文件内容为：spring.profiles.active=test
   * </pre>
   * <pre>
   *     Global.getInstance("global.properties").getConfig("spring.profiles.active", "test") = "test"
   *     Global.getInstance("global.properties").getConfig("spring.profiles.active123", "test") = "test"
   * </pre>
   *
   * @param key          key
   * @param defaultValue 默认值
   * @return value
   */
  public String getConfig(String key, String defaultValue) {
    return getConfig(mainProp, key, defaultValue);
  }

  /**
   * 根据key获取对应的value
   *
   * @param prop         Properties
   * @param key          key
   * @param defaultValue 默认值
   * @return value
   */
  public String getConfig(Properties prop, String key, String defaultValue) {
    String value = prop.getProperty(key);
    if (StringUtil.isNotBlank(value)) {
      return value.trim();
    }
    return defaultValue;
  }

}
