/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能说明：返回结果集
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 13:31
 * 功能描述：返回结果集
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity<T> {
  /**
   * 返回码
   */
  private int code;
  /**
   * 返回描述
   */
  private String msg;
  /**
   * 返回数据
   */
  private T data;
}
