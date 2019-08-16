/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能说明：返回结果枚举类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 10:34
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
  // 通用结果集
  SUCCESS(200, "成功"),
  ERROR(1001, "服务器错误");

  private int code;
  private String msg;
}
