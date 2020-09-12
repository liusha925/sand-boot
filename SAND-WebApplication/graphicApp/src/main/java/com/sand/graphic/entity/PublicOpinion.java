/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/24    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.graphic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 功能说明：
 * 开发人员：@author liusha
 * 开发日期：2020/8/24 15:22
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "public_opinion_tbl")
public class PublicOpinion {
  /**
   * 客户号
   */
  private String merId;
  /**
   * 管理员名称
   */
  private String merName;
  /**
   * 产品名称
   */
  private String productName;
  /**
   * 日期
   */
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date pubOpinDate;
  /**
   * 舆情描述
   */
  private String massage;
}
