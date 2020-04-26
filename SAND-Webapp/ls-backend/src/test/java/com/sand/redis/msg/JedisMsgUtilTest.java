/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.msg;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.sand.JunitBootStrap;
import com.sand.common.util.global.Config;
import com.sand.redis.config.init.RedisSentinelRunner;
import com.sand.redis.msg.sub.manager.LockBean;
import com.sand.redis.msg.sub.manager.LockManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * 功能说明：JedisMsgUtil测试类
 * 开发人员：@author liusha
 * 开发日期：2020/4/26 21:08
 * 功能描述：JedisMsgUtil测试类
 */
@Slf4j
public class JedisMsgUtilTest extends JunitBootStrap {

  @Test
  public void publishMsg() {
    String uuid = IdWorker.getIdStr();
    // 使用线程模拟发布消息与订阅消息在两台不同的服务器之间的处理
    new Thread(() -> {
      log.info("Redis发布消息...");
      JedisSentinelPool jedisPool = (JedisSentinelPool) Config.getConfig(RedisSentinelRunner.SENTINEL_APPLIED);
      JedisMsgUtil msgUtil = new JedisMsgUtil(jedisPool);

      String data = "哨兵已就位";
      String message = uuid + "|" + data;

      String channels = Config.getProperty("redis.subscribe.channels");
      String[] channelArray = channels.split("[,\\s]+");
      Arrays.stream(channelArray).forEach(channel -> msgUtil.publishMsg(channel, message));
    }).start();

    log.info("订阅消息处理...");
    // 初始化LockBean
    Condition condition = LockManager.lock.newCondition();
    LockBean lockBean = LockBean.builder()
        .condition(condition)
        .hasSub(false)
        .build();
    LockManager.lockBeanMap.put(uuid, lockBean);
    LockManager.lock.lock();
    try {
      condition.await(10, TimeUnit.SECONDS);
      LockManager.lockBeanMap.remove(uuid, lockBean);
      boolean hasSub = lockBean.isHasSub();
      log.info("是否订阅：{}", hasSub);
      if (hasSub) {
        String result = lockBean.getResult();
        log.info("订阅结果：{}", result);
      }
    } catch (InterruptedException e) {
      log.error("订阅消息回调处理异常，", e);
    } finally {
      LockManager.lock.unlock();
    }
  }
}
