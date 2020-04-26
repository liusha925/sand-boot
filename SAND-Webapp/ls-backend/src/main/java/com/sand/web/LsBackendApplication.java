/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/20   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web;

import com.sand.redis.config.init.RedisConfigRunner;
import com.sand.redis.config.init.RedisSentinelRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

/**
 * 功能说明：管理后台启动类
 * 开发人员：@author liusha
 * 开发日期：2019/8/20 20:15
 * 功能描述：管理后台启动类
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.sand")
public class LsBackendApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(LsBackendApplication.class);
    // 开启/关闭启动logo
    application.setBannerMode(Banner.Mode.CONSOLE);
    // 加载自定义配置参数
    String[] configs = new String[]{
        // 开启Redis配置加载
        RedisConfigRunner.CONFIG_APPLIED,
        // 开启Redis哨兵系统
        RedisSentinelRunner.SENTINEL_APPLIED
    };
    String[] newArgs = StringUtils.concatenateStringArrays(args, configs);
    SpringApplication.run(LsBackendApplication.class, newArgs);
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
