/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/15   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.common;

import com.sand.base.enums.RegexEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 功能说明：数字操作工具类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/15 14:11
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Getter
@AllArgsConstructor
public enum NumberUtil {

  /**
   * 加法运算
   */
  PLUS("+") {
    @Override
    public double apply(double x, double y) {
      BigDecimal a = BigDecimal.valueOf(x);
      BigDecimal b = BigDecimal.valueOf(y);
      return a.add(b).doubleValue();
    }

    @Override
    public String apply(String x, String y) {
      if (isNumeric(x) || isNumeric(y)) {
        throw new IllegalArgumentException("illegal arguments");
      }
      BigDecimal a = new BigDecimal(x);
      BigDecimal b = new BigDecimal(y);
      return a.add(b).toPlainString();
    }
  },
  /**
   * 减法运算
   */
  MINUS("-") {
    @Override
    public double apply(double x, double y) {
      BigDecimal a = BigDecimal.valueOf(x);
      BigDecimal b = BigDecimal.valueOf(y);
      return a.subtract(b).doubleValue();
    }

    @Override
    public String apply(String x, String y) {
      if (isNumeric(x) || isNumeric(y)) {
        throw new IllegalArgumentException("illegal arguments");
      }
      BigDecimal a = new BigDecimal(x);
      BigDecimal b = new BigDecimal(y);
      return a.subtract(b).toPlainString();
    }
  },
  /**
   * 乘法运算
   */
  TIMES("*") {
    @Override
    public double apply(double x, double y) {
      BigDecimal a = BigDecimal.valueOf(x);
      BigDecimal b = BigDecimal.valueOf(y);
      return a.multiply(b).doubleValue();
    }

    @Override
    public String apply(String x, String y) {
      if (isNumeric(x) || isNumeric(y)) {
        throw new IllegalArgumentException("illegal arguments");
      }
      BigDecimal a = new BigDecimal(x);
      BigDecimal b = new BigDecimal(y);
      return a.multiply(b).toPlainString();
    }
  },
  /**
   * 除法运算
   */
  DIVIDE("/") {
    @Override
    public double apply(double x, double y) {
      return divide(x, y, 10);
    }

    @Override
    public String apply(String x, String y) {
      return divide(x, y, 10);
    }
  },
  ;

  /**
   * 操作标识
   */
  private final String symbol;

  /**
   * double类型
   *
   * @param x
   * @param y
   * @return
   */
  public abstract double apply(double x, double y);

  /**
   * String类型
   *
   * @param x
   * @param y
   * @return
   */
  public abstract String apply(String x, String y);

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

  /**
   * 除法运算
   *
   * @param x
   * @param y
   * @param scale
   * @return
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
   *
   * @param x
   * @param y
   * @param scale
   * @return
   */
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

}
