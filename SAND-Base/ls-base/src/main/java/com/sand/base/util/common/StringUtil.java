/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.common;

import com.sand.base.enums.DateEnum;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能说明：字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 13:36
 * 功能描述：字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 */
public class StringUtil extends StringUtils {

  /**
   * 字符连接符
   */
  private static final char SEPARATOR = '_';
  /**
   * 默认编码
   */
  private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

  /**
   * 产生唯一的序列号。
   *
   * @return
   */
  public static synchronized String getSerialNo() {
    int hashCode = UUID.randomUUID().toString().hashCode();
    String nowTime = DateUtil.getNow(DateEnum.F5_YYYY_MM_DD_HH_MM_SS_SSS.getPattern());
    String codeStr = String.format("%010d", hashCode < 0 ? -hashCode : hashCode);
    return nowTime + codeStr;
  }

  /**
   * 转换为字节数组
   *
   * @param str 字符串
   * @return byte[] byte [ ]
   */
  public static byte[] getBytes(String str) {
    if (str == null) {
      throw new NullPointerException("str cannot be null");
    }
    return str.getBytes(DEFAULT_CHARSET);
  }

  /**
   * 转换为字节数组
   *
   * @param bytes 字节数组
   * @return String string
   */
  public static String toString(byte[] bytes) {
    return new String(bytes, DEFAULT_CHARSET);
  }

  /**
   * 是否包含字符串
   *
   * @param str  验证字符串
   * @param strs 字符串组
   * @return 包含返回true boolean
   */
  public static boolean inString(String str, String... strs) {
    if (str != null) {
      for (String s : strs) {
        if (str.equals(trim(s))) {
          return true;
        }
      }
    }
    return false;
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

  /**
   * 驼峰命名法工具
   *
   * @param s the s
   * @return String  toCamelCase("hello_world") == "helloWorld"
   * toCapitalizeCamelCase("hello_world") == "HelloWorld"
   * toUnderScoreCase("helloWorld") = "hello_world"
   */
  public static String toCamelCase(String s) {
    if (s == null) {
      return null;
    }

    String ls = s.toLowerCase();

    StringBuilder sb = new StringBuilder(ls.length());
    boolean upperCase = false;
    for (int i = 0; i < ls.length(); i++) {
      char c = ls.charAt(i);

      if (c == SEPARATOR) {
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
   * @param s the s
   * @return String  toCamelCase("hello_world") == "helloWorld"
   * toCapitalizeCamelCase("hello_world") == "HelloWorld"
   * toUnderScoreCase("helloWorld") = "hello_world"
   */
  public static String toCapitalizeCamelCase(String s) {
    if (s == null) {
      return null;
    }
    String cs = toCamelCase(s);
    return cs.substring(0, 1).toUpperCase() + cs.substring(1);
  }

  /**
   * 驼峰命名法工具
   *
   * @param s the s
   * @return String  toCamelCase("hello_world") == "helloWorld"
   * toCapitalizeCamelCase("hello_world") == "HelloWorld"
   * toUnderScoreCase("helloWorld") = "hello_world"
   */
  public static String toUnderScoreCase(String s) {
    if (s == null) {
      return null;
    }

    StringBuilder sb = new StringBuilder();
    boolean upperCase = false;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      boolean nextUpperCase = true;

      if (i < (s.length() - 1)) {
        nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
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
   * 首字母大写
   *
   * @param str
   * @return
   */
  public static String upperCaseFirstLetter(String str) {
    char[] strChar = str.toCharArray();
    strChar[0] -= 32;
    return String.valueOf(strChar);
  }

  /**
   * Trim to default string.
   *
   * @param str          字符串
   * @param defaultValue 默认值
   * @return String string
   */
  public static String trimToDefault(final String str, String defaultValue) {
    final String ts = trim(str);
    return isEmpty(ts) ? defaultValue : ts;
  }

  /**
   * 获取随机字符串
   *
   * @param length
   * @return
   */
  public static String getRandomString(int length) {
    String val = "";
    Random random = new Random();
    // 参数length，表示生成几位随机数
    for (int i = 0; i < length; i++) {
      String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
      // 输出字母还是数字
      if ("char".equalsIgnoreCase(charOrNum)) {
        // 输出是大写字母还是小写字母
        int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
        val += (char) (random.nextInt(26) + temp);
      } else if ("num".equalsIgnoreCase(charOrNum)) {
        val += String.valueOf(random.nextInt(10));
      }
    }
    return val;
  }

  public static final boolean isNotBlank(String s) {
    return !isBlank(s);
  }

  public static final boolean isBlank(String s) {
    if (Objects.isNull(s) || Objects.equals(s.trim(), "")) {
      return true;
    }
    return false;
  }

  /**
   * 将对象转为字符串
   *
   * @param o
   * @return
   */
  public static String isNull(Object o) {
    if (o == null) {
      return "";
    }
    String str = "";
    if (o instanceof String) {
      str = (String) o;
    } else {
      str = o.toString();
    }
    return str.trim();
  }

  /**
   * 字符串部分信息遮盖
   *
   * @param str
   * @return
   */
  public static String replacePart(String str) {
    return replacePart(str, "*");
  }

  /**
   * 字符串部分信息遮盖
   *
   * @param str
   * @param mask
   * @return
   */
  public static String replacePart(String str, String mask) {
    return replacePart(str, 3, 4, mask);
  }

  /**
   * 字符串部分信息遮盖
   *
   * @param str
   * @param start
   * @param length
   * @param mask
   * @return
   */
  public static String replacePart(String str, int start, int length, String mask) {
    if (Objects.isNull(str)) {
      return null;
    }
    if (start >= str.length()) {
      return str;
    }
    StringBuilder sb = new StringBuilder(str.substring(0, start));
    if (length > str.length() - start) {
      length = str.length() - start;
    }
    for (int i = 0; i < length; i++) {
      sb.append(mask);
    }
    sb.append(str.substring(start + length));
    return sb.toString();
  }

  /**
   * 获得固定位数的 随机字符串
   *
   * @param number
   * @return
   */
  public static String getRandomNumCode(int number) {
    String codeNum = "";
    int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    Random random = new Random();
    for (int i = 0; i < number; i++) {
      // 目的是产生足够随机的数，避免产生的数字重复率高的问题
      int next = random.nextInt(10000);
      codeNum += numbers[next % 10];
    }
    return codeNum;
  }
}
