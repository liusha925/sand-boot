/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.crypt.des;

import java.security.GeneralSecurityException;

/**
 * 功能说明：加密解密Text类型的数据
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 16:09
 * 功能描述：双向加解密工具，适合用于加密一些敏感信息，如身份证、银行卡、手机号码等信息
 */
public interface ICipher {
  /**
   * 默认盐值，长度不能少于8位
   */
  String DEFAULT_SALT = "LSand2019";

  /**
   * 初始化
   *
   * @param salt 盐值，salt.length>=8
   */
  void init(String salt);

  /**
   * 加密信息
   *
   * @param value 待加密字符串
   * @return 加密字符串
   * @throws GeneralSecurityException
   */
  String encrypt(String value) throws GeneralSecurityException;

  /**
   * 解密信息
   *
   * @param value 待解密字符串
   * @return 解密字符串
   * @throws GeneralSecurityException
   */
  String decrypt(String value) throws GeneralSecurityException;
}
