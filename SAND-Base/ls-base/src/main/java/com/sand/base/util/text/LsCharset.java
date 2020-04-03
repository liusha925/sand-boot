/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/10/9    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.text;

import com.sand.base.util.lang3.StringUtil;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 功能说明：字符集
 * 开发人员：@author liusha
 * 开发日期：2019/10/9 8:48
 * 功能描述：字符集转换,基础自java.nio.charset.Charset
 */
public class LsCharset extends Charset {
  /**
   * GBK
   */
  public static final String GBK = "GBK";
  /**
   * UTF-8
   */
  public static final String UTF_8 = "UTF-8";
  /**
   * ISO-8859-1
   */
  public static final String ISO_8859_1 = "ISO-8859-1";
  /**
   * GBK
   */
  public static final Charset CHARSET_GBK = Charset.forName(GBK);
  /**
   * UTF-8
   */
  public static final Charset CHARSET_UTF_8 = Charset.forName(UTF_8);
  /**
   * ISO-8859-1
   */
  public static final Charset CHARSET_ISO_8859_1 = Charset.forName(ISO_8859_1);

  /**
   * Initializes a new charset with the given canonical name and alias
   * set.
   *
   * @param canonicalName The canonical name of this charset
   * @param aliases       An array of this charset's aliases, or null if it has no aliases
   * @throws IllegalCharsetNameException If the canonical name or any of the aliases are illegal
   */
  protected LsCharset(String canonicalName, String[] aliases) {
    super(canonicalName, aliases);
  }

  /**
   * 转换为Charset对象
   *
   * @param charset 字符集，为空则返回默认字符集
   * @return LsCharset
   */
  public static Charset str2Charset(String charset) {
    return StringUtil.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset);
  }

  /**
   * 转换字符串的字符集编码
   *
   * @param source      字符串
   * @param srcCharset  源字符集，默认ISO-8859-1
   * @param destCharset 目标字符集，默认UTF-8
   * @return 转换后的字符集
   */
  public static String convert(String source, String srcCharset, String destCharset) {
    return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
  }

  /**
   * 转换字符串的字符集编码
   *
   * @param source      字符串
   * @param srcCharset  源字符集，默认ISO-8859-1
   * @param destCharset 目标字符集，默认UTF-8
   * @return 转换后的字符集
   */
  public static String convert(String source, Charset srcCharset, Charset destCharset) {
    if (null == srcCharset) {
      srcCharset = StandardCharsets.ISO_8859_1;
    }
    if (null == destCharset) {
      srcCharset = StandardCharsets.UTF_8;
    }
    if (StringUtil.isEmpty(source) || srcCharset.equals(destCharset)) {
      return source;
    }
    return new String(source.getBytes(srcCharset), destCharset);
  }

  /**
   * @return 系统字符集编码
   */
  public static String systemCharset() {
    return defaultCharset().name();
  }

  @Override
  public boolean contains(Charset cs) {
    return false;
  }

  @Override
  public CharsetDecoder newDecoder() {
    return null;
  }

  @Override
  public CharsetEncoder newEncoder() {
    return null;
  }
}
