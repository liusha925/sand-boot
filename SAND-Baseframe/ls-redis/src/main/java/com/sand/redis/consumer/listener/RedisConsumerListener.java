/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/29    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.consumer.listener;

import com.sand.common.util.convert.SandConvert;
import com.sand.common.util.global.Config;
import com.sand.redis.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 功能说明：Redis消费者监听器
 * 开发人员：@author liusha
 * 开发日期：2020/4/29 19:52
 * 功能描述：生产者消费者模式，生产者生产消息放到队列里，多个消费者同时监听队列，谁先抢到数据谁就会从队列中取走数据；即对于每份数据只能被最多一个消费者拥有。
 */
@Slf4j
public class RedisConsumerListener implements MessageListener {
  @Override
  public synchronized void onMessage(Message message, byte[] bytes) {
    try {
      int dbIndex = SandConvert.obj2Int(Config.getProperty("redis.database.message", "0"));
      RedisTemplate<String, String> redisTemplate = RedisConfig.getRedisTemplate(dbIndex);
      RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
      String channel = SandConvert.obj2Str(serializer.deserialize(message.getChannel()));
      String messageBody = SandConvert.obj2Str(serializer.deserialize(message.getBody()));
      log.info("Redis消费者监听到频道[{}]下的内容：{}", channel, messageBody);
      redisTemplate.opsForList().remove(channel, 0, messageBody);
      log.info("Redis消费者消费了[{}]的一条内容：{}", channel, messageBody);
    } catch (Exception e) {
      log.error("消费消息失败", e);
    }
  }

}
