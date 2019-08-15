/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/15   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.number;

/**
 * 功能说明：操作接口
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/15 14:13
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
public interface Operation {
  /**
   * double类型
   *
   * @param x
   * @param y
   * @return
   */
  double apply(double x, double y);

  /**
   * String类型
   *
   * @param x
   * @param y
   * @return
   */
  String apply(String x, String y);

}
