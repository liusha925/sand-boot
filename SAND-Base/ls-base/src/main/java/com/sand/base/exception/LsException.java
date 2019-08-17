/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.exception;

import com.sand.base.enums.RetEnum;
import lombok.Data;

/**
 * 功能说明：自定义异常类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 10:32
 * 功能描述：自定义异常类
 */
@Data
public class LsException extends RuntimeException {
  private int code;

  public LsException() {
    super(RetEnum.ERROR.getMsg());
    this.code = RetEnum.ERROR.getCode();
  }

  public LsException(RetEnum retEnum) {
    super(retEnum.getMsg());
    this.code = retEnum.getCode();
  }

  public LsException(RetEnum retEnum, String msg) {
    super(msg);
    this.code = retEnum.getCode();
  }

  public LsException(String msg) {
    super(msg);
    this.code = RetEnum.ERROR.getCode();
  }

  public LsException(int code, String msg) {
    super(msg);
    this.code = code;
  }

  public LsException(String msg, Exception e) {
    super(msg, e);
    this.code = RetEnum.ERROR.getCode();
  }

  public LsException(RetEnum retEnum, Exception e) {
    super(retEnum.getMsg(), e);
    this.code = retEnum.getCode();
  }

  public LsException(RetEnum retEnum, String msg, Exception e) {
    super(msg, e);
    this.code = retEnum.getCode();
  }

  public LsException(int code, String msg, Exception e) {
    super(msg, e);
    this.code = code;
  }
}
