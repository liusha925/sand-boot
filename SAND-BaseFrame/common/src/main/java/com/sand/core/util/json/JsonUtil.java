/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/5/28    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.json;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 功能说明：JSON解析处理工具
 * 开发人员：@author liusha
 * 开发日期：2020/5/28 15:47
 * 功能描述：JSON解析处理
 */
@Slf4j
public class JsonUtil {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

  /**
   * <p>
   * 功能描述：判断 json 格式
   * </p>
   * 开发人员：@author gy-hsh
   * 开发时间：2021/7/20 13:51
   * 修改记录：新建
   *
   * @param content content
   * @return boolean true-不是 false-是
   */
  public boolean isJson(String content) {
    if (StringUtils.isEmpty(content)) {
      return false;
    }
    boolean isJsonObject = true;
    boolean isJsonArray = true;
    try {
      JSONObject.parseObject(content);
    } catch (Exception e) {
      isJsonObject = false;
    }
    try {
      JSONObject.parseArray(content);
    } catch (Exception e) {
      isJsonArray = false;
    }
    return isJsonObject || isJsonArray;
  }

  /**
   * 将数值以json串的形式写入文件中
   *
   * @param resultFile 待写入的文件
   * @param value      待写入的数值
   */
  public static void format(File resultFile, Object value) {
    try {
      OBJECT_WRITER.writeValue(resultFile, value);
    } catch (Exception e) {
      log.error("JSON格式化出错", e);
      throw new RuntimeException("JSON格式化出错");
    }
  }

  /**
   * 从文件中读取数据并解析成对象
   *
   * @param srcFile   待读取的文件
   * @param valueType 解析对象类型
   * @param <T>       对象类型
   * @return 解析后的数据
   */
  public static <T> T unFormat(File srcFile, Class<T> valueType) {
    try {
      return OBJECT_MAPPER.readValue(srcFile, valueType);
    } catch (Exception e) {
      log.error("JSON反格式化出错", e);
      throw new RuntimeException("JSON反格式化出错");
    }
  }

  /**
   * 将数值以json串的形式写入输出流中
   *
   * @param os    待写入的文件
   * @param value 待写入的数值
   */
  public static void format(OutputStream os, Object value) {
    try {
      OBJECT_WRITER.writeValue(os, value);
    } catch (Exception e) {
      log.error("JSON格式化出错", e);
      throw new RuntimeException("JSON格式化出错");
    }
  }

  /**
   * 从输入流中读取数据并解析成对象
   *
   * @param srcIs     待读取的输入流
   * @param valueType 解析对象类型
   * @param <T>       对象类型
   * @return 解析后的数据
   */
  public static <T> T unFormat(InputStream srcIs, Class<T> valueType) {
    try {
      return OBJECT_MAPPER.readValue(srcIs, valueType);
    } catch (Exception e) {
      log.error("JSON反格式化出错", e);
      throw new RuntimeException("JSON反格式化出错");
    }
  }

  /**
   * json格式化
   *
   * @param value 待格式化的数据
   * @return 格式化后的数据
   */
  public static String format(Object value) {
    try {
      return OBJECT_WRITER.writeValueAsString(value);
    } catch (Exception e) {
      log.error("JSON格式化出错", e);
      throw new RuntimeException("JSON格式化出错");
    }
  }

  /**
   * json反格式化
   *
   * @param jsonStr   待反格式化的json串
   * @param valueType 解析对象类型
   * @param <T>       对象类型
   * @return 解析后的数据
   */
  public static <T> T unFormat(String jsonStr, Class<T> valueType) {
    try {
      return OBJECT_MAPPER.readValue(jsonStr, valueType);
    } catch (Exception e) {
      log.error("JSON反格式化出错", e);
      throw new RuntimeException("JSON反格式化出错");
    }
  }

  /**
   * json格式化
   *
   * @param value 待格式化的数据
   * @return 格式化后的数据
   */
  public static byte[] formatBytes(Object value) {
    try {
      return OBJECT_WRITER.writeValueAsBytes(value);
    } catch (Exception e) {
      log.error("JSON格式化出错", e);
      throw new RuntimeException("JSON格式化出错");
    }
  }

  /**
   * json反格式化
   *
   * @param bytes     待反格式化的数据
   * @param valueType 解析对象类型
   * @param <T>       对象类型
   * @return 解析后的数据
   * @throws Exception 异常
   */
  public static <T> T unFormat(byte[] bytes, Class<T> valueType) throws Exception {
    try {
      if (bytes == null) {
        bytes = new byte[0];
      }
      return OBJECT_MAPPER.readValue(bytes, 0, bytes.length, valueType);
    } catch (Exception e) {
      log.error("JSON反格式化出错", e);
      throw new RuntimeException("JSON反格式化出错");
    }
  }

}
