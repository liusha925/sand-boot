/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/22   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.crypt.rsa;

import com.sand.base.util.crypt.base.Base64Decoder;
import com.sand.base.util.crypt.base.Base64Encoder;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 功能说明：RSA加解密工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/22 14:11
 * 功能描述：RSA加解密工具类
 */
@Slf4j
public class RsaUtil {
  public static final String PUBLIC_KEY = "publicKey";
  public static final String PRIVATE_KEY = "privateKey";
  public static final String RSA_ALGORITHM = "RSA";
  public static final String MD5_RSA_ALGORITHM = "MD5withRSA";

  public RsaUtil() {
  }

  /**
   * 私钥生成签名
   *
   * @param dataMap    待签名数据
   * @param privateKey 私钥
   * @return
   */
  public static String sign(TreeMap<String, Object> dataMap, String privateKey) {
    return sign(buildSign(dataMap), privateKey);
  }

  /**
   * 私钥生成签名
   *
   * @param data       待签名数据
   * @param privateKey 私钥
   * @return
   */
  public static String sign(String data, String privateKey) {
    // 用私钥对数据生成签名
    try {
      byte[] dataBytes = new Base64Decoder().decodeBuffer(data);
      byte[] keyBytes = new Base64Decoder().decodeBuffer(privateKey);
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory key = KeyFactory.getInstance(RSA_ALGORITHM);
      PrivateKey privateKeyed = key.generatePrivate(pkcs8EncodedKeySpec);
      Signature signature = Signature.getInstance(MD5_RSA_ALGORITHM);
      signature.initSign(privateKeyed);
      signature.update(dataBytes);
      return new Base64Encoder().encode(signature.sign());
    } catch (Exception e) {
      e.printStackTrace();
      log.error("生成签名失败：{}", e.getMessage());
    }
    return null;
  }

  /**
   * 公钥验证签名
   *
   * @param dataMap   待签名数据
   * @param signed    签名后数据
   * @param publicKey 公钥
   * @return
   */
  public static boolean verifySign(TreeMap<String, Object> dataMap, String signed, String publicKey) {
    return verifySign(buildSign(dataMap), signed, publicKey);
  }

  /**
   * 公钥验证签名
   *
   * @param data      待签名数据
   * @param signed    签名后数据
   * @param publicKey 公钥
   * @return
   */
  private static boolean verifySign(String data, String signed, String publicKey) {
    try {
      byte[] dataBytes = new Base64Decoder().decodeBuffer(data);
      byte[] keyBytes = new Base64Decoder().decodeBuffer(publicKey);
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory key = KeyFactory.getInstance(RSA_ALGORITHM);
      // 用公钥对签名串验证
      PublicKey publicKeyed = key.generatePublic(x509EncodedKeySpec);
      Signature signature = Signature.getInstance(MD5_RSA_ALGORITHM);
      signature.initVerify(publicKeyed);
      signature.update(dataBytes);
      return signature.verify(new Base64Decoder().decodeBuffer(signed));
    } catch (Exception e) {
      e.printStackTrace();
      log.error("验证签名失败：{}", e.getMessage());
      return false;
    }
  }

  /**
   * 生成有序的待签名串
   *
   * @param dataMap 待签名数据Map
   * @return
   */
  public static String buildSign(TreeMap<String, Object> dataMap) {
    Iterator<Map.Entry<String, Object>> it = dataMap.entrySet().iterator();
    StringBuffer sb = new StringBuffer();
    while (it.hasNext()) {
      Map.Entry<String, Object> entry = it.next();
      sb.append(entry.getKey()).append("=").append(entry.getValue());
      if (!dataMap.lastKey().equalsIgnoreCase(entry.getKey())) {
        sb.append("&");
      }
    }
    return sb.toString();
  }

  /**
   * 获取密钥对，公钥-私钥
   *
   * @return
   */
  public static Map<String, Object> getKeyPairs() {
    Map<String, Object> keyMap = new HashMap<>();
    KeyPairGenerator keyPairGenerator;
    try {
      keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
      keyPairGenerator.initialize(1024);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
      keyMap.put(PUBLIC_KEY, new Base64Encoder().encode(publicKey.getEncoded()));
      keyMap.put(PRIVATE_KEY, new Base64Encoder().encode(privateKey.getEncoded()));
    } catch (Exception e) {
      e.printStackTrace();
      log.error("生成秘钥对失败：{}", e.getMessage());
    }
    return keyMap;
  }

  public static void main(String[] args) {
    Map<String, Object> keyMap = getKeyPairs();
    System.out.println("公钥：" + keyMap.get(PUBLIC_KEY));
    System.out.println("|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|*|");
    System.out.println("私钥：" + keyMap.get(PRIVATE_KEY));
  }

}
