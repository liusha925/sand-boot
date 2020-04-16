/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/10/9    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.convert;

import com.sand.common.exception.BusinessException;
import com.sand.common.util.lang3.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 功能说明：类型转换器
 * 开发人员：@author liusha
 * 开发日期：2019/10/9 8:44
 * 功能描述：类型转换器,可以自定义转换结果
 */
@Slf4j
public class SandConvert<K, T> {
  public SandConvert() {
  }

  /**
   * 对象转换
   *
   * @param entity K
   * @return T
   */
  public T k2t(K entity) {
    if (Objects.isNull(entity)) {
      return null;
    }
    ParameterizedType pType = (ParameterizedType) this.getClass().getGenericSuperclass();
    Class<T> clazz = (Class<T>) pType.getActualTypeArguments()[1];
    T t;
    try {
      t = clazz.newInstance();
    } catch (Exception e) {
      log.error("转换器类型实例化错误", e);
      throw new BusinessException("转换器类型实例化错误");
    }
    beforeConvert(entity, t);
    BeanUtils.copyProperties(entity, t);
    afterConvert(entity, t);
    return t;
  }

  /**
   * List集合类转换
   *
   * @return
   */
  public Function<List, List> kList2tList() {
    return entities -> kList2tList(entities);
  }

  /**
   * List集合类转换
   *
   * @param entities kList
   * @return tList
   */
  public List<T> kList2tList(List<K> entities) {
    return entities.stream()
        .map(entity -> k2t(entity))
        .collect(Collectors.toList());
  }

  /**
   * 转换前置处理
   *
   * @param k
   * @param t
   */
  public void beforeConvert(K k, T t) {
    log.info("类型转换前置处理");
  }

  /**
   * 转换后置处理
   *
   * @param k
   * @param t
   */
  public void afterConvert(K k, T t) {
    log.info("类型转换后置处理");
  }

  /**
   * 转换为字符串
   * <pre>
   *   System.out.println(SandConvert.obj2Str("")); = ""
   *   System.out.println(SandConvert.obj2Str(null)); = ""
   *   System.out.println(SandConvert.obj2Str(1)); = "1"
   *   System.out.println(SandConvert.obj2Str("string")); = "string"
   * </pre>
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static String obj2Str(Object value) {
    return obj2Str(value, StringUtil.EMPTY);
  }

  /**
   * 转换为字符串
   * <pre>
   *   System.out.println(SandConvert.obj2Str("", "defaultValue")); = "defaultValue"
   *   System.out.println(SandConvert.obj2Str(null, "defaultValue")); = "defaultValue"
   *   System.out.println(SandConvert.obj2Str(1, "defaultValue")); = "1"
   *   System.out.println(SandConvert.obj2Str("string", "defaultValue")); = "string"
   * </pre>
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static String obj2Str(Object value, String defaultValue) {
    if (StringUtil.isBlank(value)) {
      return defaultValue;
    }
    if (value instanceof String) {
      return (String) value;
    }
    return value.toString();
  }

  /**
   * 转换为字符
   * <pre>
   *   System.out.println(SandConvert.obj2Char("")); = null
   *   System.out.println(SandConvert.obj2Char(null)); = null
   *   System.out.println(SandConvert.obj2Char(1)); = '1'
   *   System.out.println(SandConvert.obj2Char("string")); = 's'
   * </pre>
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static Character obj2Char(Object value) {
    return obj2Char(value, null);
  }

  /**
   * 转换为字符
   * <pre>
   *   System.out.println(SandConvert.obj2Char("", 'h')); = 'h'
   *   System.out.println(SandConvert.obj2Char(null, 'h')); = 'h'
   *   System.out.println(SandConvert.obj2Char(1, 'h')); = '1'
   *   System.out.println(SandConvert.obj2Char("string", 'h')); = 's'
   * </pre>
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static Character obj2Char(Object value, Character defaultValue) {
    if (StringUtil.isBlank(value)) {
      return defaultValue;
    }
    if (value instanceof Character) {
      return (Character) value;
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    return StringUtil.isEmpty(valueStr) ? defaultValue : valueStr.charAt(0);
  }

  /**
   * 转换为byte
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static Byte obj2Byte(Object value) {
    return obj2Byte(value, null);
  }

  /**
   * 转换为byte
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static Byte obj2Byte(Object value, Byte defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof Byte) {
      return (Byte) value;
    }
    if (value instanceof Number) {
      return ((Number) value).byteValue();
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      return Byte.parseByte(valueStr);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 转换为Short
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static Short obj2Short(Object value) {
    return obj2Short(value, null);
  }

  /**
   * 转换为Short
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static Short obj2Short(Object value, Short defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof Short) {
      return (Short) value;
    }
    if (value instanceof Number) {
      return ((Number) value).shortValue();
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      return Short.parseShort(valueStr.trim());
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 转换为Number
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static Number obj2Number(Object value) {
    return obj2Number(value, null);
  }

  /**
   * 转换为Number
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static Number obj2Number(Object value, Number defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof Number) {
      return (Number) value;
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      return NumberFormat.getInstance().parse(valueStr);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 转换为int
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static Integer obj2Int(Object value) {
    return obj2Int(value, null);
  }

  /**
   * 转换为int
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static Integer obj2Int(Object value, Integer defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof Integer) {
      return (Integer) value;
    }
    if (value instanceof Number) {
      return ((Number) value).intValue();
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(valueStr.trim());
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 转换为long
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static Long obj2Long(Object value) {
    return obj2Long(value, null);
  }

  /**
   * 转换为long
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static Long obj2Long(Object value, Long defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof Long) {
      return (Long) value;
    }
    if (value instanceof Number) {
      return ((Number) value).longValue();
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      // 支持科学计数法
      return new BigDecimal(valueStr.trim()).longValue();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 转换为double
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static Double obj2Double(Object value) {
    return obj2Double(value, null);
  }

  /**
   * 转换为double
   * 如果给定的值为空，或者转换失败，返回默认值
   * 转换失败不会报错
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static Double obj2Double(Object value, Double defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof Double) {
      return (Double) value;
    }
    if (value instanceof Number) {
      return ((Number) value).doubleValue();
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      // 支持科学计数法
      return new BigDecimal(valueStr.trim()).doubleValue();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 转换为Float
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static Float obj2Float(Object value) {
    return obj2Float(value, null);
  }

  /**
   * 转换为Float
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static Float obj2Float(Object value, Float defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof Float) {
      return (Float) value;
    }
    if (value instanceof Number) {
      return ((Number) value).floatValue();
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      return Float.parseFloat(valueStr.trim());
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 转换为boolean
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static Boolean obj2Boolean(Object value) {
    return obj2Boolean(value, null);
  }

  /**
   * 转换为boolean
   * String支持的值为：true、false、yes、ok、no，1,0 如果给定的值为空，或者转换失败，返回默认值
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static Boolean obj2Boolean(Object value, Boolean defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    valueStr = valueStr.trim().toLowerCase();
    switch (valueStr) {
      case "true":
        return true;
      case "false":
        return false;
      case "yes":
        return true;
      case "ok":
        return true;
      case "no":
        return false;
      case "1":
        return true;
      case "0":
        return false;
      default:
        return defaultValue;
    }
  }

  /**
   * 转换为BigInteger
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static BigInteger obj2BigInteger(Object value) {
    return obj2BigInteger(value, null);
  }

  /**
   * 转换为BigInteger
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static BigInteger obj2BigInteger(Object value, BigInteger defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof BigInteger) {
      return (BigInteger) value;
    }
    if (value instanceof Long) {
      return BigInteger.valueOf((Long) value);
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      return new BigInteger(valueStr);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 转换为BigDecimal
   *
   * @param value 被转换的值
   * @return 结果
   */
  public static BigDecimal obj2BigDecimal(Object value) {
    return obj2BigDecimal(value, null);
  }

  /**
   * 转换为BigDecimal
   *
   * @param value        被转换的值
   * @param defaultValue 转换默认值
   * @return 结果
   */
  public static BigDecimal obj2BigDecimal(Object value, BigDecimal defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    }
    if (value instanceof Long) {
      return new BigDecimal((Long) value);
    }
    if (value instanceof Double) {
      return new BigDecimal((Double) value);
    }
    if (value instanceof Integer) {
      return new BigDecimal((Integer) value);
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      return new BigDecimal(valueStr);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 实体对象转成Map
   *
   * @param obj 实体对象
   * @return
   */
  public static Map<String, Object> obj2Map(Object obj) {
    Map<String, Object> map = new HashMap<>();
    if (Objects.isNull(obj)) {
      return map;
    }
    Class clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();
    // 获取所有父类的成员属性
    while (Objects.nonNull(clazz)) {
      clazz = clazz.getSuperclass();
      if (Objects.nonNull(clazz)) {
        Field[] superFields = clazz.getDeclaredFields();
        fields = ArrayUtils.addAll(fields, superFields);
      }
    }
    try {
      for (Field field : fields) {
        field.setAccessible(true);
        map.put(field.getName(), field.get(obj));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

  /**
   * Map转成实体对象
   *
   * @param map   map实体对象包含属性
   * @param clazz 实体对象类型
   * @return
   */
  public static Object map2Obj(Map<String, Object> map, Class<?> clazz) {
    if (Objects.isNull(map)) {
      return null;
    }
    Object obj = null;
    try {
      obj = clazz.newInstance();
      Field[] fields = obj.getClass().getDeclaredFields();
      for (Field field : fields) {
        int mod = field.getModifiers();
        if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
          continue;
        }
        field.setAccessible(true);
        field.set(obj, map.get(field.getName()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return obj;
  }

  /**
   * 转换为Integer数组
   *
   * @param str 被转换的值
   * @return 结果
   */
  public static Integer[] str2IntArray(String str) {
    return str2IntArray(",", str);
  }

  /**
   * 转换为Long数组
   *
   * @param str 被转换的值
   * @return 结果
   */
  public static Long[] str2LongArray(String str) {
    return str2LongArray(",", str);
  }

  /**
   * 转换为Integer数组
   *
   * @param split 分隔符
   * @param split 被转换的值
   * @return 结果
   */
  public static Integer[] str2IntArray(String split, String str) {
    if (StringUtil.isEmpty(str)) {
      return new Integer[]{};
    }
    String[] arr = str.split(split);
    final Integer[] ints = new Integer[arr.length];
    for (int i = 0; i < arr.length; i++) {
      final Integer v = obj2Int(arr[i], 0);
      ints[i] = v;
    }
    return ints;
  }

  /**
   * 转换为Long数组
   *
   * @param split 分隔符
   * @param str   被转换的值
   * @return 结果
   */
  public static Long[] str2LongArray(String split, String str) {
    if (StringUtil.isEmpty(str)) {
      return new Long[]{};
    }
    String[] arr = str.split(split);
    final Long[] longs = new Long[arr.length];
    for (int i = 0; i < arr.length; i++) {
      final Long v = obj2Long(arr[i], null);
      longs[i] = v;
    }
    return longs;
  }

  /**
   * 转换为String数组
   *
   * @param str 被转换的值
   * @return 结果
   */
  public static String[] str2StrArray(String str) {
    return str2StrArray(",", str);
  }

  /**
   * 转换为String数组
   *
   * @param split 分隔符
   * @param split 被转换的值
   * @return 结果
   */
  public static String[] str2StrArray(String split, String str) {
    return str.split(split);
  }

  /**
   * 转换为Enum对象
   * 如果给定的值为空，或者转换失败，返回默认值<code>null</code>
   *
   * @param clazz Enum的Class
   * @param value 值
   * @return Enum
   */
  public static <E extends Enum<E>> E clz2Enum(Class<E> clazz, Object value) {
    return clz2Enum(clazz, value, null);
  }

  /**
   * 转换为Enum对象
   * 如果给定的值为空，或者转换失败，返回默认值
   *
   * @param clazz        Enum的Class
   * @param value        值
   * @param defaultValue 默认值
   * @return Enum
   */
  public static <E extends Enum<E>> E clz2Enum(Class<E> clazz, Object value, E defaultValue) {
    if (Objects.isNull(value)) {
      return defaultValue;
    }
    if (clazz.isAssignableFrom(value.getClass())) {
      @SuppressWarnings("unchecked")
      E myE = (E) value;
      return myE;
    }
    final String valueStr = obj2Str(value, StringUtil.EMPTY);
    if (StringUtil.isEmpty(valueStr)) {
      return defaultValue;
    }
    try {
      return Enum.valueOf(clazz, valueStr);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 将对象转为字符串
   * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
   *
   * @param obj 对象
   * @return 字符串
   */
  public static String obj2UTF8Str(Object obj) {
    return obj2CharsetStr(obj, SandCharset.CHARSET_UTF_8);
  }

  /**
   * 将对象转为字符串
   * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
   *
   * @param obj         对象
   * @param charsetName 字符集
   * @return 字符串
   */
  public static String obj2CharsetStr(Object obj, String charsetName) {
    return obj2CharsetStr(obj, Charset.forName(charsetName));
  }

  /**
   * 将对象转为字符串
   * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
   * 2、对象数组会调用Arrays.toString方法
   *
   * @param obj     对象
   * @param charset 字符集
   * @return 字符串
   */
  public static String obj2CharsetStr(Object obj, Charset charset) {
    if (Objects.isNull(obj)) {
      return null;
    }
    if (obj instanceof String) {
      return (String) obj;
    } else if (obj instanceof byte[] || obj instanceof Byte[]) {
      return bytes2Str((byte[]) obj, charset);
    } else if (obj instanceof ByteBuffer) {
      return byteBuffer2Str((ByteBuffer) obj, charset);
    }
    return obj.toString();
  }

  /**
   * 将byte数组转为字符串
   *
   * @param bytes   byte数组
   * @param charset 字符集
   * @return 字符串
   */
  public static String bytes2Str(byte[] bytes, String charset) {
    return bytes2Str(bytes, StringUtil.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset));
  }

  /**
   * 解码字节码
   *
   * @param data    字符串
   * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
   * @return 解码后的字符串
   */
  public static String bytes2Str(byte[] data, Charset charset) {
    if (Objects.isNull(data)) {
      return null;
    }
    if (Objects.isNull(charset)) {
      return new String(data);
    }
    return new String(data, charset);
  }

  /**
   * 将编码的byteBuffer数据转换为字符串
   *
   * @param data    数据
   * @param charset 字符集，如果为空使用当前系统字符集
   * @return 字符串
   */
  public static String byteBuffer2Str(ByteBuffer data, String charset) {
    if (Objects.isNull(data)) {
      return null;
    }
    return byteBuffer2Str(data, Charset.forName(charset));
  }

  /**
   * 将编码的byteBuffer数据转换为字符串
   *
   * @param data    数据
   * @param charset 字符集，如果为空使用当前系统字符集
   * @return 字符串
   */
  public static String byteBuffer2Str(ByteBuffer data, Charset charset) {
    if (Objects.isNull(charset)) {
      charset = Charset.defaultCharset();
    }
    return charset.decode(data).toString();
  }

  /**
   * 半角转全角
   *
   * @param input String.
   * @return 全角字符串.
   */
  public static String DBC2SBC(String input) {
    return DBC2SBC(input, null);
  }

  /**
   * 半角转全角
   *
   * @param input         String
   * @param notConvertSet 不替换的字符集合
   * @return 全角字符串.
   */
  public static String DBC2SBC(String input, Set<Character> notConvertSet) {
    char c[] = input.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (Objects.nonNull(notConvertSet) && notConvertSet.contains(c[i])) {
        continue;
      }
      if (c[i] == ' ') {
        c[i] = '\u3000';
      } else if (c[i] < '\177') {
        c[i] = (char) (c[i] + 65248);

      }
    }
    return new String(c);
  }

  /**
   * 全角转半角
   *
   * @param input String.
   * @return 半角字符串
   */
  public static String SBC2DBC(String input) {
    return SBC2DBC(input, null);
  }

  /**
   * 替换全角为半角
   *
   * @param text          文本
   * @param notConvertSet 不替换的字符集合
   * @return 替换后的字符
   */
  public static String SBC2DBC(String text, Set<Character> notConvertSet) {
    char c[] = text.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (Objects.nonNull(notConvertSet) && notConvertSet.contains(c[i])) {
        continue;
      }
      if (c[i] == '\u3000') {
        c[i] = ' ';
      } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
        c[i] = (char) (c[i] - 65248);
      }
    }
    String returnString = new String(c);
    return returnString;
  }

  /**
   * 数字金额大写转换，精确到分
   * <pre>
   *   System.out.println(SandConvert.digitUppercase(1234567890.987654321)); = "壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元玖角捌分"
   * </pre>
   * @param value 数字
   * @return 中文大写数字
   */
  public static String digitUppercase(double value) {
    String[] fraction = {"角", "分"};
    String[] digit = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    String[][] unit = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};

    String head = value < 0 ? "负" : "";
    value = Math.abs(value);

    String s = StringUtil.EMPTY;
    for (int i = 0; i < fraction.length; i++) {
      s += (digit[(int) (Math.floor(value * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
    }
    if (s.length() < 1) {
      s = "整";
    }
    int integerPart = (int) Math.floor(value);

    for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
      String p = StringUtil.EMPTY;
      for (int j = 0; j < unit[1].length && value > 0; j++) {
        p = digit[integerPart % 10] + unit[1][j] + p;
        integerPart = integerPart / 10;
      }
      s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
    }
    return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", StringUtil.EMPTY).replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
  }

}
