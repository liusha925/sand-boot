/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/2    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能说明：分页信息
 * 开发人员：@author liusha
 * 开发日期：2019/9/2 20:08
 * 功能描述：分页信息，主要是提供给model类
 */
@Data
@Accessors(chain = true)
public class PageEntity implements Serializable {
  private static final long serialVersionUID = 3824642422523603382L;
  /**
   * 当前页码
   */
  @TableField(exist = false)
  private long current = 1L;
  /**
   * 每页记录数
   */
  @TableField(exist = false)
  private long size = 10L;
}
