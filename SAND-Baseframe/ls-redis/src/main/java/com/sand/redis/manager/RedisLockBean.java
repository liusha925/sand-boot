/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/15    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.manager;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.locks.Condition;

/**
 * 功能说明：订阅服务
 * 开发人员：@author liusha
 * 开发日期：2020/4/15 19:55
 * 功能描述：订阅服务
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class RedisLockBean {
  /**
   * 订阅结果
   */
  private String result;
  /**
   * 是否订阅
   */
  private boolean hasSub;
  /**
   * 阻塞操作
   */
  private Condition condition;
}
