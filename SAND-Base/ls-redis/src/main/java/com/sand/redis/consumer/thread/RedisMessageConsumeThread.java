/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/15    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.consumer.thread;

import com.sand.common.util.convert.SandConvert;
import com.sand.common.util.global.Config;
import com.sand.redis.consumer.listener.RedisConsumerListener;
import com.sand.redis.util.RedisMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 功能说明：Redis消息消费线程
 * 开发人员：@author liusha
 * 开发日期：2020/4/15 18:45
 * 功能描述：消息消费线程
 */
@Slf4j
public class RedisMessageConsumeThread extends Thread {
  /**
   * 消费频道
   */
  private String channels;
  /**
   * Redis redisTemplate
   */
  private RedisTemplate<String, String> redisTemplate;
  /**
   * 消息消费监听器
   */
  private final RedisConsumerListener redisConsumerListener = new RedisConsumerListener();

  public RedisMessageConsumeThread(RedisTemplate<String, String> redisTemplate, String channels) {
    super("redisMessageConsumeThread");
    this.redisTemplate = redisTemplate;
    this.channels = channels;
  }

  @Override
  public void run() {
    log.info("开始消费频道[{}]下的消息", channels);
    try {
      long tickTime = SandConvert.obj2Long(Config.getProperty("redis.consume.listener.tick-time"), 30L);
      String[] channelArray = channels.split("[,\\s]+");
      Arrays.stream(channelArray).forEach(channel -> {
        Object body = redisTemplate.opsForList().rightPop(channel, tickTime, TimeUnit.SECONDS);
        if (Objects.nonNull(body)) {
          RedisSerializer<String> serializer = (RedisSerializer<String>) redisTemplate.getValueSerializer();
          byte[] channelByte = serializer.serialize(channel);
          byte[] message = serializer.serialize(SandConvert.obj2Str(body));
          RedisMessageUtil messageUtil = new RedisMessageUtil(redisTemplate);
          messageUtil.consume(redisConsumerListener, new DefaultMessage(channelByte, message));
        } else {
          log.info("找不到消费主体...");
        }
      });
    } catch (Exception e) {
      log.error("消费消息失败", e);
    }

  }
}
