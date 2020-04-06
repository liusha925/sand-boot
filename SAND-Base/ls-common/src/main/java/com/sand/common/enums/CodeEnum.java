/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能说明：返回结果枚举类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 10:34
 * 功能描述：返回结果枚举类
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {
  // 通用结果集
  OK(200, "成功"),
  ERROR(100, "系统错误,请联系管理员"),
  BUSINESS_ERROR(101, "业务处理异常"),
  PARAM_MISSING_ERROR(102, "缺少必要参数"),
  PARAM_CHECKED_ERROR(103, "参数校验不通过"),
  DESERIALIZE_ERROR(104, "JSON反序列化失败"),
  // 登录认证
  USERNAME_NOT_FOUND(10000, "用户名或密码无效"),
  CREDENTIALS_EXPIRED(10001, "凭证已过期"),
  ACCOUNT_EXPIRED(10002, "此账号已过期"),
  DISABLED(10003, "此账号已被禁用"),
  LOCKED(10004, "此账号已被锁定"),
  LOGIN_EXPIRED(10011,"长时间未操作，请重新登录"),
  SINGLE_LOGIN(10012, "此账号已在他处登录，请重新登录"),

  FILE_NOT_EXIST(20000, "文件不存在"),
  ;

  private final int code;
  private final String msg;
}
