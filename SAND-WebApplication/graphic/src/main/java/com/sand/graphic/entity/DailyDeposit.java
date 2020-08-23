/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/8/23/023   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.graphic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能说明：大致描述 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/8/23/023 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "daily_deposit_tbl")
public class DailyDeposit {
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
   * 日均存款
   */
  private double dailyDeposit;
}
