/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/15   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.demo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sand.demo.entity.DemoUser;
import com.sand.demo.service.IDemoUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：20208/15 13:27
 * 功能描述：用户CRUD
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class DemoUserController {
  @Autowired
  private IDemoUserService userService;

  /**
   * 用户列表
   *
   * @param model model
   * @return 页面跳转
   */
  @GetMapping("/list")
  public ModelAndView list(Model model) {
    model.addAttribute("userList", getUserList());
    model.addAttribute("title", "用户管理");
    return new ModelAndView("user/list", "userModel", model);
  }

  /**
   * 用户详情
   *
   * @param id    用户id
   * @param model model
   * @return 页面跳转
   */
  @GetMapping("detail/{id}")
  public ModelAndView detail(@PathVariable("id") String id, Model model) {
    DemoUser user = userService.getById(id);
    model.addAttribute("user", user);
    model.addAttribute("title", "用户详情");
    return new ModelAndView("user/detail", "userModel", model);
  }

  /**
   * 添加用户-页面跳转
   *
   * @param model model
   * @return 页面跳转
   */
  @GetMapping("/form")
  public ModelAndView form(Model model) {
    model.addAttribute("user", new DemoUser());
    model.addAttribute("title", "添加用户");
    return new ModelAndView("user/add", "userModel", model);
  }

  /**
   * 添加用户
   *
   * @param user 用户dto
   * @return 页面跳转
   */
  @PostMapping("/add")
  public ModelAndView add(DemoUser user) {
    userService.saveOrUpdate(user);
    return new ModelAndView("redirect:/user/list");
  }

  /**
   * 删除用户
   *
   * @param id    用户id
   * @param model model
   * @return 页面跳转
   */
  @GetMapping(value = "delete/{id}")
  public ModelAndView delete(@PathVariable("id") String id, Model model) {
    userService.removeById(id);

    model.addAttribute("userList", getUserList());
    model.addAttribute("title", "用户管理");
    return new ModelAndView("user/list", "userModel", model);
  }

  /**
   * 修改用户
   *
   * @param id    用户id
   * @param model model
   * @return 页面跳转
   */
  @GetMapping(value = "modify/{id}")
  public ModelAndView modifyForm(@PathVariable("id") String id, Model model) {
    DemoUser user = userService.getById(id);

    model.addAttribute("user", user);
    model.addAttribute("title", "用户管理");
    return new ModelAndView("user/add", "userModel", model);
  }

  /**
   * 查询用户信息
   *
   * @return 用户信息
   */
  private List<DemoUser> getUserList() {
    Wrapper<DemoUser> wrapper = new QueryWrapper<>();
    List<DemoUser> userList = userService.list(wrapper);
    userList.forEach(dbUser -> log.info(dbUser.toString()));

    return userList;
  }
}
