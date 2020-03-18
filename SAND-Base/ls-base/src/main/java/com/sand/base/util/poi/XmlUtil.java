/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/21   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.poi;

import com.sand.base.web.entity.ResultEntity;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import lombok.extern.slf4j.Slf4j;
//import org.dom4j.Document;
//import org.dom4j.io.SAXReader;

//import java.io.InputStream;

/**
 * 功能说明：XML报文解析、转换
 * 开发人员：@author liusha
 * 开发日期：2019/8/21 20:46
 * 功能描述：XML报文解析、转换
 */
@Slf4j
public class XmlUtil {
  /**
   * 公有的是为了方便调用者设置访问权限
   */
  public static final XStream X_STREAM;
  /**
   * XML报文头
   */
  public static final String XML_HEADER = "<?xml version=\"1.0\" encoding = \"UTF-8\"?>";

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
  public static String obj2Xml(Object obj) {
    return obj2Xml(obj, obj.getClass().getSimpleName());
  }

  /**
   * 对象转XML
   *
   * @param obj   待转换对象
   * @param alias 对象外标签
   * @return
   */
  public static String obj2Xml(Object obj, String alias) {
    return obj2Xml(obj, alias, false);
  }

  /**
   * 对象转XML
   *
   * @param obj       待转换对象
   * @param alias     对象外标签
   * @param isEncrypt 是否加密处理 TODO 加密方式待定
   * @return
   */
  public static String obj2Xml(Object obj, String alias, boolean isEncrypt) {
    X_STREAM.alias(alias, obj.getClass());
    String xml = XML_HEADER + "\n" + X_STREAM.toXML(obj);
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
  public static Object xml2Obj(String xml, Object obj) {
    return xml2Obj(xml, obj, obj.getClass().getSimpleName());
  }

  /**
   * XML转对象
   *
   * @param xml   待转换xml
   * @param obj   结果集对象
   * @param alias 对象外标签
   * @return
   */
  public static Object xml2Obj(String xml, Object obj, String alias) {
    return xml2Obj(xml, obj, alias, false);
  }

  /**
   * XML转对象
   *
   * @param xml      待转换xml
   * @param obj      结果集对象
   * @param alias    对象外标签
   * @param isDecode 是否解密处理 TODO 解密方式待定
   * @return
   */
  public static Object xml2Obj(String xml, Object obj, String alias, boolean isDecode) {
    X_STREAM.ignoreUnknownElements();
    X_STREAM.alias(alias, obj.getClass());
    if (isDecode) {
//      xml = decode(xml);
    }
    obj = X_STREAM.fromXML(xml, obj);
    return obj;
  }

//  /**
//   * TODO 架包无法引入，此方法暂时作废 从xml文件中读取信息
//   *
//   * @param xmlPath xml路径，例：/com/sand/base/xml/test.xml
//   * @return
//   */
//  public static String readXml(String xmlPath) {
//    InputStream is = XmlUtil.class.getResourceAsStream(xmlPath);
//    SAXReader reader = new SAXReader();
//    Document doc = reader.read(is);
//    return doc.asXml();
//  }

  public static void main(String[] args) {
    ResultEntity ret = ResultEntity.builder().code(200).msg("成功").build();
    System.out.println(obj2Xml(ret));
    System.out.println(xml2Obj("<?xml version=\"1.0\" encoding = \"UTF-8\"?>\n" +
        "<ResultEntity>\n" +
        "  <code>200</code>\n" +
        "  <msg>成功</msg>\n" +
        "</ResultEntity>", ret));
  }
}
