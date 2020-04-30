/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.consumer.init;

import cn.hutool.core.thread.ThreadUtil;
import com.sand.common.util.convert.SandConvert;
import com.sand.common.util.global.Config;
import com.sand.redis.config.RedisConfig;
import com.sand.redis.consumer.thread.RedisMessageConsumeThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 功能说明：Redis消费者启动类
 * 开发人员：@author liusha
 * 开发日期：2020/4/26 16:19
 * 功能描述：Redis消费者启动类
 */
@Slf4j
@Component
@Order(value = 10)
public class RedisConsumeRunner implements ApplicationRunner {
  /**
   * Redis消息消费标识
   */
  public static final String CONSUME_APPLIED = "__redis_consume_applied";

  @Override
  public void run(ApplicationArguments args) throws Exception {
    /**
     * 消费者系统默认是关闭状态，若要开启，需在启动类上更改代码：
     * <pre>
     *    // 加载自定义配置参数
     *    String[] configs = new String[]{
     *        // 开启Redis消费者系统
     *        RedisConsumeRunner.CONSUME_APPLIED
     *    };
     *    String[] newArgs = org.springframework.util.StringUtils.concatenateStringArrays(args, configs);
     *    SpringApplication.run(LsBackendApplication.class, newArgs);
     * </pre>
     */
    List<String> argsList = Arrays.asList(args.getSourceArgs());
    if (argsList.contains(CONSUME_APPLIED)) {
      log.info("Redis消费者系统 启动开始...");
      try {
        String channels = Config.getProperty("redis.consume.channels");
        int dbIndex = SandConvert.obj2Int(Config.getProperty("redis.database.message", "0"));
        RedisTemplate<String, String> redisTemplate = RedisConfig.getRedisTemplate(dbIndex);
        // 消息消费
        RedisMessageConsumeThread consumeThread = new RedisMessageConsumeThread(redisTemplate, channels);
        ThreadUtil.execAsync(consumeThread);
        // TODO 也可以加入其它消费逻辑，参考消息消费逻辑
        log.info("...Redis消费者系统 启动完成");
      } catch (Exception e) {
        log.info("Redis消费者系统 启动异常", e);
      }
    } else {
      log.info("Redis消费者系统 未开启");
    }

  }
}
