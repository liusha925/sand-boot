/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/15    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.subscriber.listener;

import com.sand.redis.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

/**
 * 功能说明：Redis订阅者监听器
 * 开发人员：@author liusha
 * 开发日期：2020/4/15 16:57
 * 功能描述：发布者订阅者模式，发布者发布消息放到队列里，多个订阅者同时监听队列，监听队列的订阅者都会收到同一份消息；即正常情况下每个订阅者收到的消息都是一样的。
 */
@Slf4j
public class RedisSubscriberListener extends JedisPubSub {
  /**
   * 监听到订阅频道接受到消息时的回调
   *
   * @param channel channel
   * @param message message
   */
  @Override
  public synchronized void onMessage(String channel, String message) {
    log.info("Redis订阅者监听到订阅频道接受到消息时的回调: channel[{}], message[{}]", new Object[]{channel, message});
    try {
      RedisLockUtil.lock(message, "[|\\s]+");
    } catch (Exception e) {
      log.error("订阅消息失败", e);
    }
  }

  /**
   * 监听到订阅模式接受到消息时的回调
   *
   * @param pattern pattern
   * @param channel channel
   * @param message message
   */
  @Override
  public void onPMessage(String pattern, String channel, String message) {
    log.info("Redis订阅者监听到订阅模式接受到消息时的回调: pattern[{}], channel[{}], message[{}]", new Object[]{pattern, channel, message});
  }

  /**
   * 订阅频道时的回调
   *
   * @param channel            订阅频道
   * @param subscribedChannels 频道数量
   */
  @Override
  public void onSubscribe(String channel, int subscribedChannels) {
    log.info("Redis订阅者监听到订阅频道时的回调: 订阅频道[{}], 频道数量[{}]", new Object[]{channel, subscribedChannels});
  }

  /**
   * 取消订阅频道时的回调
   *
   * @param channel            channel
   * @param subscribedChannels subscribedChannels
   */
  @Override
  public void onUnsubscribe(String channel, int subscribedChannels) {
    log.info("Redis订阅者监听到取消订阅频道时的回调:{} is been subscribed:{}", new Object[]{channel, subscribedChannels});
  }

  /**
   * 取消订阅模式时的回调
   *
   * @param pattern            pattern
   * @param subscribedChannels subscribedChannels
   */
  @Override
  public void onPUnsubscribe(String pattern, int subscribedChannels) {
    log.info("Redis订阅者监听到取消订阅模式时的回调: pattern[{}], subscribedChannels[{}]", new Object[]{pattern, subscribedChannels});
  }

  /**
   * 订阅频道模式时的回调
   *
   * @param pattern            pattern
   * @param subscribedChannels subscribedChannels
   */
  @Override
  public void onPSubscribe(String pattern, int subscribedChannels) {
    log.info("Redis订阅者监听到订阅频道模式时的回调: pattern[{}], subscribedChannels[{}]", new Object[]{pattern, subscribedChannels});
  }

  @Override
  public void unsubscribe() {
    super.unsubscribe();
  }

  @Override
  public void unsubscribe(String... channels) {
    super.unsubscribe(channels);
  }

  @Override
  public void subscribe(String... channels) {
    super.subscribe(channels);
  }

  @Override
  public void psubscribe(String... patterns) {
    super.psubscribe(patterns);
  }

  @Override
  public void punsubscribe() {
    super.punsubscribe();
  }

  @Override
  public void punsubscribe(String... patterns) {
    super.punsubscribe(patterns);
  }

}
