/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.common;

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
 * 功能说明：日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 13:43
 * 功能描述：需要DateEnum辅助使用
 */
public class DateUtil extends DateUtils {

  /**
   * 时间单位
   */
  public enum TimeUnit {
    YEAR, MONTH, DAY, HOUR, MINUTE, SECOND
  }

  /**
   * 时间换算成毫秒
   */
  @Getter
  @AllArgsConstructor
  public enum TimeMillis {
    MINUTE(60 * 1000),
    HOUR(60 * 60 * 1000),
    DAY(24 * 60 * 60 * 1000),
    WEEK(7 * 24 * 60 * 60 * 1000);
    private final long millis;
  }

  /**
   * 得到当前时间字符串 格式（yyyy-MM-dd）
   *
   * @return the date
   */
  public static String getNow() {
    return getNow(DateEnum.F1_YYYY_MM_DD.getPattern());
  }

  /**
   * 得到当前时间字符串 格式取自 DateEnum
   *
   * @param pattern the pattern from DateEnum
   * @return the date
   */
  public static String getNow(String pattern) {
    return DateFormatUtils.format(new Date(), getPattern(pattern));
  }

  /**
   * 得到日期字符串 格式（yyyy-MM-dd)
   *
   * @param date the date
   * @return the string
   */
  public static String formatDate(Date date) {
    return formatDate(date, DateEnum.F1_YYYY_MM_DD.getPattern());
  }

  /**
   * 得到日期字符串 格式取自 DateEnum
   *
   * @param date    the date
   * @param pattern the pattern
   * @return the string
   */
  public static String formatDate(Date date, String pattern) {
    return DateFormatUtils.format(date, getPattern(pattern));
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
   * 日期格式必须取自 DateEnum
   *
   * @param pattern
   * @return
   */
  public static String getPattern(String pattern) {
    DateEnum dateEnum = DateEnum.getDateEnum(pattern);
    if (Objects.isNull(dateEnum)) {
      throw new IllegalArgumentException("pattern必须从DateEnum类中获取.");
    }
    return dateEnum.getPattern();
  }

  /**
   * 获取过去的天数
   *
   * @param date 对比日期
   * @return long past days
   */
  public static long getPastDays(Date date) {
    return getPastTimes(date, TimeMillis.DAY.millis);
  }

  /**
   * 获取过去的时间
   *
   * @param date   对比日期
   * @param millis 时间单位
   * @return long past times
   */
  public static long getPastTimes(Date date, long millis) {
    return (System.currentTimeMillis() - date.getTime()) / millis;
  }

  /**
   * 转换为时间（天,时:分:秒.毫秒）
   *
   * @param millis 毫秒数
   * @return 天, 时:分:秒.毫秒
   */
  public static String formatMillis(long millis) {
    long day = millis / TimeMillis.DAY.millis;
    long hour = millis / TimeMillis.HOUR.millis - day * 24;
    long minute = millis / TimeMillis.MINUTE.millis - day * 24 * 60 - hour * 60;
    long second = millis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60;
    long millisecond = millis - day * TimeMillis.DAY.millis - hour * TimeMillis.HOUR.millis - minute * TimeMillis.MINUTE.millis - second * 1000;
    return (day > 0 ? day + "" : "") + hour + ":" + minute + ":" + second + "." + millisecond;
  }

  /**
   * 计算时间前/后
   *
   * @param date 操作时间
   * @param num  加/减？时间单位
   * @param unit 时间单位
   * @return
   */
  public static Date dateAdd(Date date, int num, TimeUnit unit) {
    Calendar now = Calendar.getInstance();
    now.setTime(date);
    switch (unit) {
      case YEAR:
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) + num);
        break;
      case MONTH:
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + num);
        break;
      case DAY:
        now.set(Calendar.DATE, now.get(Calendar.DATE) + num);
        break;
      case HOUR:
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) + num);
        break;
      case MINUTE:
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + num);
        break;
      case SECOND:
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) + num);
        break;
      default:
        throw new IllegalArgumentException("时间单位不正确！");
    }
    return now.getTime();
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
    return getTimestamp(new Date(), DateEnum.F1_YYYY_MM_DD.getPattern());
  }

  /**
   * 获取时间戳
   *
   * @param date    需要转换的日期
   * @param pattern 格式取自 DateEnum
   * @return
   */
  public static long getTimestamp(Date date, String pattern) {
    SimpleDateFormat format = new SimpleDateFormat(getPattern(pattern));
    long time = format.parse(formatDate(date, pattern), new ParsePosition(0)).getTime() / 1000;
    return time;
  }

  /**
   * 计算两个日期之间相差的天数
   *
   * @param startDate
   * @param endDate
   * @return
   */
  public static long daysBetween(Date startDate, Date endDate) {
    return timesBetween(startDate, endDate, DateEnum.F1_YYYY_MM_DD.getPattern());
  }

  /**
   * 计算两个日期之间相差？时间单位
   *
   * @param startDate
   * @param endDate
   * @return
   */
  public static long timesBetween(Date startDate, Date endDate, String pattern) {
    DateFormat sdf = new SimpleDateFormat(getPattern(pattern));
    Calendar cal = Calendar.getInstance();
    try {
      Date start = sdf.parse(formatDate(startDate, pattern));
      Date end = sdf.parse(formatDate(endDate, pattern));
      cal.setTime(start);
      long time1 = cal.getTimeInMillis();
      cal.setTime(end);
      long time2 = cal.getTimeInMillis();
      return (time2 - time1) / 86400000L;
    } catch (ParseException e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 校验日期格式
   *
   * @param dateStr
   * @param pattern 日期格式必须取自 DateEnum
   * @return
   */
  public static boolean isValidDate(String dateStr, String pattern) {
    boolean convertSuccess = true;
    SimpleDateFormat format = new SimpleDateFormat(getPattern(pattern));
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
   * @param pattern 今日判断(yyyy-MM-dd),本月判断(yyyy-MM)，本周（E）
   * @return
   */
  public static boolean isThisTime(Date date, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(getPattern(pattern));
    String param = sdf.format(date);
    String now = sdf.format(parseDate(getNow()));
    if (param.equals(now)) {
      return true;
    }
    return false;
  }

  public static void main(String[] args) {
    for (DateEnum item : DateEnum.values()) {
      System.out.println(getNow(item.getPattern()));
    }
  }
}
