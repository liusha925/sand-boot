/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/7    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.config.properties;

import lombok.Data;

/**
 * 功能说明：Redis数据库配置
 * 开发人员：@author liusha
 * 开发日期：2020/4/7 16:23
 * 功能描述：配置对应的数据库
 */
@Data
public class RedisDatabase {
  /**
   * 数据库索引
   */
  private int dbIndex;
  /**
   * 数据库名称
   */
  private String dbName;
}
