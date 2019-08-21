/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能说明：返回结果集
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 13:31
 * 功能描述：返回结果集
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
  private int code;

  private String msg;

  private String type;

  private T data;
}
