/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能说明：返回结果枚举类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 10:34
 * 功能描述：返回结果枚举类
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
  // 通用结果集
  OK(200, "成功"),
  ERROR(1001, "服务器异常"),
  PARAM_MISSING_ERROR(1002, "缺少必要参数"),
  PARAM_CHECKED_ERROR(1003, "参数校验不通过"),
  READ_METHOD_ERROR(2001, "读取方法失败");

  private final int code;
  private final String msg;
}
