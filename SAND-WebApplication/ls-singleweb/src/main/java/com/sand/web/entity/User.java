/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/8/16/016   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.web.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能说明：大致描述 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/8/16/016 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Data
@NoArgsConstructor
public class User {
  private int age;
  private String name;
}
