/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sand.common.base.BaseEntity;
import com.sand.common.constant.Constant;
import com.sand.common.annotation.EnumValidAnnotation;
import com.sand.common.annotation.ExcelAnnotation;
import com.sand.system.enums.RoleEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 功能说明：系统角色
 * 开发人员：@author liusha
 * 开发日期：2019/8/30 13:10
 * 功能描述：系统角色
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(Constant.TABLE_SYS + "role")
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
  @NotBlank(message = "[角色名称]不能为空哟！")
  @Length(max = 64, message = "[角色名称]不能超过64个字符呢！")
  @ExcelAnnotation(name = "角色名称")
  private String roleName;
  /**·
   * 权限字符
   **/
  @NotBlank(message = "[权限字符]不能为空哟！")
  @Length(max = 64, message = "[权限字符]不能超过64个字符呢！")
  @ExcelAnnotation(name = "权限字符")
  private String roleKey;
  /**
   * 显示顺序
   **/
  @ExcelAnnotation(name = "显示顺序")
  private int orderNum;
  /**
   * 角色状态（0正常 1停用）
   **/
  @EnumValidAnnotation(message = "[角色状态]不存在！", target = RoleEnum.Status.class)
  @ExcelAnnotation(name = "角色状态", combo = {"正常", "停用"}, readConvertExp = "0=正常,1=停用")
  private String status;
  /**
   * 删除标志（0逻辑未删除 1逻辑已删除）
   **/
  @TableLogic
  @EnumValidAnnotation(message = "[删除标志]不存在！", target = RoleEnum.DelFlag.class)
  @ExcelAnnotation(name = "删除标志", combo = {"未删除", "已删除"}, readConvertExp = "0=未删除,1=已删除")
  private String delFlag;
  /**
   * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
   **/
  @EnumValidAnnotation(message = "[数据范围]不存在！", target = RoleEnum.DataScope.class)
  @ExcelAnnotation(name = "数据范围", combo = {"全部数据权限", "自定数据权限", "本部门数据权限", "本部门及以下数据权限"}, readConvertExp = "1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限")
  private String dataScope;
}
