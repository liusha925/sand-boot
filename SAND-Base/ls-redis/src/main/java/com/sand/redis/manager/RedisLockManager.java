/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/15    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.manager;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能说明：锁认证
 * 开发人员：@author liusha
 * 开发日期：2020/4/15 17:14
 * 功能描述：加锁处理
 */
@Slf4j
public final class RedisLockManager {
  /**
   * 消息服务锁
   */
  public static Lock lock = new ReentrantLock();
  /**
   * 消息组合锁
   */
  public static Map<String, RedisLockBean> lockBeanMap = new ConcurrentHashMap<>();

}
