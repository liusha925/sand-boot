/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/5    liusha   新增
 * =========  ===========  =====================
 */
package com.sand;

/**
 * 功能说明：Sting.format()用法
 * 开发人员：@author liusha
 * 开发日期：2019/11/5 9:32
 * 功能描述：Sting.format()用法
 */
public class StringFormat {
  public static void main(String[] args) {
    System.out.println(String.format("%s%s%s", "字符串", "类型", "格式化"));
    System.out.println(String.format("字符类型格式化：%c", 'L'));
    System.out.println(String.format("布尔类型格式化：%b", "a".equals("A")));
    System.out.println(String.format("整数类型（十进制）格式化：%d", 100));
    System.out.println(String.format("整数类型（十六进制）格式化：%x", 100));
    System.out.println(String.format("整数类型（八进制）格式化：%o", 100));
    System.out.println(String.format("浮点类型格式化：%f元", 66.66));
    System.out.println(String.format("十六进制浮点类型格式化：%a", 66.66));
    System.out.println(String.format("指数类型格式化：%e", 66.66));
    System.out.println(String.format("通用浮点类型（f和e类型中较短的）格式化：%g", 66.66));
    System.out.println(String.format("A的散列码格式化：%h", 'A'));
    System.out.println(String.format("百分比类型格式化：%d%%%n%s", 66, "顺便换个行（换行符格式化）"));
  }
}
