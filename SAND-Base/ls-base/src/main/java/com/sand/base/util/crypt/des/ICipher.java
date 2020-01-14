/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.crypt.des;

import java.security.GeneralSecurityException;

/**
 * 功能说明：加密解密Text类型的数据
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 16:09
 * 功能描述：加密解密Text类型的数据
 */
public interface ICipher {
  String SALT = "LSand";

  /**
   * 初始化
   *
   * @param salt
   */
  void init(String salt);

  /**
   * 加密信息
   *
   * @param value
   * @return
   * @throws GeneralSecurityException
   */
  String encrypt(String value) throws GeneralSecurityException;

  /**
   * 解密信息
   *
   * @param value
   * @return
   * @throws GeneralSecurityException
   */
  String decrypt(String value) throws GeneralSecurityException;
}
