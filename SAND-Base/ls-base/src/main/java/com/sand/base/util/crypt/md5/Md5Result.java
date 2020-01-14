/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.crypt.md5;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

/**
 * 功能说明：md5加密结果
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 16:24
 * 功能描述：md5加密结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Md5Result {

  /**
   * 盐值
   */
  private String salt;
  /**
   * md5结果值
   */
  private String md5;

  public Md5Result(String salt) {
    this.salt = salt;
  }

  public static Md5Result newInstance() {
    Random r = new Random();
    StringBuilder sb = new StringBuilder(16)
        .append(r.nextInt(99999999)).append(r.nextInt(99999999));
    for (int i = 0, len = 16 - sb.length(); i < len; i++) {
      sb.append("0");
    }
    return new Md5Result(sb.toString());
  }
}
