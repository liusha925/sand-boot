/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.lang3;

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
 * 功能描述：继承org.apache.commons.lang3.time.DateUtils类
 */
public class DateUtil extends DateUtils {

  public DateUtil() {
    super();
  }

  /**
   * 功能说明：日期格式枚举类
   * 开发人员：@author liusha
   * 开发日期：2019/8/16 14:15
   * 功能描述：日期格式枚举类
   */
  @Getter
  @AllArgsConstructor
  public enum Format {
    // General Format
    F_YYYY("yyyy"),
    F_MM("MM"),
    F_DD("dd"),
    // E表示星期
    F_E("E"),
    F_HH_MM_SS("HH:mm:ss"),
    // Date Format One,
    F1_MM_DD("MM-dd"),
    F1_YYYY_MM("yyyy-MM"),
    F1_YYYY_MM_DD("yyyy-MM-dd"),
    F1_YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
    F1_YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    F1_YYYY_MM_DD_HH_MM_SS_SSS("yyyy-MM-dd HH:mm:ss.SSS"),
    // Date Format Two
    F2_MM_DD("MMdd"),
    F2_YYYY_MM("yyyyMM"),
    F2_YYYY_MM_DD("yyyyMMdd"),
    F2_YYYY_MM_DD_HH_MM("yyyyMMdd HH:mm"),
    F2_YYYY_MM_DD_HH_MM_SS("yyyyMMdd HH:mm:ss"),
    F2_YYYY_MM_DD_HH_MM_SS_SSS("yyyyMMdd HH:mm:ss.SSS"),
    // Date Format Three
    F3_MM_DD("MM.dd"),
    F3_YYYY_MM("yyyy.MM"),
    F3_YYYY_MM_DD("yyyy.MM.dd"),
    F3_YYYY_MM_DD_HH_MM("yyyy.MM.dd HH:mm"),
    F3_YYYY_MM_DD_HH_MM_SS("yyyy.MM.dd HH:mm:ss"),
    F3_YYYY_MM_DD_HH_MM_SS_SSS("yyyy.MM.dd HH:mm:ss.SSS"),
    // Date Format Four
    F4_MM_DD("MM/dd"),
    F4_YYYY_MM("yyyy/MM"),
    F4_YYYY_MM_DD("yyyy/MM/dd"),
    F4_YYYY_MM_DD_HH_MM("yyyy/MM/dd HH:mm"),
    F4_YYYY_MM_DD_HH_MM_SS("yyyy/MM/dd HH:mm:ss"),
    F4_YYYY_MM_DD_HH_MM_SS_SSS("yyyy/MM/dd HH:mm:ss.SSS"),
    // Date Format Five
    F5_YYYY_MM_DD_HH_MM_SS_SSS("yyyyMMddHHmmssSSS"),
    // Date Format Six
    F6_MM_DD("MM月dd日"),
    F6_YYYY_MM("yyyy年MM月"),
    F6_YYYY_MM_DD("yyyy年MM月dd日"),
    ;
    private final String pattern;

    public static String[] getPatterns() {
      String[] patterns = new String[Format.values().length];
      for (int i = 0, len = Format.values().length; i < len; i++) {
        patterns[i] = Format.values()[i].pattern;
      }
      return patterns;
    }

    public static Format getFormat(String pattern) {
      for (Format item : Format.values()) {
        if (pattern.equals(item.pattern)) {
          return item;
        }
      }
      return null;
    }

  }

  /**
   * 时间单位换算成毫秒
   */
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
   * <pre>
   *   System.out.println(DateUtil.getNow()); = "2020-04-15"
   * </pre>
   *
   * @return the date
   */
  public static String getNow() {
    return getNow(Format.F1_YYYY_MM_DD);
  }

  /**
   * 得到当前时间字符串 格式取自 Format
   * <pre>
   *   System.out.println(DateUtil.getNow(Format.F_YYYY)); = "2020"
   *   System.out.println(DateUtil.getNow(Format.F_MM)); = "04"
   *   System.out.println(DateUtil.getNow(Format.F_DD)); = "15"
   *   System.out.println(DateUtil.getNow(Format.F_E)); = "星期三"
   *   System.out.println(DateUtil.getNow(Format.F_HH_MM_SS)); = "10:31:38"
   *   System.out.println(DateUtil.getNow(Format.F1_MM_DD)); = "04-15"
   *   System.out.println(DateUtil.getNow(Format.F1_YYYY_MM)); = "2020-04"
   *   System.out.println(DateUtil.getNow(Format.F1_YYYY_MM_DD)); = "2020-04-15"
   *   System.out.println(DateUtil.getNow(Format.F1_YYYY_MM_DD_HH_MM)); = "2020-04-15 10:31"
   *   System.out.println(DateUtil.getNow(Format.F1_YYYY_MM_DD_HH_MM_SS)); = "2020-04-15 10:31:38"
   *   System.out.println(DateUtil.getNow(Format.F1_YYYY_MM_DD_HH_MM_SS_SSS)); = "2020-04-15 10:31:38.181"
   *   System.out.println(DateUtil.getNow(Format.F2_MM_DD)); = "0415"
   *   System.out.println(DateUtil.getNow(Format.F2_YYYY_MM)); = "202004"
   *   System.out.println(DateUtil.getNow(Format.F2_YYYY_MM_DD)); = "20200415"
   *   System.out.println(DateUtil.getNow(Format.F2_YYYY_MM_DD_HH_MM)); = "20200415 10:31"
   *   System.out.println(DateUtil.getNow(Format.F2_YYYY_MM_DD_HH_MM_SS)); = "20200415 10:31:38"
   *   System.out.println(DateUtil.getNow(Format.F2_YYYY_MM_DD_HH_MM_SS_SSS)); = "20200415 10:31:38.184"
   *   System.out.println(DateUtil.getNow(Format.F3_MM_DD)); = "04.15"
   *   System.out.println(DateUtil.getNow(Format.F3_YYYY_MM)); = "2020.04"
   *   System.out.println(DateUtil.getNow(Format.F3_YYYY_MM_DD)); = "2020.04.15"
   *   System.out.println(DateUtil.getNow(Format.F3_YYYY_MM_DD_HH_MM)); = "2020.04.15 10:31"
   *   System.out.println(DateUtil.getNow(Format.F3_YYYY_MM_DD_HH_MM_SS)); = "2020.04.15 10:31:38"
   *   System.out.println(DateUtil.getNow(Format.F3_YYYY_MM_DD_HH_MM_SS_SSS)); = "2020.04.15 10:31:38.186"
   *   System.out.println(DateUtil.getNow(Format.F4_MM_DD)); = "04/15"
   *   System.out.println(DateUtil.getNow(Format.F4_YYYY_MM)); = "2020/04"
   *   System.out.println(DateUtil.getNow(Format.F4_YYYY_MM_DD)); = "2020/04/15"
   *   System.out.println(DateUtil.getNow(Format.F4_YYYY_MM_DD_HH_MM)); = "2020/04/15 10:31"
   *   System.out.println(DateUtil.getNow(Format.F4_YYYY_MM_DD_HH_MM_SS)); = "2020/04/15 10:31:38"
   *   System.out.println(DateUtil.getNow(Format.F4_YYYY_MM_DD_HH_MM_SS_SSS)); = "2020/04/15 10:31:38.190"
   *   System.out.println(DateUtil.getNow(Format.F5_YYYY_MM_DD_HH_MM_SS_SSS)); = "20200415103138190"
   *   System.out.println(DateUtil.getNow(Format.F6_MM_DD)); = "04月15日"
   *   System.out.println(DateUtil.getNow(Format.F6_YYYY_MM)); = "2020年04月"
   *   System.out.println(DateUtil.getNow(Format.F6_YYYY_MM_DD)); = "2020年04月15日"
   * </pre>
   *
   * @param format the pattern from Format
   * @return the date
   */
  public static String getNow(Format format) {
    return DateFormatUtils.format(new Date(), format.getPattern());
  }

  /**
   * 得到日期字符串 格式（yyyy-MM-dd)
   * <pre>
   *   System.out.println(DateUtil.formatDate()); = "2020-04-15"
   * </pre>
   *
   * @param date the date
   * @return the string
   */
  public static String formatDate(Date date) {
    return formatDate(date, Format.F1_YYYY_MM_DD);
  }

  /**
   * 得到日期字符串 格式取自 Format
   * <pre>
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F_YYYY)); = "2020"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F_MM)); = "04"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F_DD)); = "15"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F_E)); = "星期三"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F_HH_MM_SS)); = "10:40:09"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F1_MM_DD)); = "04-15"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F1_YYYY_MM)); = "2020-04"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F1_YYYY_MM_DD)); = "2020-04-15"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F1_YYYY_MM_DD_HH_MM)); = "2020-04-15 10:40"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F1_YYYY_MM_DD_HH_MM_SS)); = "2020-04-15 10:40:09"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F1_YYYY_MM_DD_HH_MM_SS_SSS)); = "2020-04-15 10:40:09.686"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F2_MM_DD)); = "0415"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F2_YYYY_MM)); = "202004"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F2_YYYY_MM_DD)); = "20200415"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F2_YYYY_MM_DD_HH_MM)); = "20200415 10:40"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F2_YYYY_MM_DD_HH_MM_SS)); = "20200415 10:40:09"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F2_YYYY_MM_DD_HH_MM_SS_SSS)); = "20200415 10:40:09.688"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F3_MM_DD)); = "04.15"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F3_YYYY_MM)); = "2020.04"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F3_YYYY_MM_DD)); = "2020.04.15"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F3_YYYY_MM_DD_HH_MM)); = "2020.04.15 10:40"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F3_YYYY_MM_DD_HH_MM_SS)); = "2020.04.15 10:40:09"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F3_YYYY_MM_DD_HH_MM_SS_SSS)); = "2020.04.15 10:40:09.691"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F4_MM_DD)); = "04/15"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F4_YYYY_MM)); = "2020/04"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F4_YYYY_MM_DD)); = "2020/04/15"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F4_YYYY_MM_DD_HH_MM)); = "2020/04/15 10:40"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F4_YYYY_MM_DD_HH_MM_SS)); = "2020/04/15 10:40:09"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F4_YYYY_MM_DD_HH_MM_SS_SSS)); = "2020/04/15 10:40:09.697"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F5_YYYY_MM_DD_HH_MM_SS_SSS)); = "20200415104009698"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F6_MM_DD)); = "04月15日"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F6_YYYY_MM)); = "2020年04月"
   *   System.out.println(DateUtil.formatDate(new Date(), Format.F6_YYYY_MM_DD)); = "2020年04月15日"
   * </pre>
   *
   * @param date   the date
   * @param format the pattern from Format
   * @return the string
   */
  public static String formatDate(Date date, Format format) {
    return DateFormatUtils.format(date, format.getPattern());
  }

  /**
   * 得到日期字符串 格式（yyyy-MM-dd)
   * <pre>
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy")); = "2020"
   *   System.out.println(DateUtil.formatDate(new Date(), "MM")); = "04"
   *   System.out.println(DateUtil.formatDate(new Date(), "dd")); = "15"
   *   System.out.println(DateUtil.formatDate(new Date(), "E")); = "星期三"
   *   System.out.println(DateUtil.formatDate(new Date(), "HH:mm:ss")); = "11:09:50"
   *   System.out.println(DateUtil.formatDate(new Date(), "MM-dd")); = "04-15"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM")); = "2020-04"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd")); = "2020-04-15"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm")); = "2020-04-15 11:09"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")); = "2020-04-15 11:09:50"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")); = "2020-04-15 11:09:50.661"
   *   System.out.println(DateUtil.formatDate(new Date(), "MMdd")); = "0415"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyyMM")); = "202004"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyyMMdd")); = "20200415"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyyMMdd HH:mm")); = "20200415 11:09"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyyMMdd HH:mm:ss")); = "20200415 11:09:50"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyyMMdd HH:mm:ss.SSS")); = "20200415 11:09:50.663"
   *   System.out.println(DateUtil.formatDate(new Date(), "MM.dd")); = "04.15"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy.MM")); = "2020.04"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy.MM.dd")); = "2020.04.15"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy.MM.dd HH:mm")); = "2020.04.15 11:09"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy.MM.dd HH:mm:ss")); = "2020.04.15 11:09:50"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy.MM.dd HH:mm:ss.SSS")); = "2020.04.15 11:09:50.666"
   *   System.out.println(DateUtil.formatDate(new Date(), "MM/dd")); = "04/15"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy/MM")); = "2020/04"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy/MM/dd")); = "2020/04/15"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy/MM/dd HH:mm")); = "2020/04/15 11:09"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy/MM/dd HH:mm:ss")); = "2020/04/15 11:09:50"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy/MM/dd HH:mm:ss.SSS")); = "2020/04/15 11:09:50.668"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyyMMddHHmmssSSS")); = "20200415110950670"
   *   System.out.println(DateUtil.formatDate(new Date(), "MM月dd日")); = "04月15日"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy年MM月")); = "2020年04月"
   *   System.out.println(DateUtil.formatDate(new Date(), "yyyy年MM月dd日")); = "2020年04月15日"
   * </pre>
   *
   * @param date    the date
   * @param pattern 自定义格式
   * @return the string
   */
  public static String formatDate(Date date, String pattern) {
    return DateFormatUtils.format(date, pattern);
  }

  /**
   * 日期型字符串转化为日期格式
   * <pre>
   *   System.out.println(DateUtil.parseDate("2020")); = "Wed Jan 01 00:00:00 CST 2020"
   *   System.out.println(DateUtil.parseDate("04")); = "Tue Jan 01 00:00:00 CST 4"
   *   System.out.println(DateUtil.parseDate("15")); = "Tue Jan 01 00:00:00 CST 15"
   *   System.out.println(DateUtil.parseDate("星期三")); = "Wed Jan 07 00:00:00 CST 1970"
   *   System.out.println(DateUtil.parseDate("10:40:09")); = "Thu Jan 01 10:40:09 CST 1970"
   *   System.out.println(DateUtil.parseDate("04-15")); = "Wed Apr 15 00:00:00 CST 1970"
   *   System.out.println(DateUtil.parseDate("2020-04")); = "Fri Apr 04 00:00:00 CST 2138"
   *   System.out.println(DateUtil.parseDate("2020-04-15")); = "Wed Apr 15 00:00:00 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020-04-15 10:40")); = "Wed Apr 15 10:40:00 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020-04-15 10:40:09")); = "Wed Apr 15 10:40:09 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020-04-15 10:40:09.686")); = "Wed Apr 15 10:40:09 CST 2020"
   *   System.out.println(DateUtil.parseDate("0415")); = "Fri Jan 01 00:00:00 CST 415"
   *   System.out.println(DateUtil.parseDate("202004")); = "Thu Jan 01 00:00:00 CST 202004"
   *   System.out.println(DateUtil.parseDate("20200415")); = "Thu Jan 01 00:00:00 CST 20200415"
   *   System.out.println(DateUtil.parseDate("20200415 10:40")); = "Wed Apr 15 10:40:00 CST 2020"
   *   System.out.println(DateUtil.parseDate("20200415 10:40:09")); = "Wed Apr 15 10:40:09 CST 2020"
   *   System.out.println(DateUtil.parseDate("20200415 10:40:09.688")); = "Wed Apr 15 10:40:09 CST 2020"
   *   System.out.println(DateUtil.parseDate("04.15")); = "Wed Apr 15 00:00:00 CST 1970"
   *   System.out.println(DateUtil.parseDate("2020.04")); = "Fri Apr 04 00:00:00 CST 2138"
   *   System.out.println(DateUtil.parseDate("2020.04.15")); = "Wed Apr 15 00:00:00 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020.04.15 10:40")); = "Wed Apr 15 10:40:00 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020.04.15 10:40:09")); = "Wed Apr 15 10:40:09 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020.04.15 10:40:09.691")); = "Wed Apr 15 10:40:09 CST 2020"
   *   System.out.println(DateUtil.parseDate("04/15")); = "Wed Apr 15 00:00:00 CST 1970"
   *   System.out.println(DateUtil.parseDate("2020/04")); = "Fri Apr 04 00:00:00 CST 2138"
   *   System.out.println(DateUtil.parseDate("2020/04/15")); = "Wed Apr 15 00:00:00 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020/04/15 10:40")); = "Wed Apr 15 10:40:00 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020/04/15 10:40:09")); = "Wed Apr 15 10:40:09 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020/04/15 10:40:09.697")); = "Wed Apr 15 10:40:09 CST 2020"
   *   System.out.println(DateUtil.parseDate("20200415104009698")); = "Wed Apr 15 10:40:09 CST 2020"
   *   System.out.println(DateUtil.parseDate("04月15日")); = "Wed Apr 15 00:00:00 CST 1970"
   *   System.out.println(DateUtil.parseDate("2020年04月")); = "Wed Apr 01 00:00:00 CST 2020"
   *   System.out.println(DateUtil.parseDate("2020年04月15日")); = "Wed Apr 15 00:00:00 CST 2020"
   * </pre>
   *
   * @param dateStr the dateStr 必须符合 DateUtil.Format 格式，否则返回null
   * @return the date
   */
  public static Date parseDate(String dateStr) {
    if (Objects.isNull(dateStr)) {
      return null;
    }
    try {
      return parseDate(dateStr, Format.getPatterns());
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * 获取过去的天数
   * <pre>
   *   System.out.println(DateUtil.getPastDays(new Date())); = 0
   * </pre>
   *
   * @param date 对比日期
   * @return long past days
   */
  public static long getPastDays(Date date) {
    return getPastTimes(date, TimeMillis.DAY);
  }

  /**
   * 获取过去的时间
   * <pre>
   *   System.out.println(DateUtil.getPastTimes(DateUtil.parseDate("2020/04/14 10:40:09.697"), TimeMillis.SECOND)); = 96928
   *   System.out.println(DateUtil.getPastTimes(DateUtil.parseDate("2020/04/14 10:40:09.697"), TimeMillis.MINUTE)); = 1615
   *   System.out.println(DateUtil.getPastTimes(DateUtil.parseDate("2020/04/14 10:40:09.697"), TimeMillis.HOUR)); = 26
   *   System.out.println(DateUtil.getPastTimes(DateUtil.parseDate("2020/04/14 10:40:09.697"), TimeMillis.DAY)); = 1
   *   System.out.println(DateUtil.getPastTimes(DateUtil.parseDate("2020/04/14 10:40:09.697"), TimeMillis.WEEK)); = 0
   * </pre>
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
   * <pre>
   *   System.out.println(DateUtil.getEst8Date()); = Wed Apr 15 13:39:40 CST 2020
   * </pre>
   *
   * @return Date est 8 date
   */
  public static Date getEst8Date() {
    TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat(Format.F1_YYYY_MM_DD_HH_MM_SS.getPattern());
    dateFormat.setTimeZone(tz);
    return parseDate(dateFormat.format(date));
  }

  /**
   * 获取当前时间戳
   * <pre>
   *   System.out.println(DateUtil.getNowTimestamp()); = 1586929257
   * </pre>
   *
   * @return
   */
  public static long getNowTimestamp() {
    return getTimestamp(new Date(), Format.F1_YYYY_MM_DD_HH_MM_SS);
  }

  /**
   * 获取时间戳
   * <pre>
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F_YYYY)); = 1577808000
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F_MM)); = 7747200
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F_DD)); = 1180800
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F_E)); = 489600
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F_HH_MM_SS)); = 20609
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F1_MM_DD)); = 8956800
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F1_YYYY_MM)); = 1585670400
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F1_YYYY_MM_DD)); = 1586880000
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F1_YYYY_MM_DD_HH_MM)); = 1586929380
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F1_YYYY_MM_DD_HH_MM_SS)); = 1586929409
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F1_YYYY_MM_DD_HH_MM_SS_SSS)); = 1586929409
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F2_MM_DD)); = 8956800
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F2_YYYY_MM)); = 1585670400
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F2_YYYY_MM_DD)); = 1586880000
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F2_YYYY_MM_DD_HH_MM)); = 1586929380
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F2_YYYY_MM_DD_HH_MM_SS)); = 1586929409
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F2_YYYY_MM_DD_HH_MM_SS_SSS)); = 1586929409
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F3_MM_DD)); = 8956800
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F3_YYYY_MM)); = 1585670400
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F3_YYYY_MM_DD)); = 1586880000
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F3_YYYY_MM_DD_HH_MM)); = 1586929380
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F3_YYYY_MM_DD_HH_MM_SS)); = 1586929409
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F3_YYYY_MM_DD_HH_MM_SS_SSS)); = 1586929409
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F4_MM_DD)); = 8956800
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F4_YYYY_MM)); = 1585670400
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F4_YYYY_MM_DD)); = 1586880000
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F4_YYYY_MM_DD_HH_MM)); = 1586929380
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F4_YYYY_MM_DD_HH_MM_SS)); = 1586929409
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F4_YYYY_MM_DD_HH_MM_SS_SSS)); = 1586929409
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F5_YYYY_MM_DD_HH_MM_SS_SSS)); = 1586929409
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F6_MM_DD)); = 8956800
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F6_YYYY_MM)); = 1585670400
   *   System.out.println(DateUtil.getTimestamp(new Date(), Format.F6_YYYY_MM_DD)); = 1586880000
   * </pre>
   *
   * @param date   需要转换的日期
   * @param format the pattern from Format
   * @return
   */
  public static long getTimestamp(Date date, Format format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format.getPattern());
    return sdf.parse(formatDate(date, format), new ParsePosition(0)).getTime() / 1000;
  }

  /**
   * 将时间戳转换成时间字符串
   * <pre>
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F_YYYY)); = "2020"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F_MM)); = "04"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F_DD)); = "15"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F_E)); = "星期三"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F_HH_MM_SS)); = "13:43:29"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F1_MM_DD)); = "04-15"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F1_YYYY_MM)); = "2020-04"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F1_YYYY_MM_DD)); = "2020-04-15"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F1_YYYY_MM_DD_HH_MM)); = "2020-04-15 13:43"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F1_YYYY_MM_DD_HH_MM_SS)); = "2020-04-15 13:43:29"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F1_YYYY_MM_DD_HH_MM_SS_SSS)); = "2020-04-15 13:43:29.000"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F2_MM_DD)); = "0415"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F2_YYYY_MM)); = "202004"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F2_YYYY_MM_DD)); = "20200415"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F2_YYYY_MM_DD_HH_MM)); = "20200415 13:43"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F2_YYYY_MM_DD_HH_MM_SS)); = "20200415 13:43:29"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F2_YYYY_MM_DD_HH_MM_SS_SSS)); = "20200415 13:43:29.000"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F3_MM_DD)); = "04.15"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F3_YYYY_MM)); = "2020.04"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F3_YYYY_MM_DD)); = "2020.04.15"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F3_YYYY_MM_DD_HH_MM)); = "2020.04.15 13:43"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F3_YYYY_MM_DD_HH_MM_SS)); = "2020.04.15 13:43:29"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F3_YYYY_MM_DD_HH_MM_SS_SSS)); = "2020.04.15 13:43:29.000"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F4_MM_DD)); = "04/15"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F4_YYYY_MM)); = "2020/04"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F4_YYYY_MM_DD)); = "2020/04/15"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F4_YYYY_MM_DD_HH_MM)); = "2020/04/15 13:43"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F4_YYYY_MM_DD_HH_MM_SS)); = "2020/04/15 13:43:29"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F4_YYYY_MM_DD_HH_MM_SS_SSS)); = "2020/04/15 13:43:29.000"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F5_YYYY_MM_DD_HH_MM_SS_SSS)); = "20200415134329000"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F6_MM_DD)); = "04月15日"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F6_YYYY_MM)); = "2020年04月"
   *   System.out.println(DateUtil.formatTimestamp(1586929409, Format.F6_YYYY_MM_DD)); = "2020年04月15日"
   * </pre>
   *
   * @param seconds 需要转换的时间戳
   * @param format  the pattern from Format
   * @return
   */
  public static String formatTimestamp(long seconds, Format format) {
    return DateFormatUtils.format(seconds * 1000, format.getPattern());
  }

  /**
   * 计算两个日期之间相差的天数
   * <pre>
   *   Date startDate = DateUtil.parseDate("2020-04-01 10:40:09.686");
   *   Date endDate = DateUtil.parseDate("2020-04-15 10:40:09.686");
   *   System.out.println(DateUtil.daysBetween(startDate, endDate)); = 14
   * </pre>
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
   * <pre>
   *   Date startDate = DateUtil.parseDate("2020-04-01 10:40:09.686");
   *   Date endDate = DateUtil.parseDate("2020-04-15 10:40:09.686");
   *   System.out.println(DateUtil.timesBetween(startDate, endDate, TimeMillis.SECOND)); = 1209600
   *   System.out.println(DateUtil.timesBetween(startDate, endDate, TimeMillis.MINUTE)); = 20160
   *   System.out.println(DateUtil.timesBetween(startDate, endDate, TimeMillis.HOUR)); = 336
   *   System.out.println(DateUtil.timesBetween(startDate, endDate, TimeMillis.DAY)); = 14
   *   System.out.println(DateUtil.timesBetween(startDate, endDate, TimeMillis.WEEK)); = 2
   * </pre>
   *
   * @param startDate 起始时间
   * @param endDate   结束时间
   * @param millis    换算单位 按四舍五入处理
   * @return
   */
  public static long timesBetween(Date startDate, Date endDate, TimeMillis millis) {
    try {
      DateFormat sdf = new SimpleDateFormat(Format.F1_YYYY_MM_DD_HH_MM_SS_SSS.getPattern());
      Calendar cal = Calendar.getInstance();
      Date start = sdf.parse(formatDate(startDate, Format.F1_YYYY_MM_DD_HH_MM_SS_SSS));
      Date end = sdf.parse(formatDate(endDate, Format.F1_YYYY_MM_DD_HH_MM_SS_SSS));
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
   * 校验日期格式是否配置
   * <pre>
   *   System.out.println(DateUtil.isValidPattern("")); = false
   *   System.out.println(DateUtil.isValidPattern("哈哈")); = false
   *   System.out.println(DateUtil.isValidPattern("AA")); = false
   *   System.out.println(DateUtil.isValidPattern("yyyy")); = true
   *   System.out.println(DateUtil.isValidPattern("MM")); = true
   *   System.out.println(DateUtil.isValidPattern("dd")); = true
   *   System.out.println(DateUtil.isValidPattern("E")); = true
   *   System.out.println(DateUtil.isValidPattern("HH:mm:ss")); = true
   *   System.out.println(DateUtil.isValidPattern("MM-dd")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy-MM")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy-MM-dd")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy-MM-dd HH:mm")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy-MM-dd HH:mm:ss")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy-MM-dd HH:mm:ss.SSS")); = true
   *   System.out.println(DateUtil.isValidPattern("MMdd")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyyMM")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyyMMdd")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyyMMdd HH:mm")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyyMMdd HH:mm:ss")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyyMMdd HH:mm:ss.SSS")); = true
   *   System.out.println(DateUtil.isValidPattern("MM.dd")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy.MM")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy.MM.dd")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy.MM.dd HH:mm")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy.MM.dd HH:mm:ss")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy.MM.dd HH:mm:ss.SSS")); = true
   *   System.out.println(DateUtil.isValidPattern("MM/dd")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy/MM")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy/MM/dd")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy/MM/dd HH:mm")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy/MM/dd HH:mm:ss")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy/MM/dd HH:mm:ss.SSS")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyyMMddHHmmssSSS")); = true
   *   System.out.println(DateUtil.isValidPattern("MM月dd日")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy年MM月")); = true
   *   System.out.println(DateUtil.isValidPattern("yyyy年MM月dd日")); = true
   * </pre>
   *
   * @param pattern 需要校验的日期格式，需要符合DateUtil.Format格式
   * @return
   */
  public static boolean isValidPattern(String pattern) {
    return Objects.nonNull(Format.getFormat(pattern));
  }

  /**
   * 校验日期字符串
   * <pre>
   *   System.out.println(DateUtil.isValidDate("2020")); = true
   *   System.out.println(DateUtil.isValidDate("04")); = true
   *   System.out.println(DateUtil.isValidDate("15")); = true
   *   System.out.println(DateUtil.isValidDate("星期三")); = false
   *   System.out.println(DateUtil.isValidDate("10:40:09")); = true
   *   System.out.println(DateUtil.isValidDate("04-15")); = true
   *   System.out.println(DateUtil.isValidDate("2020-04")); = true
   *   System.out.println(DateUtil.isValidDate("2020-04-15")); = true
   *   System.out.println(DateUtil.isValidDate("2020-04-15 10:40")); = true
   *   System.out.println(DateUtil.isValidDate("2020-04-15 10:40:09")); = true
   *   System.out.println(DateUtil.isValidDate("2020-04-15 10:40:09.686")); = true
   *   System.out.println(DateUtil.isValidDate("0415")); = true
   *   System.out.println(DateUtil.isValidDate("202004")); = true
   *   System.out.println(DateUtil.isValidDate("20200415")); = true
   *   System.out.println(DateUtil.isValidDate("20200415 10:40")); = true
   *   System.out.println(DateUtil.isValidDate("20200415 10:40:09")); = true
   *   System.out.println(DateUtil.isValidDate("20200415 10:40:09.688")); = true
   *   System.out.println(DateUtil.isValidDate("04.15")); = true
   *   System.out.println(DateUtil.isValidDate("2020.04")); = true
   *   System.out.println(DateUtil.isValidDate("2020.04.15")); = true
   *   System.out.println(DateUtil.isValidDate("2020.04.15 10:40")); = true
   *   System.out.println(DateUtil.isValidDate("2020.04.15 10:40:09")); = true
   *   System.out.println(DateUtil.isValidDate("2020.04.15 10:40:09.691")); = true
   *   System.out.println(DateUtil.isValidDate("04/15")); = true
   *   System.out.println(DateUtil.isValidDate("2020/04")); = true
   *   System.out.println(DateUtil.isValidDate("2020/04/15")); = true
   *   System.out.println(DateUtil.isValidDate("2020/04/15 10:40")); = true
   *   System.out.println(DateUtil.isValidDate("2020/04/15 10:40:09")); = true
   *   System.out.println(DateUtil.isValidDate("2020/04/15 10:40:09.697")); = true
   *   System.out.println(DateUtil.isValidDate("20200415104009698")); = false
   *   System.out.println(DateUtil.isValidDate("04月15日")); = true
   *   System.out.println(DateUtil.isValidDate("2020年04月")); = true
   *   System.out.println(DateUtil.isValidDate("2020年04月15日")); = true
   *  </pre>
   *
   * @param dateStr 需要校验的日期字符串，需要符合DateUtil.Format格式，有些特殊的字符不符合，如：星期*，20200415104009698这种
   * @return
   */
  public static boolean isValidDate(String dateStr) {
    boolean convertSuccess = true;
    for (Format item : Format.values()) {
      try {
        SimpleDateFormat format = new SimpleDateFormat(item.getPattern());
        format.setLenient(false);
        format.parse(dateStr);
      } catch (ParseException e) {
        convertSuccess = false;
      }
      if (convertSuccess) {
        break;
      }
    }
    return convertSuccess;
  }

  /**
   * 校验日期字符串
   * <pre>
   *   System.out.println(DateUtil.isValidDate("2020", Format.F_YYYY)); = true
   *   System.out.println(DateUtil.isValidDate("04", Format.F_MM)); = true
   *   System.out.println(DateUtil.isValidDate("15", Format.F_DD)); = true
   *   System.out.println(DateUtil.isValidDate("星期三", Format.F_E)); = true
   *   System.out.println(DateUtil.isValidDate("10:40:09", Format.F_HH_MM_SS)); = true
   *   System.out.println(DateUtil.isValidDate("04-15", Format.F1_MM_DD)); = true
   *   System.out.println(DateUtil.isValidDate("2020-04", Format.F1_YYYY_MM)); = true
   *   System.out.println(DateUtil.isValidDate("2020-04-15", Format.F1_YYYY_MM_DD)); = true
   *   System.out.println(DateUtil.isValidDate("2020-04-15 10:40", Format.F1_YYYY_MM_DD_HH_MM)); = true
   *   System.out.println(DateUtil.isValidDate("2020-04-15 10:40:09", Format.F1_YYYY_MM_DD_HH_MM_SS)); = true
   *   System.out.println(DateUtil.isValidDate("2020-04-15 10:40:09.686", Format.F1_YYYY_MM_DD_HH_MM_SS_SSS)); = true
   *   System.out.println(DateUtil.isValidDate("0415", Format.F2_MM_DD)); = true
   *   System.out.println(DateUtil.isValidDate("202004", Format.F2_YYYY_MM)); = true
   *   System.out.println(DateUtil.isValidDate("20200415", Format.F2_YYYY_MM_DD)); = true
   *   System.out.println(DateUtil.isValidDate("20200415 10:40", Format.F2_YYYY_MM_DD_HH_MM)); = true
   *   System.out.println(DateUtil.isValidDate("20200415 10:40:09", Format.F2_YYYY_MM_DD_HH_MM_SS)); = true
   *   System.out.println(DateUtil.isValidDate("20200415 10:40:09.688", Format.F2_YYYY_MM_DD_HH_MM_SS_SSS)); = true
   *   System.out.println(DateUtil.isValidDate("04.15", Format.F3_MM_DD)); = true
   *   System.out.println(DateUtil.isValidDate("2020.04", Format.F3_YYYY_MM)); = true
   *   System.out.println(DateUtil.isValidDate("2020.04.15", Format.F3_YYYY_MM_DD)); = true
   *   System.out.println(DateUtil.isValidDate("2020.04.15 10:40", Format.F3_YYYY_MM_DD_HH_MM)); = true
   *   System.out.println(DateUtil.isValidDate("2020.04.15 10:40:09", Format.F3_YYYY_MM_DD_HH_MM_SS)); = true
   *   System.out.println(DateUtil.isValidDate("2020.04.15 10:40:09.691", Format.F3_YYYY_MM_DD_HH_MM_SS_SSS)); = true
   *   System.out.println(DateUtil.isValidDate("04/15", Format.F4_MM_DD)); = true
   *   System.out.println(DateUtil.isValidDate("2020/04", Format.F4_YYYY_MM)); = true
   *   System.out.println(DateUtil.isValidDate("2020/04/15", Format.F4_YYYY_MM_DD)); = true
   *   System.out.println(DateUtil.isValidDate("2020/04/15 10:40", Format.F4_YYYY_MM_DD_HH_MM)); = true
   *   System.out.println(DateUtil.isValidDate("2020/04/15 10:40:09", Format.F4_YYYY_MM_DD_HH_MM_SS)); = true
   *   System.out.println(DateUtil.isValidDate("2020/04/15 10:40:09.697", Format.F4_YYYY_MM_DD_HH_MM_SS_SSS)); = true
   *   System.out.println(DateUtil.isValidDate("20200415104009698", Format.F5_YYYY_MM_DD_HH_MM_SS_SSS)); = true
   *   System.out.println(DateUtil.isValidDate("04月15日", Format.F6_MM_DD)); = true
   *   System.out.println(DateUtil.isValidDate("2020年04月", Format.F6_YYYY_MM)); = true
   *   System.out.println(DateUtil.isValidDate("2020年04月15日", Format.F6_YYYY_MM_DD)); = true
   * </pre>
   *
   * @param dateStr 需要校验的日期字符串，需要符合DateUtil.Format格式
   * @param format  the pattern from Format
   * @return
   */
  public static boolean isValidDate(String dateStr, Format format) {
    boolean convertSuccess = true;
    SimpleDateFormat dateFormat = new SimpleDateFormat(format.getPattern());
    try {
      dateFormat.setLenient(false);
      dateFormat.parse(dateStr);
    } catch (ParseException e) {
      convertSuccess = false;
    }
    return convertSuccess;
  }

  /**
   * 判断选择的日期是否是今日或本月或本周或...
   * <pre>
   *   System.out.println(DateUtil.isThisTime(DateUtil.parseDate("2020-04-14 10:40:09.686"), Format.F1_YYYY_MM_DD)); = false
   *   System.out.println(DateUtil.isThisTime(DateUtil.parseDate("2020-04-15 10:40:09.686"), Format.F1_YYYY_MM_DD)); = true
   *   System.out.println(DateUtil.isThisTime(DateUtil.parseDate("2020-03-15 10:40:09.686"), Format.F1_YYYY_MM)); = false
   *   System.out.println(DateUtil.isThisTime(DateUtil.parseDate("2020-04-15 10:40:09.686"), Format.F1_YYYY_MM)); = true
   *   System.out.println(DateUtil.isThisTime(DateUtil.parseDate("2020-04-12 10:40:09.686"), Format.F_E)); = false
   *   System.out.println(DateUtil.isThisTime(DateUtil.parseDate("2020-04-15 10:40:09.686"), Format.F_E)); = true
   * </pre>
   *
   * @param date   date
   * @param format 今日判断(yyyy-MM-dd),本月判断(yyyy-MM)，本周（E），需要符合DateUtil.Format格式
   * @return
   */
  public static boolean isThisTime(Date date, Format format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format.getPattern());
    String param = sdf.format(date);
    String now = sdf.format(parseDate(getNow()));
    if (param.equals(now)) {
      return true;
    }
    return false;
  }
}
