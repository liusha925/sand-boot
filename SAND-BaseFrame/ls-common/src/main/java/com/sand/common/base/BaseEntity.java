/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能说明：实体对象基类
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 13:38
 * 功能描述：实体对象的功能字段
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BaseEntity implements Serializable {
  private static final long serialVersionUID = 2367225182033538004L;
  /**
   * 创建者
   */
  private String createBy;
  /**
   * 更新者
   */
  private String updateBy;
  /**
   * 创建时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;
  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;
  /**
   * 预留字段1
   */
  @Length(max = 32, message = "[预留字段1]不能超过32个字符呢")
  private String field1;
  /**
   * 预留字段2
   */
  @Length(max = 255, message = "[预留字段2]不能超过255个字符呢")
  private String field2;
  /**
   * 预留字段3
   */
  @Length(max = 1024, message = "[预留字段3]不能超过1024个字符呢")
  private String field3;
  /**
   * 备注信息
   */
  @Length(max = 512, message = "[备注信息]不能超过512个字符呢")
  private String remark;
}
