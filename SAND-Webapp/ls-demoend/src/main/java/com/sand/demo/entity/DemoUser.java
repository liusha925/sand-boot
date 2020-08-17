/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/15   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2020/8/15 13:12
 * 功能描述：用户信息
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "demo_" + "user")
public class DemoUser {
  /**
   * 用户id
   */
  @TableId
  private String id;
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
