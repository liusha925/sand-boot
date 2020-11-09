/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/1    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sand.core.util.lang3.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;

/**
 * 功能说明：日期类型json序列化
 * 开发人员：@author liusha
 * 开发日期：2019/9/1 10:36
 * 功能描述：日期类型json序列化，格式yyyy-MM-dd HH:mm:ss
 */
@Slf4j
public class DateTimeJsonSerializer extends JsonSerializer<Date> {
  @Override
  public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    String dateParser;
    try {
      dateParser = DateUtil.formatDate(date, DateUtil.Format.F1_YYYY_MM_DD_HH_MM_SS);
    } catch (Exception e) {
      log.error("日期类型json序列化异常：{}", e.getMessage());
      dateParser = "--";
    }
    jsonGenerator.writeString(dateParser);
  }
}
