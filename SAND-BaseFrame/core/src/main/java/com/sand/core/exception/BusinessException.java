/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.exception;

import com.sand.core.vo.ResultVO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 功能说明：自定义异常类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 10:32
 * 功能描述：自定义异常类
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
  private static final long serialVersionUID = -2181521031637425151L;
  private int code;

  public BusinessException() {
    super(ResultVO.Code.ERROR.getName());
    this.code = ResultVO.Code.ERROR.getValue();
  }

  public BusinessException(ResultVO.Code code) {
    super(code.getName());
    this.code = code.getValue();
  }

  public BusinessException(ResultVO.Code code, String msg) {
    super(msg);
    this.code = code.getValue();
  }

  public BusinessException(String msg) {
    super(msg);
    this.code = ResultVO.Code.ERROR.getValue();
  }

  public BusinessException(int code, String msg) {
    super(msg);
    this.code = code;
  }

  public BusinessException(String msg, Exception e) {
    super(msg, e);
    this.code = ResultVO.Code.ERROR.getValue();
  }

  public BusinessException(ResultVO.Code code, Exception e) {
    super(code.getName(), e);
    this.code = code.getValue();
  }

  public BusinessException(ResultVO.Code code, String msg, Exception e) {
    super(msg, e);
    this.code = code.getValue();
  }

  public BusinessException(int code, String msg, Exception e) {
    super(msg, e);
    this.code = code;
  }
}
