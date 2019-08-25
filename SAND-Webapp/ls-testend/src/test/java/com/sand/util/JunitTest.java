/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/23   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 功能说明：单元测试基类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/23 13:45
 * 功能描述：单元测试基类
 */
@Slf4j
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class JunitTest {

  @Before
  public void before() {
    log.info(">>>>>>>>>>>>>>>>>>>>>>测试开始<<<<<<<<<<<<<<<<<<<<<<<<<<<");
  }

  @After
  public void after() {
    log.info(">>>>>>>>>>>>>>>>>>>>>>测试结束<<<<<<<<<<<<<<<<<<<<<<<<<<<");
  }
}
