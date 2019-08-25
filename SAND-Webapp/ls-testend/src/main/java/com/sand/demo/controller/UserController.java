/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/23   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.demo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.util.Map;

/**
 * 功能说明：用户信息
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/23 13:27
 * 功能描述：用户CRUD
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private IUserService userService;

  @RequestMapping("/pageList")
  public Ret pageList(@RequestBody Map<String, Object> map) {
    log.info("pageList param：{}", map);
    Wrapper<UserEntity> wrapper = new QueryWrapper<>();
    List<UserEntity> userList = userService.list(wrapper);
    userList.forEach(dbUser -> log.info(dbUser.toString()));
    return RetUtil.ok(userList);
  }
}
