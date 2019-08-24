/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/23   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.demo;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sand.demo.entity.UserEntity;
import com.sand.demo.mapper.UserMapper;
import com.sand.util.JunitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 功能说明：
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/23 13:43
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
public class UserTest extends JunitTest {
  @Autowired
  private UserMapper userMapper;

  @Test
  public void pageList() {
    Wrapper<UserEntity> wrapper = new EntityWrapper<>();
    List<UserEntity> userList = userMapper.selectList(wrapper);
    userList.forEach(System.out::println);
  }

}
