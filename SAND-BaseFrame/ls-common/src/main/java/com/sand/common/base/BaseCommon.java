/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.base;

import com.sand.common.exception.BusinessException;
import com.sand.common.util.editor.DateEditor;
import com.sand.common.util.editor.StringEditor;
import com.sand.common.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 功能说明：Web层通用数据处理
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 21:43
 * 功能描述：定义所有控制器的父控制器，进行属性绑定、数据转换、异常处理
 */
@Slf4j
public class BaseCommon {
  protected HttpSession session;
  protected HttpServletRequest request;
  protected HttpServletResponse response;

  /**
   * 业务处理异常
   */
  protected void newBusinessException(String msg) {
    throw new BusinessException(ResultVO.Code.BUSINESS_ERROR, msg);
  }

  /**
   * 属性访问器
   *
   * @param session  session
   * @param request  request
   * @param response response
   */
  @ModelAttribute
  public void modelAttribute(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
    this.session = session;
    this.request = request;
    this.response = response;
  }

  /**
   * 属性编辑器
   *
   * @param binder 数据绑定
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    // Date类型转换，几乎支持所有的日期类型
    binder.registerCustomEditor(Date.class, new DateEditor());
    // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
    binder.registerCustomEditor(String.class, new StringEditor());
  }

}
