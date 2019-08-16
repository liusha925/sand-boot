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
 * 功能说明：操作类型枚举类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 9:36
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Getter
@AllArgsConstructor
public enum OperateEnum {
  // 操作类型与说明
  ADD("add", "新增"),
  DELETE("delete", "删除"),
  UPDATE("update", "修改"),
  SELECT("select", "查询"),
  IMPORT("import", "导入"),
  EXPORT("export", "导出");

  private final String operateType;
  private final String description;
}
