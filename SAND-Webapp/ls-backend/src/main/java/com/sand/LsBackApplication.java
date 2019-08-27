/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/20   liusha   新增
 * =========  ===========  =====================
 */
package com.sand;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能说明：管理后台启动类
 * 开发人员：@author liusha
 * 开发日期：2019/8/20 20:15
 * 功能描述：管理后台启动类
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.sand")
@MapperScan(basePackages = {"com.sand.*.mapper"})
public class LsBackApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(LsBackApplication.class);
    // 开启/关闭启动logo
    application.setBannerMode(Banner.Mode.CONSOLE);
    SpringApplication.run(LsBackApplication.class, args);
    log.info("                                   \n" +
        "【LsBackApplication】启动成功 ლ(´ڡ`ლ)ﾞ \n" +
        "   _____         _   _ _____            \n" +
        "  / ____|  /\\   | \\ | |  __ \\        \n" +
        " | (___   /  \\  |  \\| | |  | |        \n" +
        "  \\___ \\ / /\\ \\ | . ` | |  | |      \n" +
        "  ____) / ____ \\| |\\  | |__| |        \n" +
        " |_____/_/    \\_\\_| \\_|_____/        \n" +
        " :: SAND ::        (v1.0.0-SNAPSHOT)");
  }
}
