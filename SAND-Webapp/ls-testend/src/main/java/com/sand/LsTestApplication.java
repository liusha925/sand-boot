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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能说明：测试启动器
 * 开发人员：@author liusha
 * 开发日期：2019/8/20 20:15
 * 功能描述：测试启动器
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.sand"})
@MapperScan(basePackages = {"com.sand.*.mapper"})
public class LsTestApplication {
  public static void main(String[] args) {
    SpringApplication.run(LsTestApplication.class, args);
    log.info("                                            \n" +
        "            (♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ     \n" +
        "         .----------------. .----------------.    \n" +
        "        | .--------------. | .--------------. |   \n" +
        "        | |   _____      | | |    _______   | |   \n" +
        "        | |  |_   _|     | | |   /  ___  |  | |   \n" +
        "        | |    | |       | | |  |  (__ \\_|  | |  \n" +
        "        | |    | |   _   | | |   '.___`-.   | |   \n" +
        "        | |   _| |__/ |  | | |  |`\\____) |  | |  \n" +
        "        | |  |________|  | | |  |_______.'  | |   \n" +
        "        | |              | | |              | |   \n" +
        "        | '--------------' | '--------------' |   \n" +
        "         '----------------' '----------------'    \n" +
        "           :: SAND ::        (v1.0.0-SNAPSHOT)    \n");
  }
}
