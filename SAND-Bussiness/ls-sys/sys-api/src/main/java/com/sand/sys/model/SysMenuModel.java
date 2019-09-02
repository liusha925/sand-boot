/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/2    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.model;

import com.sand.sys.entity.SysMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 功能说明：系统菜单Model
 * 开发人员：@author liusha
 * 开发日期：2019/9/2 16:20
 * 功能描述：用作DTO、VO
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysMenuModel extends SysMenu {
  private static final long serialVersionUID = 92364888777517858L;
  /**
   * 父菜单名称
   */
  private String parentName;

  /**
   * 子菜单
   */
  private List<SysMenu> children;

}
