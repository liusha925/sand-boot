/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/5/18    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 功能说明：加载系统变量信息
 * 开发人员：@author liusha
 * 开发日期：2020/5/18 9:22
 * 功能描述：目前只加载不做任何处理
 */
@Slf4j
@Component
@Order(value = 0)
public class SystemConfigRunner implements ApplicationRunner {
  /**
   * 系统变量配置标识
   */
  public static final String APPLIED = "__system_config_applied";

  @Override
  public void run(ApplicationArguments args) throws Exception {
    /**
     * 系统变量加载默认是关闭状态，若要开启，需在启动类上更改代码：
     * <pre>
     *    // 加载系统变量信息
     *    String[] configs = new String[]{
     *        // 开启系统变量加载
     *        SystemConfigRunner.APPLIED
     *    };
     *    String[] newArgs = org.springframework.util.StringUtils.concatenateStringArrays(args, configs);
     *    SpringApplication.run(LsBackendApplication.class, newArgs);
     * </pre>
     */
    List<String> argsList = Arrays.asList(args.getSourceArgs());
    if (argsList.contains(APPLIED)) {
      log.info("操作系统变量信息 加载开始...");
      Map<String, String> envMap = System.getenv();
      envMap.keySet().stream().map(key -> key + "=" + envMap.get(key)).forEach(log::info);
      log.info("...操作系统变量信息 加载完成");
      log.info("Java环境变量信息 加载开始...");
      Properties properties = System.getProperties();
      properties.keySet().stream().map(obj -> (String) obj).map(key -> key + "=" + properties.get(key)).forEach(log::info);
      log.info("...Java环境变量信息 加载完成");
    } else {
      log.info("系统变量信息加载 未开启");
    }
  }

}
