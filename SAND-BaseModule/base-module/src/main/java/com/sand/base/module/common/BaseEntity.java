/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.module.common;

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
   * 备注信息
   */
  @Length(max = 512, message = "备注信息不能超过512个字符呢")
  private String remark;
}
