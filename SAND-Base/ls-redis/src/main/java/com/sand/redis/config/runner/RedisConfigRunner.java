/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.config.runner;

import com.sand.common.util.convert.SandConvert;
import com.sand.common.util.global.Config;
import com.sand.common.util.global.PropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * 功能说明：redis配置信息加载
 * 开发人员：@author liusha
 * 开发日期：2020/4/26 13:21
 * 功能描述：redis配置信息加载
 */
@Slf4j
@Component
@Order(value = 1)
public class RedisConfigRunner implements ApplicationRunner {
  /**
   * Redis配置标识
   */
  public static final String APPLIED = "__redis_config_applied";
  /**
   * Redis配置文件名称
   */
  private static final String CONFIG_NAME = "redis.properties";

  @Override
  public void run(ApplicationArguments args) throws Exception {
    /**
     * 哨兵系统默认是关闭状态，若要开启，需在启动类上更改代码：
     * <pre>
     *    // 加载自定义配置参数
     *    String[] configs = new String[]{
     *        // 开启Redis配置加载
     *        RedisConfigRunner.APPLIED
     *    };
     *    String[] newArgs = org.springframework.util.StringUtils.concatenateStringArrays(args, configs);
     *    SpringApplication.run(LsBackendApplication.class, newArgs);
     * </pre>
     */
    List<String> argsList = Arrays.asList(args.getSourceArgs());
    if (argsList.contains(APPLIED)) {
      log.info("Redis配置信息 加载开始...");
      Properties redisProps = new PropUtil(CONFIG_NAME).getProperties();
      Enumeration<?> en = redisProps.propertyNames();
      while (en.hasMoreElements()) {
        String key = SandConvert.obj2Str(en.nextElement());
        String value = redisProps.getProperty(key);
        log.info("{}={}", new Object[]{key, value});
        // 加载到统一配置文件中
        Config.setProperty(key, value);
      }
      log.info("...Redis配置信息 加载完成");
    } else {
      log.info("Redis配置信息加载 未开启");
    }
  }
}
