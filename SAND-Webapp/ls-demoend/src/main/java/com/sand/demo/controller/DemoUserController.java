/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/23   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.demo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sand.base.core.controller.BaseController;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.util.ResultUtil;
import com.sand.base.util.ServletUtil;
import com.sand.demo.entity.DemoUser;
import com.sand.demo.service.IDemoUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2019/8/23 13:27
 * 功能描述：用户CRUD
 */
@Slf4j
@RestController
@RequestMapping("/demo/user")
public class DemoUserController extends BaseController {
  @Autowired
  private IDemoUserService userService;

  @RequestMapping("/page")
  public ResultEntity page(@RequestBody Map<String, Object> map) {
    // 测试@ModelAttribute
    log.info("访问IP：{}，系统和浏览器：{}", ServletUtil.getIp(), ServletUtil.getOSAndBrowser());
    log.info("page param：{}", map);
    Wrapper<DemoUser> wrapper = new QueryWrapper<>();
    List<DemoUser> userList = userService.list(wrapper);
    userList.forEach(dbUser -> log.info(dbUser.toString()));
    // 测试@ExceptionHandler
    System.out.println(1/0);
    return ResultUtil.ok(userList);
  }
}
