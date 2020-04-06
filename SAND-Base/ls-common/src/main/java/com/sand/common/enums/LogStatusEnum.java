/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/24    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能说明：日志状态枚举
 * 开发人员：@author liusha
 * 开发日期：2019/9/24 13:42
 * 功能描述：日志可能出现的状态
 */
@Getter
@AllArgsConstructor
public enum LogStatusEnum {
  // 日志状态
  INIT(0, "初始化"),
  SUCCESS(1, "执行成功"),
  EXCEPTION(-1, "出现异常");

  private int status;
  private String description;
}
