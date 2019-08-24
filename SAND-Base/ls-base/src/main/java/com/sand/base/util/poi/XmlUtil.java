/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/21   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.poi;

import com.sand.base.constant.Constant;
import com.sand.base.util.result.Ret;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能说明：XML报文解析、转换
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/21 20:46
 * 功能描述：XML报文解析、转换
 */
@Slf4j
public class XmlUtil {
  /**
   * 公有的是为了方便调用者设置访问权限
   */
  public static final XStream X_STREAM;

  static {
    X_STREAM = new XStream(new Xpp3Driver(new NoNameCoder()));
    // 设置默认安全防护
    XStream.setupDefaultSecurity(X_STREAM);
    // 开放所有继承Object类的访问权限
    X_STREAM.allowTypeHierarchy(Object.class);
  }

  public XmlUtil() {
  }

  /**
   * 对象转XML
   *
   * @param obj 待转换对象
   * @return
   */
  public static String objToXml(Object obj) {
    return objToXml(obj, obj.getClass().getSimpleName());
  }

  /**
   * 对象转XML
   *
   * @param obj   待转换对象
   * @param alias 对象外标签
   * @return
   */
  public static String objToXml(Object obj, String alias) {
    return objToXml(obj, alias, false);
  }

  /**
   * 对象转XML
   *
   * @param obj       待转换对象
   * @param alias     对象外标签
   * @param isEncrypt 是否加密处理
   * @return
   */
  public static String objToXml(Object obj, String alias, boolean isEncrypt) {
    X_STREAM.alias(alias, obj.getClass());
    String xml = Constant.XML_HEADER + "\n" + X_STREAM.toXML(obj);
    if (isEncrypt) {
//      xml = encrypt(xml);
    }
    return xml;
  }

  /**
   * XML转对象
   *
   * @param xml 待转换xml
   * @param obj 结果集对象
   * @return
   */
  public static Object xmlToObj(String xml, Object obj) {
    return xmlToObj(xml, obj, obj.getClass().getSimpleName());
  }

  /**
   * XML转对象
   *
   * @param xml   待转换xml
   * @param obj   结果集对象
   * @param alias 对象外标签
   * @return
   */
  public static Object xmlToObj(String xml, Object obj, String alias) {
    return xmlToObj(xml, obj, alias, false);
  }

  /**
   * XML转对象
   *
   * @param xml      待转换xml
   * @param obj      结果集对象
   * @param alias    对象外标签
   * @param isDecode 是否解密处理
   * @return
   */
  public static Object xmlToObj(String xml, Object obj, String alias, boolean isDecode) {
    X_STREAM.ignoreUnknownElements();
    X_STREAM.alias(alias, obj.getClass());
    if (isDecode) {
//      xml = decode(xml);
    }
    obj = X_STREAM.fromXML(xml, obj);
    return obj;
  }

  public static void main(String[] args) {
    Ret ret = Ret.builder().code(200).msg("成功").build();
    System.out.println(objToXml(ret));
    System.out.println(xmlToObj("<?xml version=\"1.0\" encoding = \"UTF-8\"?>\n" +
        "<Ret>\n" +
        "  <code>200</code>\n" +
        "  <msg>成功</msg>\n" +
        "</Ret>", ret));
  }
}
