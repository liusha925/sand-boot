/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/15    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.subscriber.thread;

import com.sand.redis.util.RedisMessageUtil;
import com.sand.redis.subscriber.listener.RedisSubscriberListener;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 功能说明：Redis消息订阅线程
 * 开发人员：@author liusha
 * 开发日期：2020/4/15 18:45
 * 功能描述：消息订阅
 */
@Slf4j
public class RedisMessageSubscribeThread extends Thread {
  /**
   * 订阅频道
   */
  private String channels;
  /**
   * Redis连接池
   */
  private JedisSentinelPool jedisPool;
  /**
   * 消息订阅监听器
   */
  private final RedisSubscriberListener redisSubscriberListener = new RedisSubscriberListener();

  public RedisMessageSubscribeThread(JedisSentinelPool jedisPool, String channels) {
    super("redisMessageSubscribeThread");
    this.jedisPool = jedisPool;
    this.channels = channels;
  }

  @Override
  public void run() {
    log.info("开始订阅频道[{}]下的消息", channels);
    try {
      RedisMessageUtil messageUtil = new RedisMessageUtil(jedisPool);
      messageUtil.subscribe(redisSubscriberListener, channels);
    } catch (Exception e) {
      log.error("订阅消息失败", e);
    }
  }

}
