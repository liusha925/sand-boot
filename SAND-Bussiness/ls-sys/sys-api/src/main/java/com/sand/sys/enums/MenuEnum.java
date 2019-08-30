/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/28    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 功能说明：存放菜单枚举类
 * 开发人员：@author liusha
 * 开发日期：2019/8/28 19:48
 * 功能描述：申明通用枚举属性将字段响应值返给客户端，参考https://mp.baomidou.com/guide/enum.html
 */
public final class MenuEnum {
  @Getter
  @AllArgsConstructor
  public enum MenuType implements IEnum<String> {
    // 菜单类型
    M("M", "目录"),
    C("C", "菜单"),
    F("F", "按钮");
    private String type;
    @JsonValue
    private String description;

    @Override
    public String getValue() {
      return this.type;
    }

    public static MenuType getByType(MenuType type) {
      for (MenuType item : MenuType.values()) {
        if (Objects.equals(type, item)) {
          return item;
        }
      }
      return null;
    }

  }

  @Getter
  @AllArgsConstructor
  public enum Target implements IEnum<String> {
    // 打开方式
    ITEM("_item", "页签中打开"),
    BLANK("_blank", "新窗口打开"),
    CURRENT("_current", "当前窗口打开");
    private String target;
    @JsonValue
    private String description;

    @Override
    public String getValue() {
      return this.target;
    }
  }

  @Getter
  @AllArgsConstructor
  public enum Visible implements IEnum<String> {
    // 菜单状态
    SHOW("0", "显示"),
    HIDE("1", "隐藏");
    private String status;
    @JsonValue
    private String description;

    @Override
    public String getValue() {
      return this.status;
    }
  }

}
