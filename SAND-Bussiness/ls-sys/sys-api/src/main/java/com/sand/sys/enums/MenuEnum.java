/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/28    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 功能说明：存放菜单枚举类
 * 开发人员：@author liusha
 * 开发日期：2019/8/28 19:48
 * 功能描述：存放菜单枚举类
 */
public final class MenuEnum {
  @Getter
  @AllArgsConstructor
  public enum MenuType {
    // 菜单类型
    M("M", "目录"),
    C("C", "菜单"),
    F("F", "按钮");
    private String type;
    private String description;

    public static MenuType getByType(String type) {
      for (MenuType item : MenuType.values()) {
        if (Objects.equals(type, item.getType())) {
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
    private String target;
    private String description;

  }

  @Getter
  @AllArgsConstructor
  public enum Visible {
    // 菜单状态
    SHOW("0", "显示"),
    HIDE("1", "隐藏");
    private String status;
    private String description;
  }

}
