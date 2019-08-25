/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/15   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.common;

import com.sand.base.enums.RegexEnum;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 功能说明：数字操作工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/15 14:11
 * 功能描述：继承org.apache.commons.lang3.math.NumberUtils类
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
   *
   * @param x
   * @param y
   * @return
   */
  public double add(double x, double y) {
    BigDecimal a = BigDecimal.valueOf(x);
    BigDecimal b = BigDecimal.valueOf(y);
    return a.add(b).doubleValue();
  }

  public String add(String x, String y) {
    if (isNumeric(x) || isNumeric(y)) {
      throw new IllegalArgumentException("illegal arguments");
    }
    BigDecimal a = new BigDecimal(x);
    BigDecimal b = new BigDecimal(y);
    return a.add(b).toPlainString();
  }

  /**
   * 减法运算
   *
   * @param x
   * @param y
   * @return
   */
  public double subtract(double x, double y) {
    BigDecimal a = BigDecimal.valueOf(x);
    BigDecimal b = BigDecimal.valueOf(y);
    return a.subtract(b).doubleValue();
  }

  public String subtract(String x, String y) {
    if (isNumeric(x) || isNumeric(y)) {
      throw new IllegalArgumentException("illegal arguments");
    }
    BigDecimal a = new BigDecimal(x);
    BigDecimal b = new BigDecimal(y);
    return a.subtract(b).toPlainString();
  }

  /**
   * 乘法运算
   *
   * @param x
   * @param y
   * @return
   */
  public double multiply(double x, double y) {
    BigDecimal a = BigDecimal.valueOf(x);
    BigDecimal b = BigDecimal.valueOf(y);
    return a.multiply(b).doubleValue();
  }

  public String multiply(String x, String y) {
    if (isNumeric(x) || isNumeric(y)) {
      throw new IllegalArgumentException("illegal arguments");
    }
    BigDecimal a = new BigDecimal(x);
    BigDecimal b = new BigDecimal(y);
    return a.multiply(b).toPlainString();
  }

  /**
   * 除法运算
   *
   * @param x
   * @param y
   * @return
   */
  public double divide(double x, double y) {
    return divide(x, y, DEFAULT_SCALE);
  }

  public String divide(String x, String y) {
    return divide(x, y, DEFAULT_SCALE);
  }

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

  public static String divide(String x, String y, int scale) {
    if (isNumeric(x) || isNumeric(y)) {
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
   * 判断是否为数字
   *
   * @param str
   * @return
   */
  public static boolean isNumeric(String str) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    Pattern pattern = Pattern.compile(RegexEnum.NUMBER.getExpression());
    return pattern.matcher(str).matches();
  }

}
