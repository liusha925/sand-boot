/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/8/16/016   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.web.controller;

import com.sand.web.entity.DemoUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 功能说明：大致描述 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/8/16/016 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Slf4j
@Controller
@RequestMapping("/demo")
public class DemoUserController {

  @RequestMapping("/userList")
  public String userList(ModelMap model) {
    DemoUser user = new DemoUser();
    user.setName("hsh");
    user.setAge(26);
    model.addAttribute("user", user);
    return "demo/userList";
  }
}
