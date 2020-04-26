/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/15    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.msg.sub.thread;

import com.sand.common.util.CloseableUtil;
import com.sand.redis.msg.sub.listener.RedisMsgSubListener;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 功能说明：Redis消息订阅线程
 * 开发人员：@author liusha
 * 开发日期：2020/4/15 18:45
 * 功能描述：消息订阅
 */
@Slf4j
public class RedisMsgSubThread extends Thread {
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
  private final RedisMsgSubListener redisMsgSubListener = new RedisMsgSubListener();

  public RedisMsgSubThread(JedisSentinelPool jedisPool) {
    super("RedisMsgSubThread");
    this.jedisPool = jedisPool;
  }

  public RedisMsgSubThread(JedisSentinelPool jedisPool, String channels) {
    super("RedisMsgSubThread");
    this.jedisPool = jedisPool;
    this.channels = channels;
  }

  @Override
  public void run() {
    log.info("开始订阅频道[{}]下的消息", channels);
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      jedis.subscribe(redisMsgSubListener, channels);
    } catch (Exception e) {
      log.error("消息订阅失败，", e);
    } finally {
      CloseableUtil.close(jedis);
    }

  }
}
