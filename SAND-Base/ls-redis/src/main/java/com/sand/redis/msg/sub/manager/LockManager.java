/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/15    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.msg.sub.manager;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能说明：锁认证
 * 开发人员：@author liusha
 * 开发日期：2020/4/15 17:14
 * 功能描述：加锁处理
 */
@Slf4j
public final class LockManager {
  /**
   * 消息服务锁
   */
  public static Lock lock = new ReentrantLock();
  /**
   * 消息组合锁
   */
  public static Map<String, LockBean> lockBeanMap = new ConcurrentHashMap<>();

  /**
   * 示例
   *
   * @param args
   */
  public static void main(String[] args) {
    log.info("等待订阅消息...");
    // 初始化LockStatus
    Condition condition = LockManager.lock.newCondition();
    LockBean lockBean = LockBean.builder()
        .condition(condition)
        .hasSub(false)
        .build();
    LockManager.lockBeanMap.put("uuid", lockBean);
    LockManager.lock.lock();
    try {
      condition.await(10, TimeUnit.SECONDS);
      LockManager.lockBeanMap.remove("uuid", lockBean);
      boolean hasSub = lockBean.isHasSub();
      log.info("是否订阅：{}", hasSub);
      if (hasSub) {
        String result = lockBean.getResult();
        log.info("订阅结果：{}", result);
      }
    } catch (InterruptedException e) {
      log.error("订阅消息异常，", e);
    } finally {
      LockManager.lock.unlock();
    }
  }
}
