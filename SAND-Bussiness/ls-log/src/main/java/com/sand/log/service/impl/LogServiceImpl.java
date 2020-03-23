/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/10/29    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.log.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.log.annotation.LogAnnotation;
import com.sand.log.service.IBaseLogService;
import com.sand.base.util.text.LsConvert;
import com.sand.base.enums.DateEnum;
import com.sand.base.exception.BusinessException;
import com.sand.base.util.ServletUtil;
import com.sand.base.util.lang3.DateUtil;
import com.sand.base.util.lang3.StringUtil;
import com.sand.log.entity.Log;
import com.sand.log.mapper.LogMapper;
import com.sand.log.service.ILogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：系统日志
 * 开发人员：@author liusha
 * 开发日期：2019/10/29 15:37
 * 功能描述：系统日志
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements IBaseLogService, ILogService {
  private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

  @Override
  public Object init() {
    return new Log();
  }

  @Override
  public void beforeProceed(Object obj, Object[] args) {
    Log log = (Log) obj;
    List<Object> requestParams = new ArrayList<>();
    Arrays.stream(args).forEach(arg -> {
      if (!(arg instanceof Log)) {
        requestParams.add(arg);
      }
    });
    Map<String, Object> agentMap = ServletUtil.getOSAndBrowser();
    log.setOs(LsConvert.obj2Str(agentMap.get(ServletUtil.OS)));
    log.setBrowser(LsConvert.obj2Str(agentMap.get(ServletUtil.BROWSER)));
    log.setAddIp(ServletUtil.getIp());
    log.setUrl(LsConvert.obj2Str(ServletUtil.getRequest().getRequestURL()));
    log.setRequestMethod(LsConvert.obj2Str(ServletUtil.getRequest().getMethod()));
    log.setRequestParams(JSON.toJSONString(requestParams));
    log.setUserName("超级管理员");
    log.setCreateBy("超级管理员");
  }

  @Override
  public void exceptionProceed(Object obj, Throwable ep) {
    Log log = (Log) obj;
    log.setExceptionClz(ep.getClass().getName());
    log.setExceptionMsg(ep.getMessage());
  }

  @Override
  public void afterProceed(Object obj, Method method) {
    Log log = (Log) obj;
    LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
    String symbol = log.getSymbol();
    if (StringUtil.isBlank(symbol)) {
      symbol = logAnnotation.symbol();
    }
    String remark = log.getRemark();
    if (StringUtil.isBlank(remark)) {
      remark = logAnnotation.description();
    }
    String methodName = method.getDeclaringClass() + "." + method.getName() + "()";
    log.setSymbol(symbol);
    log.setRemark(remark);
    log.setMethod(methodName);
  }

  @Override
  @Transactional(rollbackFor = BusinessException.class)
  public void save(Object obj, long exeTime, int exeStatus) {
    Log log = (Log) obj;
    log.setExeTime(LsConvert.obj2Str(exeTime));
    log.setExeStatus(exeStatus);
    logger.info(new StringBuilder().append("用户：").append(StringUtil.isBlank(log.getUserName()) ? "匿名用户" : log.getUserName()).append("，于")
        .append(DateUtil.getNow(DateEnum.F1_YYYY_MM_DD_HH_MM_SS_SSS)).append("进行了[").append(log.getRemark()).append("]操作")
        .append("，耗时").append(log.getExeTime()).append("毫秒，URL：").append(log.getUrl()).toString());
    super.save(log);
  }
}
