/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.annotation;

import com.sand.base.enums.DateEnum;
import com.sand.base.util.lang3.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能说明：自定义excel注解
 * 开发人员：@author liusha
 * 开发日期：2019/9/27 8:11
 * 功能描述：用于excel导入导出
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAnnotation {
  /**
   * 导出到excel中的表头名称
   */
  String name() default "表头名称";

  /**
   * 导出到excel中的列宽，单位为字符
   */
  double width() default 16;

  /**
   * 导出到excel中的列高，单位为字符
   */
  double height() default 14;

  /**
   * 设置了提示信息则鼠标放上去提示
   */
  String prompt() default "提示信息！";

  /**
   * 设置只能选择不能输入的列内容
   */
  String[] combo() default {};

  /**
   * 是否导出数据
   */
  boolean isExport() default true;

  /**
   * 日期格式化，默认yyyy-MM-dd HH:mm:ss
   */
  DateEnum dataFormat() default DateEnum.F1_YYYY_MM_DD_HH_MM_SS;

  /**
   * 读取内容转表达式，如：0=男,1=女,2=未知
   */
  String readConvertExp() default StringUtil.EMPTY;

  /**
   * 当值为空时的默认值
   */
  String defaultValue() default StringUtil.EMPTY;

  /**
   * 文字后缀，如%100变成100%
   */
  String suffix() default StringUtil.EMPTY;

  /**
   * 另一个注解类中的属性名称，支持多级获取，以.隔开
   */
  String targetAnnotation() default StringUtil.EMPTY;

  /**
   * 操作类型
   *
   * @return
   */
  Type type() default Type.ALL;

  @Getter
  @AllArgsConstructor
  enum Type {
    // 操作类型（all-导入导出，export-导出，import-导入，downTemplate-下载模板）
    ALL("all"), EXPORT("export"), IMPORT("import"), DOWN_TEMPLATE("downTemplate");
    private final String value;
  }
}
