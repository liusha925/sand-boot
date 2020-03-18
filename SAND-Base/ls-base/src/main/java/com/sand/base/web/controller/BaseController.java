/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.web.controller;

import com.sand.base.web.common.BaseCommon;
import com.sand.base.web.entity.ResultEntity;
import com.sand.base.enums.CodeEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.ResultUtil;
import com.sand.base.util.editor.DateEditor;
import com.sand.base.util.editor.StringEditor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
public class BaseController extends BaseCommon {
  protected HttpSession session;
  protected HttpServletRequest request;
  protected HttpServletResponse response;

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

  /**
   * 顶级异常处理
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(Exception.class)
  public ResultEntity handleException(Exception e) {
    super.errorLog(e);
    return ResultUtil.error();
  }

  /**
   * 自定义异常处理
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(LsException.class)
  public ResultEntity handleLsException(LsException e) {
    super.errorLog(e);
    return ResultUtil.info(e.getCode(), e.getMessage());
  }

  /**
   * 参数缺失处理
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResultEntity handleMissingParamException(MissingServletRequestParameterException e) {
    super.errorLog(e);
    return ResultUtil.error(CodeEnum.PARAM_MISSING_ERROR);
  }

  /**
   * JSON反序列化失败
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResultEntity handleMessageNotReadableException(HttpMessageNotReadableException e) {
    super.errorLog(e);
    return ResultUtil.error(CodeEnum.DESERIALIZE_ERROR);
  }

}
