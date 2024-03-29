/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.crypt.des;

import com.sand.core.exception.BusinessException;
import com.sand.core.util.lang3.StringUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能说明：des加密解密工具类
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 16:07
 * 功能描述：双向加密，盐值加密解密，依赖com.sand.core.util.crypt.des.DesCipher
 */
@Slf4j
@NoArgsConstructor
public class DesCryptUtil {
  private static ICipher cipher;

  static {
    cipher = new DesCipher();
    cipher.init(ICipher.DEFAULT_SALT);
  }

  /**
   * 加密
   * <pre>
   *   System.out.println(DesCryptUtil.encrypt("123456")); = "olCPKgwi0rk="
   * </pre>
   *
   * @param encodeStr 待加密串
   * @return 加密串
   */
  public static String encrypt(String encodeStr) {
    if (StringUtil.isBlank(encodeStr)) {
      return encodeStr;
    }
    try {
      return cipher.encrypt(encodeStr.trim());
    } catch (Exception e) {
      throw new BusinessException("加密信息出错");
    }
  }

  /**
   * 解密
   * <pre>
   *   System.out.println(DesCryptUtil.decrypt("olCPKgwi0rk=")); = "123456"
   * </pre>
   *
   * @param encodeStr 待解密串
   * @return 解密串
   */
  public static String decrypt(String encodeStr) {
    if (StringUtil.isBlank(encodeStr)) {
      return encodeStr;
    }
    try {
      return cipher.decrypt(encodeStr.trim());
    } catch (Exception e) {
      throw new BusinessException("解密信息出错");
    }
  }

  public static void main(String[] args) {
    System.out.println(DesCryptUtil.encrypt("123456"));
  }

}
