/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/2    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.model;

import com.sand.sys.entity.SysRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 功能说明：系统角色Model
 * 开发人员：@author liusha
 * 开发日期：2019/9/2 17:47
 * 功能描述：用作DTO、VO
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysRoleModel extends SysRole {
  private static final long serialVersionUID = 6373508068162527557L;
  /**
   * 菜单集合
   */
  private List<String> menuIds;
  /**
   * 当前页码
   */
  private long current = 1L;
  /**
   * 每页记录数
   */
  private long size = 10L;
}
