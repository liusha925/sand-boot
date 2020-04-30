/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.lang3;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

  public StringUtil() {
    super();
  }

  /**
   * 判断object是否为空或空字符串
   *
   * @param obj obj
   * @return true-为空或空字符串 flase-不为空或空字符串
   */
  public static boolean isBlank(Object obj) {
    if (Objects.isNull(obj) || isBlank(obj.toString())) {
      return true;
    }
    return false;
  }

  /**
   * 判断object是否不为空或空字符串
   *
   * @param obj obj
   * @return true-不为空或空字符串 flase-为空或空字符串
   */
  public static boolean isNotBlank(Object obj) {
    return !isBlank(obj);
  }

  /**
   * 产生唯一的序列号（27位）
   * <pre>
   *   System.out.println(StringUtil.getUniqueSerialNo()); = "202004161446356330693259868"
   * </pre>
   *
   * @return 唯一的序列号
   */
  public static synchronized String getUniqueSerialNo() {
    int hashCode = UUID.randomUUID().toString().hashCode();
    String codeStr = String.format("%010d", hashCode < 0 ? -hashCode : hashCode);
    String nowTime = DateUtil.getNow(DateUtil.Format.F5_YYYY_MM_DD_HH_MM_SS_SSS);
    return nowTime + codeStr;
  }

  /**
   * 首字母大写
   * <pre>
   *   System.out.println(StringUtil.firstToUpperCase("abcd")); = "Abcd"
   *   System.out.println(StringUtil.firstToUpperCase("ABCD")); = "!BCD"
   *   System.out.println(StringUtil.firstToUpperCase("1abcd")); = "abcd"
   *   System.out.println(StringUtil.firstToUpperCase("呵abcd")); = "呕abcd"
   * </pre>
   *
   * @param caseStr 待转换的字符串
   * @return 转换后的字符串
   */
  public static String firstToUpperCase(String caseStr) {
    char[] strChar = caseStr.toCharArray();
    strChar[0] -= 32;
    return String.valueOf(strChar);
  }

  /**
   * 驼峰命名法转换工具，hello_world ==> helloWorld
   * <pre>
   *   System.out.println(StringUtil.toCamelCase("helloworld")); = "helloworld"
   *   System.out.println(StringUtil.toCamelCase("helloWorld")); = "helloworld"
   *   System.out.println(StringUtil.toCamelCase("hello_world")); = "helloWorld"
   *   System.out.println(StringUtil.toCamelCase("hello_World")); = "helloWorld"
   *   System.out.println(StringUtil.toCamelCase("HELLO_WORLD")); = "helloWorld"
   * </pre>
   *
   * @param caseStr 待转换字符串
   * @return 转换后的字符串
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
      if (Objects.equals(c, '_')) {
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
   * 驼峰命名法转换工具，hello_world ==> HelloWorld
   * <pre>
   *   System.out.println(StringUtil.toCapitalizeCamelCase("helloworld")); = "Helloworld"
   *   System.out.println(StringUtil.toCapitalizeCamelCase("helloWorld")); = "Helloworld"
   *   System.out.println(StringUtil.toCapitalizeCamelCase("hello_world")); = "HelloWorld"
   *   System.out.println(StringUtil.toCapitalizeCamelCase("hello_World")); = "HelloWorld"
   *   System.out.println(StringUtil.toCapitalizeCamelCase("HELLO_WORLD")); = "HelloWorld"
   * </pre>
   *
   * @param caseStr 待转换字符串
   * @return 转换后的字符串
   */
  public static String toCapitalizeCamelCase(String caseStr) {
    if (Objects.isNull(caseStr)) {
      return null;
    }
    String cs = toCamelCase(caseStr);
    return cs.substring(0, 1).toUpperCase() + cs.substring(1);
  }

  /**
   * 驼峰命名法转换工具，helloWorld ==> hello_world
   * <pre>
   *   System.out.println(StringUtil.toUnderScoreCase("helloworld"));
   *   System.out.println(StringUtil.toUnderScoreCase("helloWorld"));
   *   System.out.println(StringUtil.toUnderScoreCase("hello_world"));
   *   System.out.println(StringUtil.toUnderScoreCase("hello_World"));
   *   System.out.println(StringUtil.toUnderScoreCase("HELLO_WORLD"));
   * </pre>
   *
   * @param caseStr 待转换字符串
   * @return 转换后的字符串
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
          sb.append('_');
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
   * 替换掉HTML标签
   * <pre>
   *   System.out.println(StringUtil.replaceHtml("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">" +
   *   "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">" +
   *   "    <head>" +
   *   "        <meta http-equiv=\"Content-Type\" content=\"convert/html; charset=UTF-8\" />" +
   *   "        <title>简单实例</title>" +
   *   "    </head>" +
   *   "    <body>" +
   *   "    <h1>h1标签</h1>" +
   *   "    <h2>h2标签</h2>" +
   *   "    <h3>h3标签</h3>" +
   *   "    <p>p标签</p>" +
   *   "    <div>div标签 </div>" +
   *   "    <span>span标签</span>" +
   *   "    <strong>strong标签</strong>" +
   *   "    <input type=\"convert\" value=\"input标签\" />" +
   *   "    <textarea>textarea标签</textarea>" +
   *   "    <input type=\"button\" value=\"提交\" />" +
   *   "    <ul>" +
   *   "        <li>1</li>" +
   *   "        <li>2</li>" +
   *   "        <li>3</li>" +
   *   "        <li>4</li>" +
   *   "    </ul>" +
   *   "    </body>" +
   *   "</html>"));
   *   输出结果：
   *   "                   简单实例            h1标签    h2标签    h3标签    p标签    div标签     span标签    strong标签        textarea标签                1        2        3        4"
   * </pre>
   *
   * @param html the html
   * @return the string
   */
  public static String replaceHtml(String html) {
    if (isBlank(html)) {
      return EMPTY;
    }
    String regex = "<.+?>";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(html);
    return matcher.replaceAll(EMPTY);
  }

  /**
   * List中是否包含重复记录，对应实体类需要重写hashCode和equals方法
   *
   * @param list 要校验的List
   * @param <T>  对应实体类
   * @return true：存在重复记录
   */
  public static <T> boolean listHasRepeatRecord(List<T> list) {
    Set<T> set = new HashSet<>();
    set.addAll(list);
    return list.size() != set.size();
  }

  /**
   * 扩充字符数组
   * <pre>
   *    String[] newArray = StringUtil.addStringToArray(new String[]{"1", "2"}, "3", "4");
   *    Arrays.stream(newArray).forEach(str -> System.out.print(str + " ")); = 1 2 3 4
   *
   *    String[] newArray = StringUtil.addStringToArray(null, "3", "4");
   *    Arrays.stream(newArray).forEach(str -> System.out.print(str + " ")); = 3 4
   *
   *    也可以用org.springframework.util.StringUtils.addStringToArray(String[] array, String str)
   *    或者org.springframework.util.StringUtils.concatenateStringArrays(String[] array1, String[] array2)替代
   * </pre>
   *
   * @param array 字符数组
   * @param str   字符串
   * @return 新的字符数组
   */
  public static String[] addStringToArray(String[] array, String... str) {
    String[] newArray;
    int strSize = str.length;
    if (ObjectUtils.isEmpty(array)) {
      newArray = new String[strSize];
      for (int i = 0; i < strSize; i++) {
        newArray[i] = str[i];
      }
      return newArray;
    } else {
      int arrSize = array.length;
      newArray = new String[arrSize + strSize];
      for (int i = 0; i < strSize; i++) {
        System.arraycopy(array, 0, newArray, 0, arrSize);
        newArray[arrSize + i] = str[i];
      }
      return newArray;
    }
  }

}
