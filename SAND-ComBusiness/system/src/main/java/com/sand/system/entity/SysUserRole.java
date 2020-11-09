/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/2    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sand.core.constant.Constant;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 功能说明：用户-角色关联表
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 16:50
 * 功能描述：用户-角色关联表   用户1-N角色
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@TableName(Constant.TABLE_SYS + "user_role")
public class SysUserRole {
  /**
   * 用户D
   */
  private String userId;
  /**
   * 角色ID
   */
  private String roleId;
}
