/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/10/29    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sand.business.parent.base.BaseEntity;
import com.sand.business.parent.constant.Constant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 功能说明：系统日志
 * 开发人员：@author liusha
 * 开发日期：2019/10/29 14:51
 * 功能描述：系统日志
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@TableName(Constant.TABLE_SYS + "log")
public class SysLog extends BaseEntity {
  private static final long serialVersionUID = -8322909114249380529L;
  /**
   * 日志ID
   */
  @TableId
  private String logId;
  /**
   * 模块标识
   */
  private String symbol;
  /**
   * 请求用户
   */
  private String userName;
  /**
   * 操作系统
   */
  private String os;
  /**
   * 浏览器
   */
  private String browser;
  /**
   * 请求IP
   */
  private String addIp;
  /**
   * 请求URL
   */
  private String url;
  /**
   * 请求方法
   */
  private String method;
  /**
   * 请求方式
   */
  private String requestMethod;
  /**
   * 请求参数
   */
  private String requestParams;
  /**
   * 执行时间(ms)
   */
  private String exeTime;
  /**
   * 执行状态(0-初始化 1-执行成功 2-执行异常)
   */
  private int exeStatus;
  /**
   * 异常类
   */
  private String exceptionClz;
  /**
   * 异常信息
   */
  private String exceptionMsg;
}
