/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sand.base.constant.Constant;
import com.sand.base.core.entity.BaseEntity;
import com.sand.sys.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 功能说明：系统角色
 * 开发人员：@author liusha
 * 开发日期：2019/8/30 13:10
 * 功能描述：系统角色
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(Constant.TABLE_PREFIX_SYS + "role")
public class SysRole extends BaseEntity {
  private static final long serialVersionUID = 9084971943250909716L;
  /**
   * 角色ID
   **/
  @TableId
  private String roleId;
  /**
   * 角色名称
   **/
  private String roleName;
  /**
   * 权限字符串
   **/
  private String roleKey;
  /**
   * 显示顺序
   **/
  private int orderNum;
  /**
   * 角色状态（0正常 1停用）
   **/
  private RoleEnum.Status status;
  /**
   * 删除标志（0逻辑未删除 1逻辑已删除）
   **/
  @TableLogic
  private RoleEnum.DelFlag delFlag;
  /**
   * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
   **/
  private RoleEnum.DataScope dataScope;
}
