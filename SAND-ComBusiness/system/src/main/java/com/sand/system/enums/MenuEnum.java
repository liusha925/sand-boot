/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/28    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.enums;

import com.sand.core.annotation.EnumValidAnnotation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 功能说明：存放系统菜单枚举类
 * 开发人员：@author liusha
 * 开发日期：2019/8/28 19:48
 * 功能描述：使用@EnumValidAnnotation可用于表单校验，使用getBy*返回给客户端转译等
 */
public final class MenuEnum {
  @Getter
  @AllArgsConstructor
  public enum Type {
    // 菜单类型
    M("M", "目录"),
    C("C", "菜单"),
    F("F", "按钮");

    @EnumValidAnnotation
    private final String value;
    private final String name;

    public static Type getByValue(String type) {
      for (Type item : Type.values()) {
        if (Objects.equals(type, item.getValue())) {
          return item;
        }
      }
      return null;
    }
  }

  @Getter
  @AllArgsConstructor
  public enum Target {
    // 打开方式
    ITEM("_item", "页签中打开"),
    BLANK("_blank", "新窗口打开"),
    CURRENT("_current", "当前窗口打开");

    @EnumValidAnnotation
    private final String value;
    private final String name;

    public static Target getByValue(String target) {
      for (Target item : Target.values()) {
        if (Objects.equals(target, item.getValue())) {
          return item;
        }
      }
      return null;
    }
  }

  @Getter
  @AllArgsConstructor
  public enum Visible {
    // 菜单状态
    SHOW("0", "显示"),
    HIDE("1", "隐藏");

    @EnumValidAnnotation
    private final String value;
    private final String name;

    public static Visible getByValue(String visible) {
      for (Visible item : Visible.values()) {
        if (Objects.equals(visible, item.getValue())) {
          return item;
        }
      }
      return null;
    }
  }
}
