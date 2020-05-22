/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.exception;

import com.sand.common.vo.ResultVO;
import lombok.Data;

/**
 * 功能说明：自定义异常类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 10:32
 * 功能描述：自定义异常类
 */
@Data
public class BusinessException extends RuntimeException {
  private int code;

  public BusinessException() {
    super(ResultVO.Code.ERROR.getVName());
    this.code = ResultVO.Code.ERROR.getValue();
  }

  public BusinessException(ResultVO.Code code) {
    super(code.getVName());
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
    super(code.getVName(), e);
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
