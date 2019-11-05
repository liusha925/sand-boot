/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/5    liusha   新增
 * =========  ===========  =====================
 */
package com.sand;

import java.util.Date;

/**
 * 功能说明：Sting.format()用法
 * 开发人员：@author liusha
 * 开发日期：2019/11/5 10:32
 * 功能描述：Sting.format()用法
 */
public class StringFormatDate {
  public static void main(String[] args) {
    Date date = new Date();
    // c的使用
    System.out.println(String.format("全部日期和时间信息格式化：%tc", date));
    // f的使用
    System.out.println(String.format("年-月-日格式化：%tF", date));
    // d的使用
    System.out.println(String.format("月/日/年格式化：%tD", date));
    // r的使用
    System.out.println(String.format("HH:MM:SS PM（12时制）格式化：%tr", date));
    // t的使用
    System.out.println(String.format("HH:MM:SS（24时制）格式化：%tT", date));
    // R的使用
    System.out.println(String.format("HH:MM（24时制）格式化：%tR", date));
  }
}
