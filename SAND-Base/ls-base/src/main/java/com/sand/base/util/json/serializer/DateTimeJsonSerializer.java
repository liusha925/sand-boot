/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/1    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sand.base.enums.DateEnum;
import com.sand.base.util.lang3.DateUtil;

import java.io.IOException;
import java.util.Date;

/**
 * 功能说明：日期类型json格式化
 * 开发人员：@author liusha
 * 开发日期：2019/9/1 10:36
 * 功能描述：日期类型json格式化，格式yyyy-MM-dd HH:mm:ss
 */
public class DateTimeJsonSerializer extends JsonSerializer<Date> {
  @Override
  public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    String dateParser;
    try {
      dateParser = DateUtil.formatDate(date, DateEnum.F1_YYYY_MM_DD_HH_MM_SS);
    } catch (Exception e) {
      dateParser = "--";
    }
    jsonGenerator.writeString(dateParser);
  }

}
