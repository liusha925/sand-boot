/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/22   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.security.base;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 功能说明：base64加密
 * 开发人员：@author liusha
 * 开发日期：2019/8/22 14:06
 * 功能描述：base64加密
 */
public class Base64Encoder extends CharacterEncoder {

  private static final char[] PEM_ARRAY = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
      'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
      'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
      '4', '5', '6', '7', '8', '9', '+', '/'};

  public Base64Encoder() {
  }

  @Override
  protected int bytesPerAtom() {
    return 3;
  }

  @Override
  protected int bytesPerLine() {
    return 57;
  }

  @Override
  protected void encodeAtom(OutputStream os, byte[] bytes, int i, int j) throws IOException {
    if (j == 1) {
      byte byte0 = bytes[i];
      int k = 0;
      os.write(PEM_ARRAY[byte0 >>> 2 & 0x3f]);
      os.write(PEM_ARRAY[(byte0 << 4 & 0x30) + (k >>> 4 & 0xf)]);
      os.write(61);
      os.write(61);
    } else if (j == 2) {
      byte byte1 = bytes[i];
      byte byte3 = bytes[i + 1];
      int l = 0;
      os.write(PEM_ARRAY[byte1 >>> 2 & 0x3f]);
      os.write(PEM_ARRAY[(byte1 << 4 & 0x30) + (byte3 >>> 4 & 0xf)]);
      os.write(PEM_ARRAY[(byte3 << 2 & 0x3c) + (l >>> 6 & 3)]);
      os.write(61);
    } else {
      byte byte2 = bytes[i];
      byte byte4 = bytes[i + 1];
      byte byte5 = bytes[i + 2];
      os.write(PEM_ARRAY[byte2 >>> 2 & 0x3f]);
      os.write(PEM_ARRAY[(byte2 << 4 & 0x30) + (byte4 >>> 4 & 0xf)]);
      os.write(PEM_ARRAY[(byte4 << 2 & 0x3c) + (byte5 >>> 6 & 3)]);
      os.write(PEM_ARRAY[byte5 & 0x3f]);
    }
  }

}
