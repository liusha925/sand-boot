/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/14    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.controller;

import com.sand.web.domain.User;
import com.sand.web.repository.UserRepository;
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
 * 功能说明：
 * 开发人员：@author liusha
 * 开发日期：2020/8/14 7:52
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @RequestMapping("/hello")
  public String hello(){
    return "Hello！";
  }

  /**
   * 查询所用用户
   *
   * @return
   */
  @GetMapping("/list")
  public ModelAndView list(Model model) {
    model.addAttribute("userList", getUserList());
    model.addAttribute("title", "用户管理");
    return new ModelAndView("users/list", "userModel", model);
  }

  /**
   * 根据id查询用户
   *
   * @return
   */
  @GetMapping("{id}")
  public ModelAndView view(@PathVariable("id") Long id, Model model) {
    User user = userRepository.getUserById(id);
    model.addAttribute("user", user);
    model.addAttribute("title", "查看用户");
    return new ModelAndView("users/view", "userModel", model);
  }

  /**
   * 获取 form 表单页面
   *
   * @return
   */
  @GetMapping("/form")
  public ModelAndView createForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("title", "创建用户");
    return new ModelAndView("users/form", "userModel", model);
  }

  /**
   * 新建用户
   *
   * @param user
   * @return
   */
  @PostMapping
  public ModelAndView create(User user) {
    userRepository.saveOrUpdateUser(user);
    return new ModelAndView("redirect:/users");
  }

  /**
   * 删除用户
   *
   * @param id
   * @return
   */
  @GetMapping(value = "delete/{id}")
  public ModelAndView delete(@PathVariable("id") Long id, Model model) {
    userRepository.deleteUser(id);

    model.addAttribute("userList", getUserList());
    model.addAttribute("title", "删除用户");
    return new ModelAndView("users/list", "userModel", model);
  }

  /**
   * 修改用户
   */
  @GetMapping(value = "modify/{id}")
  public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
    User user = userRepository.getUserById(id);

    model.addAttribute("user", user);
    model.addAttribute("title", "修改用户");
    return new ModelAndView("users/form", "userModel", model);
  }

  /**
   * 从 用户存储库 获取用户列表
   *
   * @return
   */
  private List<User> getUserList() {
    return userRepository.listUser();
  }
}
