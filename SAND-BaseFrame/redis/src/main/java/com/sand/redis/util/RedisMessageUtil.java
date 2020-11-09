/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/23    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.util;

import com.sand.core.util.CloseableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 功能说明：发布/订阅消息，生产/消费消息
 * 开发人员：@author liusha
 * 开发日期：2020/4/23 18:07
 * 功能描述：发布/订阅消息，生产/消费消息
 */
@Slf4j
public class RedisMessageUtil {
  private Jedis jedis;
  private RedisTemplate<String, String> redisTemplate;

  /**
   * 发布者订阅者模式 构造器
   *
   * @param jedisPool jedisPool
   */
  public RedisMessageUtil(JedisSentinelPool jedisPool) {
    this.jedis = jedisPool.getResource();
  }

  /**
   * 生产者消费者模式 构造器
   *
   * @param redisTemplate redisTemplate
   */
  public RedisMessageUtil(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 发布消息
   *
   * @param channel 发布频道
   * @param message 消息内容
   */
  public void publish(String channel, String message) {
    try {
      jedis.publish(channel, message);
      log.info("Redis发布者给频道[{}]发布了一条消息：{}", channel, message);
    } catch (Exception e) {
      log.info("Redis发布者发布消息失败", e);
    } finally {
      CloseableUtil.close(jedis);
    }
  }

  /**
   * 订阅消息
   *
   * @param listener 订阅者监听器
   * @param channels 订阅频道（可以订阅多个，以,分割开）
   */
  public void subscribe(JedisPubSub listener, String channels) {
    try {
      jedis.subscribe(listener, channels);
      log.info("Redis订阅者订阅了频道[{}]下的消息", channels);
    } catch (Exception e) {
      log.info("Redis订阅者订阅消息失败", e);
    } finally {
      CloseableUtil.close(jedis);
    }
  }

  /**
   * 生产消息
   *
   * @param channel 发布频道
   * @param message 消息内容
   */
  public void produce(String channel, String message) {
    try {
      redisTemplate.opsForList().leftPush(channel, message);
      log.info("Redis生产者给频道[{}]生产了一条消息：{}", channel, message);
    } catch (Exception e) {
      log.info("Redis生产者生产消息失败", e);
    }
  }

  /**
   * 消费消息
   *
   * @param listener 消费者监听器
   * @param message  消费主体
   */
  public void consume(MessageListener listener, Message message) {
    try {
      log.info("Redis消费者开始消费消息");
      listener.onMessage(message, null);
    } catch (Exception e) {
      log.info("Redis消费者消费消息失败", e);
    }
  }

}
