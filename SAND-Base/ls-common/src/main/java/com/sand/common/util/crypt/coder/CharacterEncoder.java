/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/22   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.crypt.coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 功能说明：初始化
 * 开发人员：@author liusha
 * 开发日期：2019/8/22 13:53
 * 功能描述：初始化
 */
public abstract class CharacterEncoder {

  protected PrintStream printStream;

  public CharacterEncoder() {
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

  /**
   * encodeAtom
   *
   * @param os
   * @param bytes
   * @param i
   * @param j
   * @throws IOException
   */
  protected abstract void encodeAtom(OutputStream os, byte[] bytes, int i, int j) throws IOException;

  protected void encodeBufferPrefix(OutputStream os) throws IOException {
    printStream = new PrintStream(os);
  }

  protected void encodeBufferSuffix(OutputStream os) throws IOException {
  }

  protected void encodeLinePrefix(OutputStream os, int i) throws IOException {
  }

  protected void encodeLineSuffix(OutputStream os) throws IOException {
  }

  protected int readFully(InputStream is, byte[] bytes) throws IOException {
    for (int i = 0; i < bytes.length; i++) {
      int j = is.read();
      if (j == -1) {
        return i;
      }
      bytes[i] = (byte) j;
    }
    return bytes.length;
  }

  public void encode(InputStream is, OutputStream os) throws IOException {
    byte[] bytes = new byte[bytesPerLine()];
    encodeBufferPrefix(os);
    do {
      int j = readFully(is, bytes);
      if (j == 0) {
        break;
      }
      encodeLinePrefix(os, j);
      for (int i = 0; i < j; i += bytesPerAtom()) {
        if (i + bytesPerAtom() <= j) {
          encodeAtom(os, bytes, i, bytesPerAtom());
        } else {
          encodeAtom(os, bytes, i, j - i);
        }
      }
      if (j < bytesPerLine()) {
        break;
      }
      encodeLineSuffix(os);
    } while (true);
    encodeBufferSuffix(os);
  }

  public void encodeBuffer(InputStream is, OutputStream os) throws IOException {
    byte[] bytes = new byte[bytesPerLine()];
    encodeBufferPrefix(os);
    int j;
    do {
      j = readFully(is, bytes);
      if (j == 0) {
        break;
      }
      encodeLinePrefix(os, j);
      for (int i = 0; i < j; i += bytesPerAtom()) {
        if (i + bytesPerAtom() <= j) {
          encodeAtom(os, bytes, i, bytesPerAtom());
        } else {
          encodeAtom(os, bytes, i, j - i);
        }
      }
      encodeLineSuffix(os);
    } while (j >= bytesPerLine());
    encodeBufferSuffix(os);
  }

  public void encode(byte[] bytes, OutputStream os) throws IOException {
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    encode(is, os);
  }

  public String encode(byte[] bytes) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    String str;
    try {
      encode(is, os);
      str = os.toString("8859_1");
    } catch (Exception exception) {
      throw new Error("CharacterEncoder::encodeBuffer internal error");
    }
    return str;
  }

  public void encodeBuffer(byte[] bytes, OutputStream os) throws IOException {
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    encodeBuffer(is, os);
  }

  public String encodeBuffer(byte[] bytes) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    try {
      encodeBuffer(is, os);
    } catch (Exception exception) {
      throw new Error("CharacterEncoder::encodeBuffer internal error");
    }
    return os.toString();
  }
}
