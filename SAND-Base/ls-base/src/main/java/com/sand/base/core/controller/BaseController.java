/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.enums.ResultEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.common.ServletUtil;
import com.sand.base.util.editor.DateEditor;
import com.sand.base.util.editor.StringEditor;
import com.sand.base.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * 功能说明：父控制器
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 21:43
 * 功能描述：定义所有控制器的父控制器，用来转化数据类型
 */
@Slf4j
public class BaseController {
  /**
   * 请求
   */
  protected HttpServletRequest request;
  /**
   * 响应
   */
  protected HttpServletResponse response;

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
    if (e.getCause() instanceof LsException) {
      return handleLsException((LsException) e.getCause());
    }
    errorLog(e);
    log.error("错误信息：{}", e);
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
    errorLog(e);
    return ResultUtil.result(null, e.getCode(), e.getMessage());
  }

  /**
   * 参数缺失处理
   *
   * @param e
   * @return
   */
  @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
  public ResultEntity handleMissingParamException(Exception e) {
    errorLog(e);
    return ResultUtil.fail(ResultEnum.PARAM_MISSING_ERROR);
  }

  /**
   * 打印出错log
   *
   * @param e
   */
  protected void errorLog(Exception e) {
    StackTraceElement element = e.getStackTrace()[0];
    if (e instanceof LsException) {
      log.info("异常位置：{}.{}，第{}行，原因：{}", element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getMessage());
      if (!Objects.isNull(e.getCause())) {
        StackTraceElement cause = e.getCause().getStackTrace()[0];
        log.info("起因：{}.{}，第{}行，原因：{}", cause.getClassName(), cause.getMethodName(), cause.getLineNumber(), e.getCause().getMessage());
      }
    } else {
      log.error("错误位置：{}.{}，第{}行，错误原因：{}", element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getClass().getName());
    }
  }

  /**
   * 获取IP
   *
   * @return
   */
  protected String getIp() {
    return ServletUtil.getIp(request);
  }

  /**
   * 获取系统和浏览器信息
   *
   * @return
   */
  protected Map<String, Object> getOSAndBrowserInfo() {
    return ServletUtil.getOSAndBrowserInfo(request);
  }

  /**
   * 将mybatis-plus的分页插件转换成需要的展示格式
   *
   * @param page
   * @return
   */
  protected Map<String, Object> page2map(Page page) {
    return page2map(page, page.getRecords());
  }

  /**
   * 将mybatis-plus的分页插件转换成需要的展示格式
   *
   * @param page
   * @param function
   * @return
   */
  protected Map<String, Object> page2map(Page page, Function<List, List> function) {
    return page2map(page, function.apply(page.getRecords()));
  }

  /**
   * 将mybatis-plus的分页插件转换成需要的展示格式
   *
   * @param page
   * @param list
   * @return
   */
  protected Map<String, Object> page2map(Page page, List list) {
    Map<String, Object> map = new HashMap<>();
    map.put("records", list);
    map.put("total", page.getTotal());
    map.put("pages", page.getPages());
    map.put("current", page.getCurrent());
    map.put("size", page.getSize());
    return map;
  }

}
