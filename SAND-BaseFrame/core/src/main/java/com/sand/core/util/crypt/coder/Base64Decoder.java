/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/22   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.crypt.coder;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 功能说明：Base64解密
 * 开发人员：@author liusha
 * 开发日期：2019/8/22 13:36
 * 功能描述：BASE64解密
 */
@Slf4j
public class Base64Decoder extends CharacterDecoder {
  private static final char[] PEM_ARRAY = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
      'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
      'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
      '4', '5', '6', '7', '8', '9', '+', '/'};
  private static byte[] decode_buffer;
  private static final byte[] PEM_CONVERT_ARRAY;

  static {
    PEM_CONVERT_ARRAY = new byte[256];
    for (int i = 0; i < 255; i++) {
      PEM_CONVERT_ARRAY[i] = -1;
    }
    for (int j = 0; j < PEM_ARRAY.length; j++) {
      PEM_CONVERT_ARRAY[PEM_ARRAY[j]] = (byte) j;
    }
  }

  public Base64Decoder() {
    decode_buffer = new byte[4];
  }

  @Override
  protected int bytesPerAtom() {
    return 4;
  }

  @Override
  protected int bytesPerLine() {
    return 72;
  }

  @Override
  protected void decodeAtom(InputStream is, OutputStream os, int i) throws IOException {
    byte byte0 = -1;
    byte byte1 = -1;
    byte byte2 = -1;
    byte byte3 = -1;
    if (i < 2) {
      throw new IOException("Base64Decoder: Not enough bytes for an atom.");
    }
    int j;
    do {
      j = is.read();
      if (j == -1) {
        throw new IOException(EXCEPTION_MSG);
      }
    } while (j == 10 || j == 13);
    decode_buffer[0] = (byte) j;
    j = readFully(is, decode_buffer, 1, i - 1);
    if (j == -1) {
      throw new IOException(EXCEPTION_MSG);
    }
    if (i > 3 && decode_buffer[3] == 61) {
      i = 3;
    }
    if (i > 2 && decode_buffer[2] == 61) {
      i = 2;
    }
    switch (i) {
      case 4:
        byte3 = PEM_CONVERT_ARRAY[decode_buffer[3] & 0xff];
      case 3:
        byte2 = PEM_CONVERT_ARRAY[decode_buffer[2] & 0xff];
      case 2:
        byte1 = PEM_CONVERT_ARRAY[decode_buffer[1] & 0xff];
        byte0 = PEM_CONVERT_ARRAY[decode_buffer[0] & 0xff];
      default:
        switch (i) {
          case 2:
            os.write((byte) (byte0 << 2 & 0xfc | byte1 >>> 4 & 3));
            break;
          case 3:
            os.write((byte) (byte0 << 2 & 0xfc | byte1 >>> 4 & 3));
            os.write((byte) (byte1 << 4 & 0xf0 | byte2 >>> 2 & 0xf));
            break;
          case 4:
            os.write((byte) (byte0 << 2 & 0xfc | byte1 >>> 4 & 3));
            os.write((byte) (byte1 << 4 & 0xf0 | byte2 >>> 2 & 0xf));
            os.write((byte) (byte2 << 6 & 0xc0 | byte3 & 0x3f));
            break;
          default:
            log.info("Base64Decoder.decodeAtom().");
            break;
        }
        break;
    }
  }

  public String decodeStr(String str) throws IOException {
    return new String(decodeBuffer(str));
  }

  public byte[] decodeBuffer(InputStream is) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    decodeBuffer(is, os);
    return os.toByteArray();
  }

  public byte[] decodeBuffer(String str) throws IOException {
    byte[] bytes = str.getBytes();
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    decodeBuffer(is, os);
    return os.toByteArray();
  }

}
