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
 * 功能说明：日期格式枚举类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 14:15
 * 功能描述：日期格式枚举类
 */
@Getter
@AllArgsConstructor
public enum DateEnum {
  // General Format
  F_YYYY("yyyy"),
  F_MM("MM"),
  F_DD("dd"),
  // E表示星期
  F_E("E"),
  F_HH_MM_SS("HH:mm:ss"),
  // Date Format One,
  F1_MM_DD("MM-dd"),
  F1_YYYY_MM("yyyy-MM"),
  F1_YYYY_MM_DD("yyyy-MM-dd"),
  F1_YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
  F1_YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
  F1_YYYY_MM_DD_HH_MM_SS_SSS("yyyy-MM-dd HH:mm:ss.SSS"),
  // Date Format Two
  F2_MM_DD("MMdd"),
  F2_YYYY_MM("yyyyMM"),
  F2_YYYY_MM_DD("yyyyMMdd"),
  F2_YYYY_MM_DD_HH_MM("yyyyMMdd HH:mm"),
  F2_YYYY_MM_DD_HH_MM_SS("yyyyMMdd HH:mm:ss"),
  F2_YYYY_MM_DD_HH_MM_SS_SSS("yyyyMMdd HH:mm:ss.SSS"),
  // Date Format Three
  F3_MM_DD("MM.dd"),
  F3_YYYY_MM("yyyy.MM"),
  F3_YYYY_MM_DD("yyyy.MM.dd"),
  F3_YYYY_MM_DD_HH_MM("yyyy.MM.dd HH:mm"),
  F3_YYYY_MM_DD_HH_MM_SS("yyyy.MM.dd HH:mm:ss"),
  F3_YYYY_MM_DD_HH_MM_SS_SSS("yyyy.MM.dd HH:mm:ss.SSS"),
  // Date Format Four
  F4_MM_DD("MM/dd"),
  F4_YYYY_MM("yyyy/MM"),
  F4_YYYY_MM_DD("yyyy/MM/dd"),
  F4_YYYY_MM_DD_HH_MM("yyyy/MM/dd HH:mm"),
  F4_YYYY_MM_DD_HH_MM_SS("yyyy/MM/dd HH:mm:ss"),
  F4_YYYY_MM_DD_HH_MM_SS_SSS("yyyy/MM/dd HH:mm:ss.SSS"),
  // Date Format Five
  F5_YYYY_MM_DD_HH_MM_SS_SSS("yyyyMMddHHmmssSSS"),
  // Date Format Six
  F6_MM_DD("MM月dd日"),
  F6_YYYY_MM("yyyy年MM月"),
  F6_YYYY_MM_DD("yyyy年MM月dd日"),
  ;
  private final String pattern;

  public static String[] getPatterns() {
    String[] patterns = new String[DateEnum.values().length];
    for (int i = 0, len = DateEnum.values().length; i < len; i++) {
      patterns[i] = DateEnum.values()[i].pattern;
    }
    return patterns;
  }

  public static DateEnum getDateEnum(String pattern) {
    for (DateEnum item : DateEnum.values()) {
      if (pattern.equals(item.pattern)) {
        return item;
      }
    }
    return null;
  }

}
