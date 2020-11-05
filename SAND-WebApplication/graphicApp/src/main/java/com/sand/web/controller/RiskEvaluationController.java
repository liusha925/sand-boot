/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/8/23/023   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sand.web.entity.MerchantInfo;
import com.sand.web.service.IMerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 功能说明：大致描述 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/8/23/023 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Slf4j
@Controller
public class RiskEvaluationController {

  @Autowired
  private IMerchantInfoService merchantInfoService;

  @RequestMapping("/index")
  public String index(ModelMap model) {
    QueryWrapper<MerchantInfo> wrapper = new QueryWrapper<>();
    wrapper.select("merId", "merName");
    List<MerchantInfo> merchantInfoList = merchantInfoService.list(wrapper);
    model.addAttribute("list", merchantInfoList);
    return "index";
  }

  @ResponseBody
  @RequestMapping("/view")
  public Map<String, Object> view(String merId) {
    return merchantInfoService.view(merId);
  }
}
