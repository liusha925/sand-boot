/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/23   liusha   新增
 * =========  ===========  =====================
 */
package com.sand;

import com.sand.redis.config.init.RedisConfigRunner;
import com.sand.redis.config.init.RedisSentinelRunner;
import com.sand.web.LsBackendApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 功能说明：单元测试启动类
 * 开发人员：@author liusha
 * 开发日期：2020/4/23 13:45
 * 功能描述：单元测试启动类
 */
@Slf4j
@RunWith(SpringRunner.class)
public class JunitBootStrap {

  @Before
  public void before() {
    // 加载自定义配置参数
    String[] configs = new String[]{
        // 开启Redis配置加载
        RedisConfigRunner.CONFIG_APPLIED,
        // 开启Redis哨兵系统
        RedisSentinelRunner.SENTINEL_APPLIED
    };
    SpringApplication.run(LsBackendApplication.class, configs);
    log.info(">>>>>>>>>>>>>>测试开始<<<<<<<<<<<<<<<");
  }

  @After
  public void after() {
    log.info(">>>>>>>>>>>>>>测试结束<<<<<<<<<<<<<<<");
  }
}
