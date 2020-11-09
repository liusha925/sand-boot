/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/6/2    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.runner;

import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 功能说明：fast json安全模式
 * 开发人员：@author liusha
 * 开发日期：2020/6/2 8:30
 * 功能描述：针对0day漏洞问题，版本要求1.2.70或以上
 */
@Slf4j
@Component
@Order(value = 99)
public class FastJsonSafeRunner implements ApplicationRunner {
  /**
   * 安全模式标识
   */
  public static final String APPLIED = "__fast_json_safe_applied";

  @Override
  public void run(ApplicationArguments args) throws Exception {
    /**
     * fast json安全模式默认是关闭状态，若要开启，需在启动类上更改代码：
     * <pre>
     *    // fast json安全模式
     *    String[] configs = new String[]{
     *        // 开启fast json安全模式
     *        FastJsonSafeRunner.APPLIED
     *    };
     *    String[] newArgs = org.springframework.util.StringUtils.concatenateStringArrays(args, configs);
     *    SpringApplication.run(LsBackendApplication.class, newArgs);
     * </pre>
     */
    List<String> argsList = Arrays.asList(args.getSourceArgs());
    if (argsList.contains(APPLIED)) {
      try {
        log.info("Fast Json安全模式 启用");
        ParserConfig.getGlobalInstance().setSafeMode(true);
      } catch (Exception e) {
        log.info("Fast Json安全模式 启用异常", e);
      }
    } else {
      log.info("Fast Json安全模式 未启用");
    }
  }
}
