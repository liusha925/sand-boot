/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/15    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.msg.sub.listener;

import com.sand.common.util.lang3.StringUtil;
import com.sand.redis.msg.sub.manager.LockBean;
import com.sand.redis.msg.sub.manager.LockManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;
import java.util.concurrent.locks.Condition;

/**
 * 功能说明：消息订阅中心
 * 开发人员：@author liusha
 * 开发日期：2020/4/15 16:57
 * 功能描述：Redis订阅消息监听器
 */
@Slf4j
public class RedisMsgSubListener extends JedisPubSub {
  /**
   * 监听到订阅频道接受到消息时的回调
   *
   * @param channel channel
   * @param message message
   */
  @Override
  public synchronized void onMessage(String channel, String message) {
    log.info("监听到订阅频道接受到消息时的回调: channel[{}], message[{}]", new Object[]{channel, message});
    try {
      if (StringUtil.isNotBlank(message)) {
        // 规则需要跟发布消息的服务端约定好
        String[] messages = message.split("[|\\s]+");
        if (messages.length == 2) {
          String uuid = messages[0];
          String data = messages[1];
          if (LockManager.lockBeanMap.containsKey(uuid)) {
            LockBean lockBean = LockManager.lockBeanMap.get(uuid);
            Condition condition = lockBean.getCondition();
            if (Objects.nonNull(condition)) {
              lockBean.setHasSub(true);
              lockBean.setResult(data);
              LockManager.lock.lock();
              condition.signal();
              LockManager.lock.unlock();
            }
          } else {
            log.info("找不到此uuid：{}", uuid);
          }
        } else {
          log.info("订阅消息格式不符合规定：uuid|data");
        }

      }

    } catch (Exception e) {
      log.error("处理订阅消息失败，", e);
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
    log.info("监听到订阅模式接受到消息时的回调: pattern[{}], channel[{}], message[{}]", new Object[]{pattern, channel, message});
  }

  /**
   * 订阅频道时的回调
   *
   * @param channel            订阅频道
   * @param subscribedChannels 频道数量
   */
  @Override
  public void onSubscribe(String channel, int subscribedChannels) {
    log.info("订阅频道时的回调: 订阅频道[{}], 频道数量[{}]", new Object[]{channel, subscribedChannels});
  }

  /**
   * 取消订阅频道时的回调
   *
   * @param channel            channel
   * @param subscribedChannels subscribedChannels
   */
  @Override
  public void onUnsubscribe(String channel, int subscribedChannels) {
    log.info("取消订阅频道时的回调:{} is been subscribed:{}", new Object[]{channel, subscribedChannels});
  }

  /**
   * 取消订阅模式时的回调
   *
   * @param pattern            pattern
   * @param subscribedChannels subscribedChannels
   */
  @Override
  public void onPUnsubscribe(String pattern, int subscribedChannels) {
    log.info("取消订阅模式时的回调: pattern[{}], subscribedChannels[{}]", new Object[]{pattern, subscribedChannels});
  }

  /**
   * 订阅频道模式时的回调
   *
   * @param pattern            pattern
   * @param subscribedChannels subscribedChannels
   */
  @Override
  public void onPSubscribe(String pattern, int subscribedChannels) {
    log.info("订阅频道模式时的回调: pattern[{}], subscribedChannels[{}]", new Object[]{pattern, subscribedChannels});
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
