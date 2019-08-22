/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/20   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能说明：测试启动器
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/20 20:15
 * 功能描述：测试启动器
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.sand")
public class LsTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(LsTestApplication.class, args);
    log.info("LsTestApplication started.");
  }
}
