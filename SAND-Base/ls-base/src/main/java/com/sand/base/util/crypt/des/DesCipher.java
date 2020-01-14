/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.crypt.des;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.GeneralSecurityException;

/**
 * 功能说明：加密解密Text类型的数据
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 16:11
 * 功能描述：加密解密Text类型的数据实现类
 */
public class DesCipher implements ICipher {
  /**
   * 加密
   */
  private Cipher encryptCipher;

  /**
   * 解密
   */
  private Cipher decryptCipher;

  /**
   * KeyFactory
   */
  private SecretKeyFactory keyFactory;

  public DesCipher() {
    try {
      encryptCipher = Cipher.getInstance("DES");
      decryptCipher = Cipher.getInstance("DES");
      keyFactory = SecretKeyFactory.getInstance("DES");
    } catch (GeneralSecurityException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void init(String salt) {
    try {
      SecretKey sk = keyFactory.generateSecret(new DESKeySpec(salt.getBytes()));
      encryptCipher.init(Cipher.ENCRYPT_MODE, sk);
      decryptCipher.init(Cipher.DECRYPT_MODE, sk);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public String encrypt(String value) throws GeneralSecurityException {
    return new String(Base64.encodeBase64(encryptCipher.doFinal(value.getBytes())));
  }

  @Override
  public String decrypt(String value) throws GeneralSecurityException {
    return new String(decryptCipher.doFinal(Base64.decodeBase64(value.getBytes())));
  }
}
