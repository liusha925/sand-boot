/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/15   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.lang3;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 功能说明：数字操作工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/15 14:11
 * 功能描述：加减乘除运算，继承org.apache.commons.lang3.math.NumberUtils类
 */
public class NumberUtil extends NumberUtils {
  /**
   * 默认精确位数
   */
  public static final int DEFAULT_SCALE = 10;

  public NumberUtil() {
    super();
  }

  /**
   * 加法运算
   * <pre>
   *   System.out.println(NumberUtil.add(1, 1)); = 2.0
   *   System.out.println(NumberUtil.add(1, 1.0)); = 2.0
   *   System.out.println(NumberUtil.add(1, 1.00)); = 2.0
   *   System.out.println(NumberUtil.add(1.0, 1.0)); = 2.0
   *   System.out.println(NumberUtil.add(1.0, 1.00)); = 2.0
   * </pre>
   *
   * @param x x
   * @param y y
   * @return 运算结果
   */
  public static double add(double x, double y) {
    BigDecimal a = BigDecimal.valueOf(x);
    BigDecimal b = BigDecimal.valueOf(y);
    return a.add(b).doubleValue();
  }

  /**
   * 加法运算
   * <pre>
   *   System.out.println(NumberUtil.add("1", "1")); = "2"
   *   System.out.println(NumberUtil.add("1", "1.0")); = "2.0"
   *   System.out.println(NumberUtil.add("1", "1.00")); = "2.00"
   *   System.out.println(NumberUtil.add("1.0", "1.0")); = "2.0"
   *   System.out.println(NumberUtil.add("1.0", "1.00")); = "2.00"
   * </pre>
   *
   * @param x x
   * @param y y
   * @return 运算结果
   */
  public static String add(String x, String y) {
    if (isNotNumeric(x) || isNotNumeric(y)) {
      throw new IllegalArgumentException("illegal arguments");
    }
    BigDecimal a = new BigDecimal(x);
    BigDecimal b = new BigDecimal(y);
    return a.add(b).toPlainString();
  }

  /**
   * 减法运算
   * <pre>
   *   System.out.println(NumberUtil.subtract(1, 1)); = 0.0
   *   System.out.println(NumberUtil.subtract(1, 1.0)); = 0.0
   *   System.out.println(NumberUtil.subtract(1, 1.00)); = 0.0
   *   System.out.println(NumberUtil.subtract(1.0, 1.0)); = 0.0
   *   System.out.println(NumberUtil.subtract(1.0, 1.00)); = 0.0
   * </pre>
   *
   * @param x x
   * @param y y
   * @return 运算结果
   */
  public static double subtract(double x, double y) {
    BigDecimal a = BigDecimal.valueOf(x);
    BigDecimal b = BigDecimal.valueOf(y);
    return a.subtract(b).doubleValue();
  }

  /**
   * 减法运算
   * <pre>
   *   System.out.println(NumberUtil.subtract("1", "1")); = "0"
   *   System.out.println(NumberUtil.subtract("1", "1.0")); = "0.0"
   *   System.out.println(NumberUtil.subtract("1", "1.00")); = "0.00"
   *   System.out.println(NumberUtil.subtract("1.0", "1.0")); = "0.0"
   *   System.out.println(NumberUtil.subtract("1.0", "1.00")); = "0.00"
   * </pre>
   *
   * @param x x
   * @param y y
   * @return 运算结果
   */
  public static String subtract(String x, String y) {
    if (isNotNumeric(x) || isNotNumeric(y)) {
      throw new IllegalArgumentException("illegal arguments");
    }
    BigDecimal a = new BigDecimal(x);
    BigDecimal b = new BigDecimal(y);
    return a.subtract(b).toPlainString();
  }

  /**
   * 乘法运算
   * <pre>
   *   System.out.println(NumberUtil.multiply(1, 1)); = 1.0
   *   System.out.println(NumberUtil.multiply(1, 1.0)); = 1.0
   *   System.out.println(NumberUtil.multiply(1, 1.00)); = 1.0
   *   System.out.println(NumberUtil.multiply(1.0, 1.0)); = 1.0
   *   System.out.println(NumberUtil.multiply(1.0, 1.00)); = 1.0
   * </pre>
   *
   * @param x x
   * @param y y
   * @return 运算结果
   */
  public static double multiply(double x, double y) {
    BigDecimal a = BigDecimal.valueOf(x);
    BigDecimal b = BigDecimal.valueOf(y);
    return a.multiply(b).doubleValue();
  }

  /**
   * 乘法运算
   * <pre>
   *   System.out.println(NumberUtil.multiply("1", "1")); = "1"
   *   System.out.println(NumberUtil.multiply("1", "1.0")); = "1.0"
   *   System.out.println(NumberUtil.multiply("1", "1.00")); = "1.00"
   *   System.out.println(NumberUtil.multiply("1.0", "1.0")); = "1.00"
   *   System.out.println(NumberUtil.multiply("1.0", "1.00")); = "1.000"
   * </pre>
   *
   * @param x x
   * @param y y
   * @return 远算结果
   */
  public static String multiply(String x, String y) {
    if (isNotNumeric(x) || isNotNumeric(y)) {
      throw new IllegalArgumentException("illegal arguments");
    }
    BigDecimal a = new BigDecimal(x);
    BigDecimal b = new BigDecimal(y);
    return a.multiply(b).toPlainString();
  }

  /**
   * 除法运算
   * <pre>
   *   System.out.println(NumberUtil.divide(2, 3)); = 0.6666666667
   *   System.out.println(NumberUtil.divide(2, 3.0)); = 0.6666666667
   *   System.out.println(NumberUtil.divide(2, 3.00)); = 0.6666666667
   *   System.out.println(NumberUtil.divide(2.0, 3.0)); = 0.6666666667
   *   System.out.println(NumberUtil.divide(2.0, 3.00)); = 0.6666666667
   * </pre>
   *
   * @param x x
   * @param y y
   * @return 运算结果
   */
  public static double divide(double x, double y) {
    return divide(x, y, DEFAULT_SCALE);
  }

  /**
   * 除法运算
   * <pre>
   *   System.out.println(NumberUtil.divide("1", "1")); = "1.0000000000"
   *   System.out.println(NumberUtil.divide("1", "1.0")); = "1.0000000000"
   *   System.out.println(NumberUtil.divide("1", "1.00")); = "1.0000000000"
   *   System.out.println(NumberUtil.divide("1.0", "1.0")); = "1.0000000000"
   *   System.out.println(NumberUtil.divide("1.0", "1.00")); = "1.0000000000"
   * </pre>
   *
   * @param x x
   * @param y y
   * @return 运算结果
   */
  public static String divide(String x, String y) {
    return divide(x, y, DEFAULT_SCALE);
  }

  /**
   * 除法运算
   * <pre>
   *   System.out.println(NumberUtil.divide(2, 3, 2)); = 0.67
   *   System.out.println(NumberUtil.divide(2, 3.0, 2)); = 0.67
   *   System.out.println(NumberUtil.divide(2, 3.00, 2)); = 0.67
   *   System.out.println(NumberUtil.divide(2.0, 3.0, 2)); = 0.67
   *   System.out.println(NumberUtil.divide(2.0, 3.00, 2)); = 0.67
   * </pre>
   *
   * @param x     x
   * @param y     y
   * @param scale 保留小数位数
   * @return 运算结果
   */
  public static double divide(double x, double y, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal a = BigDecimal.valueOf(x);
    BigDecimal b = BigDecimal.valueOf(y);
    if (a.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO.doubleValue();
    }
    return a.divide(b, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  /**
   * 除法运算
   * <pre>
   *   System.out.println(NumberUtil.divide("1", "1", 2)); = "1.00"
   *   System.out.println(NumberUtil.divide("1", "1.0", 2)); = "1.00"
   *   System.out.println(NumberUtil.divide("1", "1.00", 2)); = "1.00"
   *   System.out.println(NumberUtil.divide("1.0", "1.0", 2)); = "1.00"
   *   System.out.println(NumberUtil.divide("1.0", "1.00", 2)); = "1.00"
   * </pre>
   *
   * @param x     x
   * @param y     y
   * @param scale 保留小数位数
   * @return 运算结果
   */
  public static String divide(String x, String y, int scale) {
    if (isNotNumeric(x) || isNotNumeric(y)) {
      throw new IllegalArgumentException("illegal arguments");
    }
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal a = new BigDecimal(x);
    BigDecimal b = new BigDecimal(y);
    if (a.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO.toPlainString();
    }
    return a.divide(b, scale, BigDecimal.ROUND_HALF_UP).toPlainString();
  }

  /**
   * 判断是否为数值类型
   * <pre>
   *   System.out.println(NumberUtil.isNumeric("")); = false
   *   System.out.println(NumberUtil.isNumeric(null)); = false
   *   System.out.println(NumberUtil.isNumeric("-1")); = true
   *   System.out.println(NumberUtil.isNumeric("0")); = true
   *   System.out.println(NumberUtil.isNumeric("1")); = true
   *   System.out.println(NumberUtil.isNumeric("1.0")); = true
   *   System.out.println(NumberUtil.isNumeric("1.1")); = true
   *   System.out.println(NumberUtil.isNumeric("aaa")); = false
   *   System.out.println(NumberUtil.isNumeric("呵呵")); = false
   * </pre>
   *
   * @param str 字符串
   * @return true-为数值类型 false-不为数值类型
   */
  public static boolean isNumeric(String str) {
    if (StringUtils.isBlank(str)) {
      return false;
    }
    String regex = "^[0-9]*.[0-9]*$";
    Pattern pattern = Pattern.compile(regex);
    return pattern.matcher(str).matches();
  }

  /**
   * 判断是否为数值类型
   *
   * @param str 字符串
   * @return true-不为数值类型 false-为数值类型
   */
  public static boolean isNotNumeric(String str) {
    return !isNumeric(str);
  }

  /**
   * 判断是否为0或者正整数
   * <pre>
   *   System.out.println(NumberUtil.isZeroOrPositiveInteger("")); = false
   *   System.out.println(NumberUtil.isZeroOrPositiveInteger(null)); = false
   *   System.out.println(NumberUtil.isZeroOrPositiveInteger("-1")); = false
   *   System.out.println(NumberUtil.isZeroOrPositiveInteger("0")); = true
   *   System.out.println(NumberUtil.isZeroOrPositiveInteger("1")); = true
   *   System.out.println(NumberUtil.isZeroOrPositiveInteger("1.0")); = false
   *   System.out.println(NumberUtil.isZeroOrPositiveInteger("1.1")); = false
   *   System.out.println(NumberUtil.isZeroOrPositiveInteger("aaa")); = false
   *   System.out.println(NumberUtil.isZeroOrPositiveInteger("呵呵")); = false
   * </pre>
   *
   * @param str 字符串
   * @return true-为0或者正整数 false-不为0或者正整数
   */
  public static boolean isZeroOrPositiveInteger(String str) {
    if (StringUtils.isNotBlank(str) && StringUtils.isNumeric(str)) {
      return true;
    }
    return false;
  }

}
