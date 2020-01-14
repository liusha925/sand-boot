/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.crypt.des;

import com.sand.base.exception.LsException;
import com.sand.base.util.lang3.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明：des加密解密工具类
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 16:07
 * 功能描述：des加密解密
 */
@Slf4j
public class DesCryptUtil {

  /**
   * 参数解密
   *
   * @param encodeStr 加密串
   * @return
   */
  public static Map<String, Object> decryptParams(String encodeStr) {
    Map<String, Object> req = new HashMap<>();
    String decryStr = decrypt(encodeStr);
    String[] params = decryStr.split("&");
    if (params != null) {
      for (int i = 0; i < params.length; i++) {
        String[] param = params[i].split("=");
        if (param != null && param.length == 2) {
          req.put(param[0], param[1]);
        }
      }
    }
    return req;
  }

  /**
   * 加密
   *
   * @param encodeStr 待加密串
   * @return
   */
  public static String encrypt(String encodeStr) {
    if (StringUtil.isBlank(encodeStr)) {
      return encodeStr;
    }
    ICipher cipher = new DesCipher();
    cipher.init(ICipher.SALT);
    try {
      return cipher.encrypt(encodeStr.trim());
    } catch (Exception e) {
      throw new LsException("加密信息出错");
    }
  }

  /**
   * 解密
   *
   * @param encodeStr 待解密串
   * @return
   */
  public static String decrypt(String encodeStr) {
    if (StringUtil.isBlank(encodeStr)) {
      return encodeStr;
    }
    ICipher cipher = new DesCipher();
    cipher.init(ICipher.SALT);
    try {
      return cipher.decrypt(encodeStr.trim());
    } catch (Exception e) {
      throw new LsException("解密信息出错");
    }
  }

  /**
   * 加密
   *
   * @param encodeStr 待加密串
   * @return
   */
  public static String encryptHPJR(String encodeStr) {
    if (StringUtil.isBlank(encodeStr)) {
      return encodeStr;
    }
    ICipher cipher = new DesCipher();
    cipher.init("CreditCloudRock");
    try {
      return cipher.encrypt(encodeStr.trim());
    } catch (Exception e) {
      throw new LsException("加密信息出错");
    }
  }

  /**
   * 解密
   *
   * @param encodeStr 待解密串
   * @return
   */
  public static String decryptHPJR(String encodeStr) {
    if (StringUtil.isBlank(encodeStr)) {
      return encodeStr;
    }
    ICipher cipher = new DesCipher();
    cipher.init("CreditCloudRock");
    try {
      return cipher.decrypt(encodeStr.trim());
    } catch (Exception e) {
      throw new LsException("解密信息出错");
    }
  }

}
