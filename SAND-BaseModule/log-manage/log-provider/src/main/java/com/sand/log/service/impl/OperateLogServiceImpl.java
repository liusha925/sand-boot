/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/10/29    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.core.exception.BusinessException;
import com.sand.core.util.ServletUtil;
import com.sand.core.util.convert.SandConvert;
import com.sand.core.util.json.JsonUtil;
import com.sand.core.util.lang3.DateUtil;
import com.sand.core.util.lang3.StringUtil;
import com.sand.log.annotation.LogAnnotation;
import com.sand.log.entity.OperateLog;
import com.sand.log.mapper.OperateLogMapper;
import com.sand.log.service.ILogService;
import com.sand.log.service.IOperateLogService;
import lombok.extern.slf4j.Slf4j;
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
 * 功能说明：操作日志
 * 开发人员：@author liusha
 * 开发日期：2020/08/22 15:31
 * 功能描述：操作日志
 */
@Slf4j
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements ILogService, IOperateLogService {
  private static final Logger logger = LoggerFactory.getLogger(OperateLogServiceImpl.class);

  @Override
  public Object init() {
    return new OperateLog();
  }

  @Override
  public void beforeProceed(Object obj, Object[] args) {
    OperateLog log = (OperateLog) obj;
    List<Object> requestParams = new ArrayList<>();
    Arrays.stream(args).forEach(arg -> {
      if (!(arg instanceof OperateLog)) {
        requestParams.add(arg);
      }
    });
    Map<String, Object> agentMap = ServletUtil.getOSAndBrowser();
    log.setOs(SandConvert.obj2Str(agentMap.get(ServletUtil.OS)));
    log.setBrowser(SandConvert.obj2Str(agentMap.get(ServletUtil.BROWSER)));
    log.setAddIp(ServletUtil.getRemoteAddress());
    log.setUrl(SandConvert.obj2Str(ServletUtil.getRequest().getRequestURL()));
    log.setRequestMethod(ServletUtil.getRequest().getMethod());
    log.setRequestParams(JsonUtil.format(requestParams));
    // 获取用户信息
//    try {
//      Object user = AuthenticationUtil.getUser();
//      if (user instanceof SysUser) {
//        SysUser sysUser = (SysUser) user;
//        log.setUserName(sysUser.getUsername());
//        log.setCreateBy(sysUser.getRealName());
//        log.setUpdateBy(sysUser.getRealName());
//      } else {
//        logger.info("用户类型非法！");
//      }
//    } catch (Exception e) {
//      logger.error("获取登录用户信息异常", e);
//    }
  }

  @Override
  public void exceptionProceed(Object obj, Throwable ep) {
    OperateLog log = (OperateLog) obj;
    log.setExceptionClz(ep.getClass().getName());
    log.setExceptionMsg(ep.getMessage());
  }

  @Override
  public void afterProceed(Object obj, Method method) {
    OperateLog log = (OperateLog) obj;
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
    OperateLog log = (OperateLog) obj;
    log.setExeTime(SandConvert.obj2Str(exeTime));
    log.setExeStatus(exeStatus);
    logger.info(new StringBuilder().append("用户：").append(StringUtil.isBlank(log.getUserName()) ? "匿名用户" : log.getUserName()).append("，于")
        .append(DateUtil.getNow(DateUtil.Format.F1_YYYY_MM_DD_HH_MM_SS_SSS)).append("进行了[").append(log.getRemark()).append("]操作")
        .append("，耗时").append(log.getExeTime()).append("毫秒，URL：").append(log.getUrl()).toString());
    super.save(log);
  }

}
