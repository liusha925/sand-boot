/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能说明：操作类型枚举类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 9:36
 * 功能描述：操作类型枚举类
 */
@Getter
@AllArgsConstructor
public enum OperateType {
  // 操作类型与说明
  INSERT("insert", "新增"),
  DELETE("delete", "删除"),
  UPDATE("update", "修改"),
  SELECT("select", "查询"),
  IMPORT("import", "导入"),
  EXPORT("export", "导出");

  private final String value;
  private final String name;
}
