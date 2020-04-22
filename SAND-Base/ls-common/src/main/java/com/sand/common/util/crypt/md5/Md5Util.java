/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.crypt.md5;

import com.sand.common.enums.CodeEnum;
import com.sand.common.exception.BusinessException;
import lombok.NoArgsConstructor;

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
 * 功能描述：单向加密，适用于安全级别比较高的，如密码
 */
@NoArgsConstructor
public class Md5Util {
  private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  /**
   * 加密字符串
   * <pre>
   *   System.out.println(Md5Util.encryptStr("123456")); = "e10adc3949ba59abbe56e057f20f883e"
   * </pre>
   *
   * @param encryptStr 待加密字符串
   * @return 32位小写加密字符串
   */
  public static String encryptStr(String encryptStr) {
    try {
      // 输入的字符串转换成字节数组
      byte[] inputByteArray = encryptStr.getBytes();
      MessageDigest messageDigest = MessageDigest.getInstance("md5");
      messageDigest.update(inputByteArray);
      // 转换并返回结果，也是字节数组，包含16个元素
      byte[] resultByteArray = messageDigest.digest();
      // 字符数组转换成十六进制字符串返回
      return bufferToHex(resultByteArray);
    } catch (NoSuchAlgorithmException e) {
      throw new BusinessException("md5加密初始化失败！", e);
    } catch (Exception e) {
      throw new BusinessException("md5加密失败！", e);
    }
  }

  public static void main(String[] args) {
    System.out.println(Md5Util.encryptStr("123456"));
  }
  /**
   * 加密文件
   *
   * @param filePath 文件路径
   * @return
   */
  public static String encryptFile(String filePath) {
    try {
      return encryptFile(new FileInputStream(filePath));
    } catch (FileNotFoundException e) {
      throw new BusinessException(CodeEnum.FILE_NOT_EXIST);
    }
  }

  /**
   * 加密文件
   *
   * @param fileInputStream fileInputStream
   * @return
   */
  public static String encryptFile(InputStream fileInputStream) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("md5");
      try (
          DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, messageDigest)
      ) {
        // read的过程中进行MD5处理，直到读完文件
        byte[] buffer = new byte[256 * 1024];
        while (digestInputStream.read(buffer) > 0) ;
        // 拿到结果，也是字节数组，包含16个元素
        byte[] resultByteArray = digestInputStream.getMessageDigest().digest();
        // 把字节数组转换成字符串
        return bufferToHex(resultByteArray);
      }
    } catch (NoSuchAlgorithmException e) {
      throw new BusinessException("md5加密初始化失败!", e);
    } catch (Exception e) {
      throw new BusinessException("md5加密失败！", e);
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
