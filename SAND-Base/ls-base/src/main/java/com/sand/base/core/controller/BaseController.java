/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.core.controller;

import com.sand.base.core.common.BaseCommon;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.enums.ResultEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.editor.DateEditor;
import com.sand.base.util.editor.StringEditor;
import com.sand.base.util.result.ResultUtil;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 功能说明：Web层通用数据处理
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 21:43
 * 功能描述：定义所有控制器的父控制器，用来转化数据类型
 */
public class BaseController extends BaseCommon {
  /**
   * 属性访问器
   *
   * @param request
   * @param response
   */
  @ModelAttribute
  public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }

  /**
   * 属性编辑器
   *
   * @param binder
   */
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    // Date类型转换，几乎支持所有的日期类型
    binder.registerCustomEditor(Date.class, new DateEditor());
    // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
    binder.registerCustomEditor(String.class, new StringEditor());
  }

  /**
   * 顶级的异常处理
   *
   * @param e
   * @return
   */
  @ExceptionHandler(Exception.class)
  public ResultEntity handleException(Exception e) {
    super.errorLog(e);
    return ResultUtil.fail();
  }

  /**
   * 自定义异常处理
   *
   * @param e
   * @return
   */
  @ExceptionHandler(LsException.class)
  public ResultEntity handleLsException(LsException e) {
    super.errorLog(e);
    return ResultUtil.result(e.getCode(), e.getMessage());
  }

  /**
   * 参数缺失处理
   *
   * @param e
   * @return
   */
  @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
  public ResultEntity handleMissingParamException(Exception e) {
    super.errorLog(e);
    return ResultUtil.fail(ResultEnum.PARAM_MISSING_ERROR);
  }

}
