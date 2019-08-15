/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/15   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.number;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 功能说明：数字操作工具类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/15 14:11
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
public enum NumberUtil implements Operation {

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
      if (!isNumeric(x) || !isNumeric(y)) {
        throw new IllegalArgumentException("illegal arguments");
      }
      BigDecimal a = new BigDecimal(x);
      BigDecimal b = new BigDecimal(y);
      return a.add(b).toPlainString();
    }
  },
  ;

  private final String symbol;

  NumberUtil(String symbol) {
    this.symbol = symbol;
  }

  public boolean isNumeric(String str) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
    return pattern.matcher(str).matches();
  }

}
