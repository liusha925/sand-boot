/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.message;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.sand.JunitBootStrap;
import com.sand.core.util.convert.SandConvert;
import com.sand.core.util.global.Config;
import com.sand.redis.config.RedisConfig;
import com.sand.redis.manager.RedisLockBean;
import com.sand.redis.manager.RedisLockManager;
import com.sand.redis.subscriber.runner.RedisSentinelRunner;
import com.sand.redis.util.RedisMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * 功能说明：RedisMessageUtil测试类
 * 开发人员：@author liusha
 * 开发日期：2020/4/26 21:08
 * 功能描述：RedisMessageUtil测试类
 */
@Slf4j
public class RedisMessageUtilTest extends JunitBootStrap {
  @Test
  public void publish() {
    String uuid = IdWorker.getIdStr();
    // 使用线程模拟发布者与订阅者在两台不同的服务器之间的处理
    // 1、发布者服务器
    new Thread(() -> {
      log.info("Redis发布者...");
      JedisSentinelPool jedisPool = (JedisSentinelPool) Config.getConfig(RedisSentinelRunner.APPLIED);
      RedisMessageUtil messageUtil = new RedisMessageUtil(jedisPool);

      String data = "发布者订阅者模式";
      String message = uuid + "|" + data;
      String channels = Config.getProperty("redis.subscribe.channels");
      String[] channelArray = channels.split("[,\\s]+");
      Arrays.stream(channelArray).forEach(channel -> messageUtil.publish(channel, message));
      // 消息存储在redis数据库中
//      RedisConfig redisConfig = SpringUtil.getBean(RedisConfig.class);
//      int dbIndex = SandConvert.obj2Int(Config.getProperty("redis.database.message", "0"));
//      RedisRepository redisRepository = redisConfig.getRedisRepository(dbIndex);
//      redisRepository.expireHashValue(uuid, channels, new Gson().toJson(message), 60);
    }).start();
    // 2、订阅者服务器
    log.info("Redis订阅者处理...");
    // 初始化LockBean
    Condition condition = RedisLockManager.lock.newCondition();
    RedisLockBean redisLockBean = RedisLockBean.builder()
        .condition(condition)
        .hasSub(false)
        .build();
    RedisLockManager.lockBeanMap.put(uuid, redisLockBean);
    RedisLockManager.lock.lock();
    try {
      condition.await(10, TimeUnit.SECONDS);
      RedisLockManager.lockBeanMap.remove(uuid, redisLockBean);
      boolean hasSub = redisLockBean.isHasSub();
      log.info("是否订阅：{}", hasSub);
      if (hasSub) {
        String result = redisLockBean.getResult();
        log.info("订阅结果：{}", result);
      }
    } catch (InterruptedException e) {
      log.error("订阅回调处理异常，", e);
    } finally {
      RedisLockManager.lock.unlock();
    }
  }

  @Test
  public void produce() throws InterruptedException {
    final String uuid = IdWorker.getIdStr();
    // 生产者服务器
    new Thread(() -> {
      log.info("Redis生产者...");
      int dbIndex = SandConvert.obj2Int(Config.getProperty("redis.database.message", "0"));
      RedisTemplate<String, String> redisTemplate = RedisConfig.getRedisTemplate(dbIndex);
      RedisMessageUtil messageUtil = new RedisMessageUtil(redisTemplate);

      String data = "生产者消费者模式";
      String message = uuid + "|" + data;
      String channels = Config.getProperty("redis.consume.channels");
      String[] channelArray = channels.split("[,\\s]+");
      Arrays.stream(channelArray).forEach(channel -> messageUtil.produce(channel, message));
      // 消息存储在redis数据库中
//      RedisConfig redisConfig = SpringUtil.getBean(RedisConfig.class);
//      RedisRepository redisRepository = redisConfig.getRedisRepository(dbIndex);
//      redisRepository.expireHashValue(uuid, channels, new Gson().toJson(message), 60);
    }).start();
    Thread.sleep(3000);
  }

  @Test
  public void huTool() {
    Thread thread = new Thread(
        () -> System.out.println("跑起来了。。。")
    );
    ThreadUtil.execAsync(thread);
    System.out.println("主线程。。。");
  }

}
