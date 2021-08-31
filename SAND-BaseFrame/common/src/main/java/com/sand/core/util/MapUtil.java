/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sand.core.vo.ResultVO;
import com.sand.core.exception.BusinessException;
import com.sand.core.util.lang3.StringUtil;
import com.sand.core.util.convert.SandConvert;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * 功能说明：参数获取工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 14:29
 * 功能描述：参数获取工具类
 */
public class MapUtil {
  /**
   * 默认整型参数值
   */
  protected static final int DEFAULT_PARAM_INT = 0;

  public MapUtil() {
  }

  /**
   * 获取整型参数（非必须），默认DEFAULT_INT_PARAM = 0
   *
   * @param map
   * @param key
   * @return
   */
  public static int getIntValue(Map<String, Object> map, String key) {
    return getIntValue(map, key, DEFAULT_PARAM_INT);
  }

  /**
   * 获取整型参数（非必须），默认defaultValue
   *
   * @param map map
   * @param key key
   * @param defaultValue defaultValue
   * @return 整型参数
   */
  public static int getIntValue(Map<String, Object> map, String key, int defaultValue) {
    String value = SandConvert.obj2Str(getValue(map, key, defaultValue));
    if (StringUtil.isBlank(value)) {
      return defaultValue;
    }
    return Integer.parseInt(getValue(map, key, defaultValue).toString());
  }

  /**
   * 获取整型参数
   *
   * @param map map
   * @param key key
   * @param required required
   * @return 整型参数
   */
  public static int getIntValue(Map<String, Object> map, String key, boolean required) {
    return Integer.parseInt(getValue(map, key, DEFAULT_PARAM_INT, required).toString());
  }

  /**
   * 获取字符串参数值（必须）
   *
   * @param map map
   * @param key key
   * @return 字符串参数值
   */
  public static String getStringValue(Map<String, Object> map, String key) {
    Object value = getValue(map, key, "", true);
    return Objects.isNull(value) ? null : StringUtils.deleteWhitespace(value.toString());
  }

  /**
   * 获取字符串参数信息（非必须）
   *
   * @param map map
   * @param key key
   * @param defaultValue defaultValue
   * @return 字符串参数值
   */
  public static String getStringValue(Map<String, Object> map, String key, Object defaultValue) {
    Object value = getValue(map, key, defaultValue, false);
    return Objects.isNull(value) ? null : StringUtils.deleteWhitespace(value.toString());
  }

  /**
   * 获取BigDecimal参数值（必须）
   *
   * @param map map
   * @param key key
   * @return BigDecimal参数值
   */
  public static BigDecimal getBigDecimalValue(Map<String, Object> map, String key) {
    Object value = getValue(map, key, null, true);
    return Objects.isNull(value) ? null : BigDecimal.valueOf(Double.parseDouble(value.toString()));
  }

  /**
   * 获取BigDecimal参数值（非必须）
   *
   * @param map map
   * @param key key
   * @param defaultValue defaultValue
   * @return BigDecimal参数值
   */
  public static BigDecimal getBigDecimalValue(Map<String, Object> map, String key, BigDecimal defaultValue) {
    Object value = getValue(map, key, defaultValue, false);
    return Objects.isNull(value) ? BigDecimal.ZERO : BigDecimal.valueOf(Double.parseDouble(value.toString()));
  }

  /**
   * 长整型
   *
   * @param map map
   * @param key key
   * @param defaultValue defaultValue
   * @return 长整型
   */
  public static long getLongValue(Map<String, Object> map, String key, long defaultValue) {
    Object value = getValue(map, key, defaultValue, false);
    return Objects.isNull(value) ? 0L : Long.valueOf(value + StringUtil.EMPTY);
  }

  /**
   * 对象
   *
   * @param map map
   * @param key key
   * @param clazz clazz
   * @return 对象
   */
  public static <K> K getObjectValue(Map<String, Object> map, String key, Class<K> clazz) {
    String value = getStringValue(map, key, null);
    K k;
    try {
      if (StringUtil.isNotBlank(value)) {
        k = JSONObject.parseObject(value, clazz);
      } else {
        k = null;
      }
    } catch (Exception e) {
      throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, e);
    }
    return k;
  }

  /**
   * 转换带泛型的对象
   *
   * @param map map
   * @param key key
   * @param type type
   * @return 带泛型的对象
   */
  public static Object getObjectValue(Map<String, Object> map, String key, TypeReference type) {
    String value = getStringValue(map, key, null);
    Object obj = null;
    try {
      if (StringUtil.isNotBlank(value)) {
        obj = JSONObject.parseObject(value, type);
      }
    } catch (Exception e) {
      throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, e);
    }
    return obj;
  }

  /**
   * 获取参数值（必须）
   *
   * @param  map map
   * @param  key key
   * @return 参数值
   */
  public static Object getValue(Map<String, Object> map, String key) {
    return getValue(map, key, null, true);
  }

  /**
   * 获取参数信息（非必须）
   *
   * @param  map map
   * @param  key key
   * @param  defaultValue defaultValue
   * @return 参数值
   */
  public static Object getValue(Map<String, Object> map, String key, Object defaultValue) {
    return getValue(map, key, defaultValue, false);
  }

  /**
   * 获取参数信息
   *
   * @param map map
   * @param key key
   * @param defaultValue defaultValue
   * @param required required
   * @return 参数值
   */
  public static Object getValue(Map<String, Object> map, String key, Object defaultValue, boolean required) {
    if (Objects.isNull(map)) {
      if (required) {
        throw new BusinessException(ResultVO.Code.PARAM_MISSING_ERROR, "参数【" + key + "】不能为空");
      }
      return defaultValue;
    }
    if (map.containsKey(key)) {
      Object value = map.get(key);
      if (required && StringUtil.isBlank(SandConvert.obj2Str(value))) {
        throw new BusinessException(ResultVO.Code.PARAM_MISSING_ERROR, "参数【" + key + "】不能为空");
      }
      return value;
    } else if (required) {
      throw new BusinessException(ResultVO.Code.PARAM_MISSING_ERROR, "参数【" + key + "】不能为空");
    }
    return defaultValue;
  }

}
