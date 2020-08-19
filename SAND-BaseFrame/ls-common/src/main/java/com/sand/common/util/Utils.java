/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/6/8    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util;

import com.sand.common.util.lang3.DateUtil;
import com.sand.common.util.lang3.StringUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能说明：通用工具类
 * 开发人员：@author liusha
 * 开发日期：2020/6/8 10:24
 * 功能描述：封装一些不好归类的工具方法
 */
@Slf4j
@NoArgsConstructor
public class Utils {
  /**
   * List中是否包含重复记录，对应实体类需要重写hashCode和equals方法
   *
   * @param list 要校验的List
   * @param <T>  对应实体类
   * @return true：存在重复记录
   */
  public static <T> boolean listHasRepeatRecord(List<T> list) {
    Set<T> set = new HashSet<>(list);
    return list.size() != set.size();
  }

  /**
   * 获取唯一序列号
   * <pre>
   *   System.out.println(Utils.getUniqueSerialNo()); = "202006081446356330693259868"
   * </pre>
   *
   * @return 27位的唯一序列号
   */
  public static synchronized String getUniqueSerialNo() {
    int hashCode = UUID.randomUUID().toString().hashCode();
    String codeStr = String.format("%010d", hashCode < 0 ? -hashCode : hashCode);
    String nowTime = DateUtil.getNow(DateUtil.Format.F5_YYYY_MM_DD_HH_MM_SS_SSS);
    String uniqueSerialNo = nowTime + codeStr;
    log.info("唯一序列号uniqueSerialNo={}", uniqueSerialNo);
    return uniqueSerialNo;
  }

  /**
   * 获取自增序列号
   * <pre>
   *   Arrays.asList(1, 2, 3).forEach(e -> System.out.println(Utils.getIncrSerialNo()));
   *  输出结果：2020060811285563900000001 \n 2020060811285584700000002 \n 2020060811285584700000003
   * </pre>
   *
   * @return 25位或大于25位的自增序列号
   */
  public static String getIncrSerialNo() {
    String nowTime = DateUtil.getNow(DateUtil.Format.F5_YYYY_MM_DD_HH_MM_SS_SSS);
    String incrSerialNo = nowTime + new Gen().getIncrSerial();
    log.info("自增序列号incrSerialNo={}", incrSerialNo);
    return incrSerialNo;
  }

  static class Gen {
    private int uuid;
    private static int init = 1;

    Gen() {
      uuid = init;
      ++init;
    }

    /**
     * 生产自增序列
     *
     * @return 当自增数小于等于8位数时，生成8位的自增序列；当自增数大于8位数时，生成8位以上的自增序列
     */
    String getIncrSerial() {
      return new DecimalFormat("00000000").format(uuid);
    }

  }

  /**
   * 替换掉HTML标签
   * <pre>
   *   System.out.println(Utils.replaceHtml("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">" +
   *   "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">" +
   *   "    <head>" +
   *   "        <meta http-equiv=\"Content-Type\" content=\"convert/html; charset=UTF-8\" />" +
   *   "        <title>简单实例</title>" +
   *   "    </head>" +
   *   "    <body>" +
   *   "    <h1>h1标签</h1>" +
   *   "    <h2>h2标签</h2>" +
   *   "    <h3>h3标签</h3>" +
   *   "    <p>p标签</p>" +
   *   "    <div>div标签 </div>" +
   *   "    <span>span标签</span>" +
   *   "    <strong>strong标签</strong>" +
   *   "    <input type=\"convert\" value=\"input标签\" />" +
   *   "    <textarea>textarea标签</textarea>" +
   *   "    <input type=\"button\" value=\"提交\" />" +
   *   "    <ul>" +
   *   "        <li>1</li>" +
   *   "        <li>2</li>" +
   *   "        <li>3</li>" +
   *   "        <li>4</li>" +
   *   "    </ul>" +
   *   "    </body>" +
   *   "</html>"));
   *   输出结果：
   *   "                   简单实例            h1标签    h2标签    h3标签    p标签    div标签     span标签    strong标签        textarea标签                1        2        3        4"
   * </pre>
   *
   * @param html the html
   * @return the string
   */
  public static String replaceHtml(String html) {
    if (StringUtil.isBlank(html)) {
      return StringUtil.EMPTY;
    }
    String regex = "<.+?>";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(html);
    return matcher.replaceAll(StringUtil.EMPTY);
  }
}
