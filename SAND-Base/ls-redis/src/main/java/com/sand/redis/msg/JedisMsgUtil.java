/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/23    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.msg;

import com.sand.common.util.CloseableUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 功能说明：消息发布、订阅工具
 * 开发人员：@author liusha
 * 开发日期：2020/4/23 18:07
 * 功能描述：消息发布、订阅工具
 */
@Slf4j
public class JedisMsgUtil {
  private Jedis jedis;

  public JedisMsgUtil(JedisSentinelPool jedisPool) {
    this.jedis = jedisPool.getResource();
  }

  /**
   * 消息发布
   *
   * @param channel 发布频道
   * @param message 发布消息
   */
  public void publishMsg(String channel, String message) {
    try {
      jedis.publish(channel, message);
    } catch (Exception e) {
      log.info("消息发布失败", e);
    } finally {
      CloseableUtil.close(jedis);
    }
  }

  /**
   * 消息订阅
   *
   * @param listener 消息监听
   * @param channels 订阅频道（可以订阅多个，以,分割开）
   */
  public void subscribeMsg(JedisPubSub listener, String channels) {
    try {
      jedis.subscribe(listener, channels);
    } catch (Exception e) {
      log.info("消息订阅失败", e);
    } finally {
      CloseableUtil.close(jedis);
    }
  }

}
