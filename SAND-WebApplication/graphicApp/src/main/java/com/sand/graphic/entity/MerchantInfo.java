/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/8/23/023   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.graphic.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能说明：大致描述 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/8/23/023 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "merchant_info_tbl")
public class MerchantInfo {
  /**
   * 客户号
   */
  @TableId
  private String merId;
  /**
   * 管理员名称
   */
  private String merName;
  /**
   * 业务规模
   */
  private double busiScale;
  /**
   * 业务收入
   */
  private double busiIncome;
  /**
   * 产品数量
   */
  private int productNum;
  /**
   * 是否开通服务平台PC
   */
  private String platfPCFlag;
  /**
   * 是否开通杭银托管APP
   */
  private String platfAPPFlag;
  /**
   * 是否开通杭银托管公众号
   */
  private String platfGZFlag;
  /**
   * 是否开通绩效评估
   */
  private String perEvaluate;
  /**
   * 是否开通估值外包
   */
  private String valuaOutsourc;
  /**
   * 是否签订银行间备忘录
   */
  private String bankMemo;
  /**
   * 是否电子指令直连
   */
  private String electrOrder;
  /**
   * 是否文件传输直连
   */
  private String fileTransfer;
  /**
   * 是否电子对账
   */
  private String electrCheck;
  /**
   * 活动参与度是否活跃
   */
  private String vitality;
}
