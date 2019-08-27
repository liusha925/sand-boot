/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/23   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.demo;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sand.demo.entity.DemoUser;
import com.sand.demo.mapper.DemoUserMapper;
import com.sand.util.JunitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 功能说明：
 * 开发人员：@author liusha
 * 开发日期：2019/8/23 13:43
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
public class DemoUserMapperTest extends JunitTest {
  @Autowired
  private DemoUserMapper demoUserMapper;

  @Test
  public void userList() {
    Wrapper<DemoUser> wrapper = new QueryWrapper<>();
    List<DemoUser> userList = demoUserMapper.selectList(wrapper);
    userList.forEach(user -> log.info(user.toString()));
  }

}
