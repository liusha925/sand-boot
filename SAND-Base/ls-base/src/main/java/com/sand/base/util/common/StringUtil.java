/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.common;

import com.sand.base.enums.DateEnum;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能说明：字符串工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 13:36
 * 功能描述：字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 */
public class StringUtil extends StringUtils {
  /**
   * 字符连接符
   */
  private static final char SEPARATOR = '_';
  /**
   * 默认字符编码
   */
  private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

  public StringUtil() {
    super();
  }

  /**
   * 将对象转为字符串
   *
   * @param obj
   * @return
   */
  public static String isNull(Object obj) {
    String str = Objects.isNull(obj) ? "" : (
        (obj instanceof String) ? (String) obj : obj.toString()
    );
    return str.trim();
  }

  /**
   * 判断字符串是否为空
   *
   * @param s
   * @return
   */
  public static final boolean isBlank(String s) {
    if (Objects.isNull(s) || Objects.equals(s.trim(), "")) {
      return true;
    }
    return false;
  }

  /**
   * 判断字符串是否不为空
   *
   * @param s
   * @return
   */
  public static final boolean isNotBlank(String s) {
    return !isBlank(s);
  }

  /**
   * 字符串转换为字节数组
   *
   * @param str 字符串
   * @return byte[] byte [ ]
   */
  public static byte[] getBytes(String str) {
    if (Objects.isNull(str)) {
      throw new NullPointerException("str cannot be null");
    }
    return str.getBytes(DEFAULT_CHARSET);
  }

  /**
   * 字节数组转换为字符串
   *
   * @param bytes 字节数组
   * @return String 字符串
   */
  public static String getString(byte[] bytes) {
    return new String(bytes, DEFAULT_CHARSET);
  }

  /**
   * 产生唯一的序列号。
   *
   * @return
   */
  public static synchronized String getUniqueSerialNo() {
    int hashCode = UUID.randomUUID().toString().hashCode();
    String nowTime = DateUtil.getNow(DateEnum.F5_YYYY_MM_DD_HH_MM_SS_SSS);
    String codeStr = String.format("%010d", hashCode < 0 ? -hashCode : hashCode);
    return nowTime + codeStr;
  }

  /**
   * 首字母大写
   *
   * @param caseStr
   * @return
   */
  public static String firstToUpperCase(String caseStr) {
    char[] strChar = caseStr.toCharArray();
    strChar[0] -= 32;
    return String.valueOf(strChar);
  }

  /**
   * 驼峰命名法工具
   *
   * @param caseStr the caseStr
   * @return String  toCamelCase("hello_world") == "helloWorld"
   * toCapitalizeCamelCase("hello_world") == "HelloWorld"
   * toUnderScoreCase("helloWorld") = "hello_world"
   */
  public static String toCamelCase(String caseStr) {
    if (Objects.isNull(caseStr)) {
      return null;
    }
    String ls = caseStr.toLowerCase();
    StringBuilder sb = new StringBuilder(ls.length());
    boolean upperCase = false;
    for (int i = 0; i < ls.length(); i++) {
      char c = ls.charAt(i);
      if (Objects.equals(c, SEPARATOR)) {
        upperCase = true;
      } else if (upperCase) {
        sb.append(Character.toUpperCase(c));
        upperCase = false;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * 驼峰命名法工具
   *
   * @param caseStr the caseStr
   * @return String  toCamelCase("hello_world") == "helloWorld"
   * toCapitalizeCamelCase("hello_world") == "HelloWorld"
   * toUnderScoreCase("helloWorld") = "hello_world"
   */
  public static String toCapitalizeCamelCase(String caseStr) {
    if (Objects.isNull(caseStr)) {
      return null;
    }
    String cs = toCamelCase(caseStr);
    return cs.substring(0, 1).toUpperCase() + cs.substring(1);
  }

  /**
   * 驼峰命名法工具
   *
   * @param caseStr the caseStr
   * @return String  toCamelCase("hello_world") == "helloWorld"
   * toCapitalizeCamelCase("hello_world") == "HelloWorld"
   * toUnderScoreCase("helloWorld") = "hello_world"
   */
  public static String toUnderScoreCase(String caseStr) {
    if (Objects.isNull(caseStr)) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    boolean upperCase = false;
    for (int i = 0; i < caseStr.length(); i++) {
      char c = caseStr.charAt(i);
      boolean nextUpperCase = true;
      if (i < (caseStr.length() - 1)) {
        nextUpperCase = Character.isUpperCase(caseStr.charAt(i + 1));
      }
      if ((i > 0) && Character.isUpperCase(c)) {
        if (!upperCase || !nextUpperCase) {
          sb.append(SEPARATOR);
        }
        upperCase = true;
      } else {
        upperCase = false;
      }
      sb.append(Character.toLowerCase(c));
    }
    return sb.toString();
  }

  /**
   * 替换掉HTML标签方法
   *
   * @param html the html
   * @return the string
   */
  public static String replaceHtml(String html) {
    if (isBlank(html)) {
      return "";
    }
    String regEx = "<.+?>";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(html);
    return m.replaceAll("");
  }

}
