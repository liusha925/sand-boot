/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/15    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.redis.msg;

import com.sand.common.util.spring.SpringUtil;
import com.sand.redis.config.RedisConfig;
import com.sand.redis.msg.sub.thread.RedisMsgSubThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 功能说明：Redis消息订阅
 * 开发人员：@author liusha
 * 开发日期：2020/4/15 19:02
 * 功能描述：启动Redis消息订阅功能
 */
@Slf4j
@Component
public class RedisMsgSubStrap {
  static {
    try {
      log.info("~~~~~~~~~Redis消息订阅 启动开始~~~~~~~~");
      /*String channel = "msgNotice";
      String masterName = "mymaster";
      RedisConfig redisConfig = SpringUtil.getBean(RedisConfig.class);
      String hosts = redisConfig.getHostName() + ":" + redisConfig.getPort();
      Set<String> sentinels = new HashSet<>();
      Collections.addAll(sentinels, hosts.split("[,\\s]+"));
      JedisPoolConfig jedisPoolConfig = SpringUtil.getBean(JedisPoolConfig.class);
      JedisSentinelPool sentinelPool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig);
      RedisMsgSubThread msgSubThread = new RedisMsgSubThread(sentinelPool, channel);
      msgSubThread.start();*/
      log.info("~~~~~~~~~Redis消息订阅 启动结束~~~~~~~~");
    } catch (Exception e) {
      log.error("Redis消息订阅失败", e);
    }
  }

}
