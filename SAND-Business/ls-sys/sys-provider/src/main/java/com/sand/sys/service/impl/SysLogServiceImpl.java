/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/10/29    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.common.exception.BusinessException;
import com.sand.common.util.ServletUtil;
import com.sand.common.util.lang3.DateUtil;
import com.sand.common.util.lang3.StringUtil;
import com.sand.common.util.convert.SandConvert;
import com.sand.log.annotation.LogAnnotation;
import com.sand.log.service.ILogService;
import com.sand.sys.entity.SysLog;
import com.sand.sys.mapper.SysLogMapper;
import com.sand.sys.service.ISysLogService;
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
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ILogService, ISysLogService {
  private static final Logger logger = LoggerFactory.getLogger(SysLogServiceImpl.class);

  @Override
  public Object init() {
    return new SysLog();
  }

  @Override
  public void beforeProceed(Object obj, Object[] args) {
    SysLog log = (SysLog) obj;
    List<Object> requestParams = new ArrayList<>();
    Arrays.stream(args).forEach(arg -> {
      if (!(arg instanceof SysLog)) {
        requestParams.add(arg);
      }
    });
    Map<String, Object> agentMap = ServletUtil.getOSAndBrowser();
    log.setOs(SandConvert.obj2Str(agentMap.get(ServletUtil.OS)));
    log.setBrowser(SandConvert.obj2Str(agentMap.get(ServletUtil.BROWSER)));
    log.setAddIp(ServletUtil.getRemoteAddress());
    log.setUrl(SandConvert.obj2Str(ServletUtil.getRequest().getRequestURL()));
    log.setRequestMethod(SandConvert.obj2Str(ServletUtil.getRequest().getMethod()));
    log.setRequestParams(JSON.toJSONString(requestParams));
    log.setUserName("超级管理员");
    log.setCreateBy("超级管理员");
  }

  @Override
  public void exceptionProceed(Object obj, Throwable ep) {
    SysLog log = (SysLog) obj;
    log.setExceptionClz(ep.getClass().getName());
    log.setExceptionMsg(ep.getMessage());
  }

  @Override
  public void afterProceed(Object obj, Method method) {
    SysLog log = (SysLog) obj;
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
    SysLog log = (SysLog) obj;
    log.setExeTime(SandConvert.obj2Str(exeTime));
    log.setExeStatus(exeStatus);
    logger.info(new StringBuilder().append("用户：").append(StringUtil.isBlank(log.getUserName()) ? "匿名用户" : log.getUserName()).append("，于")
        .append(DateUtil.getNow(DateUtil.Format.F1_YYYY_MM_DD_HH_MM_SS_SSS)).append("进行了[").append(log.getRemark()).append("]操作")
        .append("，耗时").append(log.getExeTime()).append("毫秒，URL：").append(log.getUrl()).toString());
    super.save(log);
  }
}
