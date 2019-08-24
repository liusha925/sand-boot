/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/23   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.demo.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sand.base.util.result.Ret;
import com.sand.base.util.result.RetUtil;
import com.sand.demo.entity.UserEntity;
import com.sand.demo.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能说明：
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/23 13:27
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private IUserService userService;

  @RequestMapping("/pageList")
  public Ret pageList(@RequestBody UserEntity user) {
    log.info("pageList param：{}", user);
    Wrapper<UserEntity> wrapper = new EntityWrapper<>();
    List<UserEntity> userList = userService.selectList(wrapper);
    userList.forEach(System.out::println);
    return RetUtil.ok(userList);
  }
}
