/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/22   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.crypt.coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 功能说明：初始化
 * 开发人员：@author liusha
 * 开发日期：2019/8/22 13:10
 * 功能描述：初始化
 */
public abstract class CharacterDecoder {

  public static final String EXCEPTION_MSG = "StreamExhausted";

  public CharacterDecoder() {
  }

  /**
   * bytesPerAtom
   *
   * @return
   */
  protected abstract int bytesPerAtom();

  /**
   * bytesPerLine
   *
   * @return
   */
  protected abstract int bytesPerLine();

  protected void decodeBufferPrefix(InputStream is, OutputStream os) throws IOException {
  }

  protected void decodeBufferSuffix(InputStream is, OutputStream os) throws IOException {
  }

  protected int decodeLinePrefix(InputStream is, OutputStream os) throws IOException {
    return bytesPerLine();
  }

  protected void decodeLineSuffix(InputStream is, OutputStream os) throws IOException {
  }

  protected void decodeAtom(InputStream is, OutputStream os, int i) throws IOException {
    throw new IOException(EXCEPTION_MSG);
  }

  protected int readFully(InputStream is, byte[] bytes, int i, int j) throws IOException {
    for (int k = 0; k < j; k++) {
      int line = is.read();
      if (line == -1) {
        return k != 0 ? k : -1;
      }
      bytes[k + i] = (byte) line;
    }
    return j;
  }

  public void decodeBuffer(InputStream is, OutputStream os) throws IOException {
    int j = 0;
    decodeBufferPrefix(is, os);
    try {
      do {
        int k = decodeLinePrefix(is, os);
        int i;
        for (i = 0; i + bytesPerAtom() < k; i += bytesPerAtom()) {
          decodeAtom(is, os, bytesPerAtom());
          j += bytesPerAtom();
        }
        if (i + bytesPerAtom() == k) {
          decodeAtom(is, os, bytesPerAtom());
          j += bytesPerAtom();
        } else {
          decodeAtom(is, os, k - i);
          j += k - i;
        }
        decodeLineSuffix(is, os);
      } while (true);
    } catch (IOException e) {
      if (e.getMessage().equals(EXCEPTION_MSG)) {
        decodeBufferSuffix(is, os);
      } else {
        throw e;
      }
    }
  }

}
