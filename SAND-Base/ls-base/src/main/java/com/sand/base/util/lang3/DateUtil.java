/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.lang3;

import com.sand.base.enums.DateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 功能说明：日期工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 13:43
 * 功能描述：继承org.apache.commons.lang.time.DateUtils类，需要DateEnum辅助使用
 */
public class DateUtil extends DateUtils {

  public DateUtil() {
    super();
  }

  @Getter
  @AllArgsConstructor
  public enum TimeMillis {
    // 时间换算成毫秒
    SECOND(1000),
    MINUTE(60 * 1000),
    HOUR(60 * 60 * 1000),
    DAY(24 * 60 * 60 * 1000),
    WEEK(7 * 24 * 60 * 60 * 1000);
    private final long convert;
  }

  /**
   * 得到当前时间字符串 格式（yyyy-MM-dd）
   *
   * @return the date
   */
  public static String getNow() {
    return getNow(DateEnum.F1_YYYY_MM_DD);
  }

  /**
   * 得到当前时间字符串 格式取自 DateEnum
   *
   * @param dateEnum the pattern from DateEnum
   * @return the date
   */
  public static String getNow(DateEnum dateEnum) {
    return DateFormatUtils.format(new Date(), dateEnum.getPattern());
  }

  /**
   * 得到日期字符串 格式（yyyy-MM-dd)
   *
   * @param date the date
   * @return the string
   */
  public static String formatDate(Date date) {
    return formatDate(date, DateEnum.F1_YYYY_MM_DD);
  }

  /**
   * 得到日期字符串 格式取自 DateEnum
   *
   * @param date     the date
   * @param dateEnum the pattern from DateEnum
   * @return the string
   */
  public static String formatDate(Date date, DateEnum dateEnum) {
    return DateFormatUtils.format(date, dateEnum.getPattern());
  }

  /**
   * 日期型字符串转化为日期格式
   *
   * @param dateStr the dateStr 必须符合 DateEnum格式
   * @return the date
   */
  public static Date parseDate(String dateStr) {
    if (Objects.isNull(dateStr)) {
      return null;
    }
    try {
      return parseDate(dateStr, DateEnum.getPatterns());
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * 获取过去的天数
   *
   * @param date 对比日期
   * @return long past days
   */
  public static long getPastDays(Date date) {
    return getPastTimes(date, TimeMillis.DAY);
  }

  /**
   * 获取过去的时间
   *
   * @param date   对比日期
   * @param millis 时间单位
   * @return long past times
   */
  public static long getPastTimes(Date date, TimeMillis millis) {
    return (System.currentTimeMillis() - date.getTime()) / millis.convert;
  }

  /**
   * 获取东八区当前时间
   *
   * @return Date est 8 date
   */
  public static Date getEst8Date() {
    TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat(DateEnum.F1_YYYY_MM_DD_HH_MM_SS.getPattern());
    dateFormat.setTimeZone(tz);
    return parseDate(dateFormat.format(date));
  }

  /**
   * 获取当前时间戳
   *
   * @return
   */
  public static long getNowTimestamp() {
    return getTimestamp(new Date(), DateEnum.F1_YYYY_MM_DD_HH_MM_SS);
  }

  /**
   * 获取时间戳
   *
   * @param date     需要转换的日期
   * @param dateEnum the pattern from DateEnum
   * @return
   */
  public static long getTimestamp(Date date, DateEnum dateEnum) {
    SimpleDateFormat sdf = new SimpleDateFormat(dateEnum.getPattern());
    return sdf.parse(formatDate(date, dateEnum), new ParsePosition(0)).getTime() / 1000;
  }

  /**
   * 将时间戳转换成时间字符串
   *
   * @param seconds  需要转换的时间戳
   * @param dateEnum the pattern from DateEnum
   * @return
   */
  public static String formatTimestamp(long seconds, DateEnum dateEnum) {
    return DateFormatUtils.format(seconds * 1000, dateEnum.getPattern());
  }

  /**
   * 计算两个日期之间相差的天数
   *
   * @param startDate
   * @param endDate
   * @return
   */
  public static long daysBetween(Date startDate, Date endDate) {
    return timesBetween(startDate, endDate, TimeMillis.DAY);
  }

  /**
   * 计算两个日期之间相差？时间单位
   *
   * @param startDate 起始时间
   * @param endDate   结束时间
   * @param millis    换算单位 按四舍五入处理
   * @return
   */
  public static long timesBetween(Date startDate, Date endDate, TimeMillis millis) {
    try {
      DateFormat sdf = new SimpleDateFormat(DateEnum.F1_YYYY_MM_DD_HH_MM_SS_SSS.getPattern());
      Calendar cal = Calendar.getInstance();
      Date start = sdf.parse(formatDate(startDate, DateEnum.F1_YYYY_MM_DD_HH_MM_SS_SSS));
      Date end = sdf.parse(formatDate(endDate, DateEnum.F1_YYYY_MM_DD_HH_MM_SS_SSS));
      cal.setTime(start);
      long startTime = cal.getTimeInMillis();
      cal.setTime(end);
      long endTime = cal.getTimeInMillis();
      return (endTime - startTime) / millis.convert;
    } catch (ParseException e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 校验日期格式
   *
   * @param pattern 需要校验的日期格式
   * @return
   */
  public static boolean isValidPattern(String pattern) {
    return Objects.nonNull(DateEnum.getDateEnum(pattern));
  }

  /**
   * 校验日期字符串
   *
   * @param dateStr  需要校验的日期字符串
   * @param dateEnum the pattern from DateEnum
   * @return
   */
  public static boolean isValidDate(String dateStr, DateEnum dateEnum) {
    boolean convertSuccess = true;
    SimpleDateFormat format = new SimpleDateFormat(dateEnum.getPattern());
    try {
      format.setLenient(false);
      format.parse(dateStr);
    } catch (ParseException e) {
      convertSuccess = false;
    }
    return convertSuccess;
  }

  /**
   * 判断选择的日期是否是今日或本月或本周或...
   *
   * @param date
   * @param dateEnum 今日判断(yyyy-MM-dd),本月判断(yyyy-MM)，本周（E）
   * @return
   */
  public static boolean isThisTime(Date date, DateEnum dateEnum) {
    SimpleDateFormat sdf = new SimpleDateFormat(dateEnum.getPattern());
    String param = sdf.format(date);
    String now = sdf.format(parseDate(getNow()));
    if (param.equals(now)) {
      return true;
    }
    return false;
  }

}
