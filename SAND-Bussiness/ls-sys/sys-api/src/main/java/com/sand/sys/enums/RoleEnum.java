/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 功能说明：存放系统角色枚举类
 * 开发人员：@author liusha
 * 开发日期：2019/8/30 15:55
 * 功能描述：申明通用枚举属性将字段响应值返给客户端，参考https://mp.baomidou.com/guide/enum.html
 */
public final class RoleEnum {
  @Getter
  @AllArgsConstructor
  public enum Status implements IEnum<String> {
    // 角色状态
    NORMAL("0", "正常"),
    DISABLE("1", "停用");

    private final String status;
    @JsonValue
    private final String description;

    @Override
    public String getValue() {
      return this.status;
    }

    public static Status getByStatus(Status status) {
      for (Status item : Status.values()) {
        if (Objects.equals(status, item)) {
          return item;
        }
      }
      return null;
    }
  }

  @Getter
  @AllArgsConstructor
  public enum DelFlag implements IEnum<String> {
    // 删除标志
    DEL("1", "已删除"),
    NOT_DEL("0", "未删除");

    private final String flag;
    @JsonValue
    private final String description;

    @Override
    public String getValue() {
      return this.flag;
    }

    public static DelFlag getByFlag(DelFlag flag) {
      for (DelFlag item : DelFlag.values()) {
        if (Objects.equals(flag, item)) {
          return item;
        }
      }
      return null;
    }
  }

  @Getter
  @AllArgsConstructor
  public enum DataScope implements IEnum<String> {
    // 数据范围
    ALL("1", "全部数据权限"),
    CUSTOM("2", "自定数据权限"),
    DEPARTMENT("3", "本部门数据权限"),
    DEPARTMENT_UNDER("4", "本部门及以下数据权限");

    private final String scope;
    @JsonValue
    private final String description;

    @Override
    public String getValue() {
      return this.scope;
    }

    public static DataScope getByScope(DataScope scope) {
      for (DataScope item : DataScope.values()) {
        if (Objects.equals(scope, item)) {
          return item;
        }
      }
      return null;
    }
  }
}
