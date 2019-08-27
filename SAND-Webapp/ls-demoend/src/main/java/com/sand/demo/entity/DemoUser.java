/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/23   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sand.base.core.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2019/8/23 13:12
 * 功能描述：用户信息
 */
@Data
@ToString(callSuper = true)
@TableName(value = "demo_" + "user")
public class DemoUser extends BaseEntity {
  /**
   * 用户id
   */
  private Long id;
  /**
   * 用户名
   */
  private String name;
  /**
   * 年龄
   */
  private Integer age;
  /**
   * 邮箱
   */
  private String email;
}
