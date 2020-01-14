/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.crypt.md5;

import com.sand.base.enums.CodeEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.lang3.StringUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 功能说明：md5加密
 * 开发人员：@author liusha
 * 开发日期：2019/11/27 16:22
 * 功能描述：单向加密
 */
public class Md5Util {

  private static final String ALGORITHM = "md5";
  private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  private static final int BUFFER_SIZE = 256 * 1024;
  private static final String DEFAULT_SALT = "LSand-salt-for-md5";

  /**
   * 随机盐值加密
   *
   * @param input
   * @return
   */
  public static Md5Result randomSaltStrMD5(String input) {
    Md5Result md5Result = Md5Result.newInstance();
    md5Result.setMd5(strMD5(md5Result.getSalt()));
    return md5Result;
  }

  /**
   * 带盐值加密
   *
   * @param input
   * @param salt
   * @return
   */
  public static Md5Result saltStringMD5(String input, String salt) {
    if (StringUtil.isBlank(salt)) {
      salt = DEFAULT_SALT;
    }
    return new Md5Result(salt, strMD5(input + salt));
  }

  /**
   * 字符串的md5加密
   *
   * @param input
   * @return
   */
  public static String strMD5(String input) {
    try {
      // 输入的字符串转换成字节数组
      byte[] inputByteArray = input.getBytes();
      MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
      messageDigest.update(inputByteArray);
      // 转换并返回结果，也是字节数组，包含16个元素
      byte[] resultByteArray = messageDigest.digest();
      // 字符数组转换成十六进制字符串返回
      return bufferToHex(resultByteArray);
    } catch (NoSuchAlgorithmException e) {
      throw new LsException("md5加密初始化失败！", e);
    } catch (Exception e) {
      throw new LsException("md5加密失败！", e);
    }
  }

  /**
   * 文件的md5加密
   *
   * @param filePath
   * @return
   */
  public static String fileMD5(String filePath) {
    try {
      return fileMD5(new FileInputStream(filePath));
    } catch (FileNotFoundException e) {
      throw new LsException(CodeEnum.FILE_NOT_EXIST);
    }
  }

  /**
   * 文件md5值
   *
   * @param fileInputStream
   * @return
   */
  public static String fileMD5(InputStream fileInputStream) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
      try (
          DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
      ) {
        // read的过程中进行MD5处理，直到读完文件
        byte[] buffer = new byte[BUFFER_SIZE];
        while (digestInputStream.read(buffer) > 0) ;
        // 拿到结果，也是字节数组，包含16个元素
        byte[] resultByteArray = digestInputStream.getMessageDigest().digest();
        // 把字节数组转换成字符串
        return bufferToHex(resultByteArray);
      }
    } catch (NoSuchAlgorithmException e) {
      throw new LsException("md5加密初始化失败!", e);
    } catch (Exception e) {
      throw new LsException("md5加密失败！", e);
    }
  }

  /**
   * 转十六进制数
   *
   * @param bytes
   * @return
   */
  private static String bufferToHex(byte bytes[]) {
    return bufferToHex(bytes, 0, bytes.length);
  }

  /**
   * 转十六进制数
   *
   * @param bytes
   * @param start
   * @param length
   * @return
   */
  private static String bufferToHex(byte bytes[], int start, int length) {
    StringBuffer stringbuffer = new StringBuffer(2 * length);
    int end = start + length;
    for (int i = start; i < end; i++) {
      appendHexPair(bytes[i], stringbuffer);
    }
    return stringbuffer.toString();
  }

  /**
   * byte值映射为十六进制数
   *
   * @param bt
   * @param stringbuffer
   */
  private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
    char c0 = HEX_DIGITS[(bt & 0xf0) >> 4];
    char c1 = HEX_DIGITS[bt & 0xf];
    stringbuffer.append(c0);
    stringbuffer.append(c1);
  }

}
