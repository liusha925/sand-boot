/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.exception;

import com.sand.base.enums.ResultEnum;
import lombok.Data;

/**
 * 功能说明：异常类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 10:32
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Data
public class LsException extends RuntimeException {
  private int code;

  public LsException() {
    super(ResultEnum.ERROR.getMsg());
    this.code = ResultEnum.ERROR.getCode();
  }

  public LsException(ResultEnum resultEnum) {
    super(resultEnum.getMsg());
    this.code = resultEnum.getCode();
  }

  public LsException(ResultEnum resultEnum, String msg) {
    super(msg);
    this.code = resultEnum.getCode();
  }

  public LsException(String msg) {
    super(msg);
    this.code = ResultEnum.ERROR.getCode();
  }

  public LsException(int code, String msg) {
    super(msg);
    this.code = code;
  }

  public LsException(String msg, Exception e) {
    super(msg, e);
    this.code = ResultEnum.ERROR.getCode();
  }

  public LsException(ResultEnum resultEnum, Exception e) {
    super(resultEnum.getMsg(), e);
    this.code = resultEnum.getCode();
  }

  public LsException(ResultEnum resultEnum, String msg, Exception e) {
    super(msg, e);
    this.code = resultEnum.getCode();
  }

  public LsException(int code, String msg, Exception e) {
    super(msg, e);
    this.code = code;
  }
}
