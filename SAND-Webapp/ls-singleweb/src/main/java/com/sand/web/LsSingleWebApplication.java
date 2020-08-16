/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/20   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web;

import com.sand.common.runner.FastJsonSafeRunner;
import com.sand.common.runner.SystemConfigRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

/**
 * 功能说明：启动类
 * 开发人员：@author liusha
 * 开发日期：2020/8/15 20:15
 * 功能描述：启动类
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.sand")
public class LsSingleWebApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(LsSingleWebApplication.class);
    // 开启/关闭启动logo
    application.setBannerMode(Banner.Mode.CONSOLE);
    // 加载自定义配置参数
    String[] configs = new String[]{
        // 开启系统变量加载
        SystemConfigRunner.APPLIED,
        // 开启fast json安全模式
        FastJsonSafeRunner.APPLIED
    };
    String[] newArgs = StringUtils.concatenateStringArrays(args, configs);
    SpringApplication.run(LsSingleWebApplication.class, newArgs);
    log.info("                                   \n" +
        "【LsSingleWebApplication】启动成功 ლ(´ڡ`ლ)ﾞ \n" +
        "   _____         _   _ _____            \n" +
        "  / ____|  /\\   | \\ | |  __ \\        \n" +
        " | (___   /  \\  |  \\| | |  | |        \n" +
        "  \\___ \\ / /\\ \\ | . ` | |  | |      \n" +
        "  ____) / ____ \\| |\\  | |__| |        \n" +
        " |_____/_/    \\_\\_| \\_|_____/        \n" +
        " :: SAND ::        (v1.0.0-SNAPSHOT)");
  }
}
