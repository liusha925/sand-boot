/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/2    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sand.common.constant.Constant;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 功能说明：角色-菜单关联表
 * 开发人员：@author liusha
 * 开发日期：2019/9/2 9:50
 * 功能描述：角色-菜单关联表   角色1-N菜单
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@TableName(Constant.TABLE_SYS + "role_menu")
public class SysRoleMenu {
  /**
   * 角色ID
   */
  private String roleId;
  /**
   * 菜单ID
   */
  private String menuId;
}
