/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/24    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.graphic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能说明：
 * 开发人员：@author liusha
 * 开发日期：2020/8/24 16:23
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "arrears_info_tbl")
public class ArrearsInfo {
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
   * 欠费次数
   */
  private int arrearsNum;
}
