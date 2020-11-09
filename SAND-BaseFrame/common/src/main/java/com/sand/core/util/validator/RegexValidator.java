/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/19   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.validator;

import com.sand.core.util.convert.SandConvert;
import com.sand.core.util.lang3.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能说明：正则验证器
 * 开发人员：@author liusha
 * 开发日期：2019/8/19 13:43
 * 功能描述：正则验证器
 */
public class RegexValidator {
  /**
   * 统一社会性用代码基础字符
   */
  public static final String BASE_UNIFY_CODE = "0123456789ABCDEFGHJKLMNPQRTUWXY";

  public RegexValidator() {
  }

  /**
   * 功能说明：正则表达式枚举类
   * 开发人员：@author liusha
   * 开发日期：2019/8/16 8:40
   * 功能描述：包括四要素验证和一些基本的类型验证，也可以自定义验证规则
   */
  @Getter
  @AllArgsConstructor
  public enum Rule {
    // 系统规则
    PAY_PASSWORD("^[0-9]{6}$", "交易密码，6位数字组合"),
    PASSWORD("^[A-Za-z0-9]{6,16}$", "登录密码，6~16位数字或字母组合"),
    LOGIN_NAME("^[A-Za-z0-9]{8,18}$", "登录用户名，8~18位数字或字母组合"),
    // 四要素规则
    REAL_NAME("^([\\u4e00-\\u9fa5]){2,4}$", "真实姓名，仅支持2~4位的中文字符"),
    BANK_NO("^[1-9]{1}[0-9]{7,22}$", "银行卡号，7~22位数字组合且第一位不允许为0"),
    MOBILE("^((13[0-9])|(14[5,7,9])|(15([0-9]))|(16[6,7,8,9])|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))[0-9]{8}$", "手机号码，支持13~19开头的号码"),
    ID_NO("(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)" +
        "|(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)", "身份证号，支持15或18位的证件号"),
    // 通用规则
    INTEGER("\\d*", "整数"),
    NUMERIC("^[0-9]*.[0-9]*$", "数字"),
    DECIMAL("^([0-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])?$", "小数"),
    EMAIL("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", "电子邮箱"),
    TEL_NO("^[1-9]{1}[0-9]{5,8}$", "电话号码"),
    AREA_TEL_NO("^[0][1-9]{2,3}-[1-9]{1}[0-9]{5,8}$", "区号-电话号码"),
    UNIFY_CODE("^([0-9ABCDEFGHJKLMNPQRTUWXY]{2})([0-9]{6})([0-9ABCDEFGHJKLMNPQRTUWXY]{10})$", "统一社会信用代码"),
    DATE("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])" +
        "|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])" +
        "|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])" +
        "|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])" +
        "|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$", "日期格式");
    /**
     * 正则表达式
     */
    private final String expression;
    /**
     * 描述信息
     */
    private final String description;
  }

  /**
   * 校验登录用户名
   * <pre>
   *   System.out.println(RegexValidator.isLoginName("")); = false
   *   System.out.println(RegexValidator.isLoginName(null)); = false
   *   System.out.println(RegexValidator.isLoginName("1234567")); = false
   *   System.out.println(RegexValidator.isLoginName("12345678")); = true
   *   System.out.println(RegexValidator.isLoginName("123456ab")); = true
   *   System.out.println(RegexValidator.isLoginName("ab123456")); = true
   *   System.out.println(RegexValidator.isLoginName("0123456789123456789")); = false
   *   System.out.println(RegexValidator.isLoginName("~!@#$%^&9")); = false
   * </pre>
   *
   * @param loginName 登录用户名
   * @return 校验结果
   */
  public static boolean isLoginName(String loginName) {
    return matchers(SandConvert.obj2Str(loginName), Rule.LOGIN_NAME);
  }

  /**
   * 校验登录用户名
   *
   * @param loginName 登录用户名
   * @param rule      正则表达式
   * @return 校验结果
   */
  public static boolean isLoginName(String loginName, String rule) {
    return matchers(SandConvert.obj2Str(loginName), rule);
  }

  /**
   * 校验登录密码
   * <pre>
   *   System.out.println(RegexValidator.isPassword("")); = false
   *   System.out.println(RegexValidator.isPassword(null)); = false
   *   System.out.println(RegexValidator.isPassword("12345")); = false
   *   System.out.println(RegexValidator.isPassword("123456")); = true
   *   System.out.println(RegexValidator.isPassword("abcdef")); = true
   *   System.out.println(RegexValidator.isPassword("!@#$%^&*()")); = false
   *   System.out.println(RegexValidator.isPassword("abcdef123456")); = true
   *   System.out.println(RegexValidator.isPassword("123456abcdef")); = true
   *   System.out.println(RegexValidator.isPassword("01234567891234567")); = false
   * </pre>
   *
   * @param password 登录密码
   * @return 校验结果
   */
  public static boolean isPassword(String password) {
    return matchers(SandConvert.obj2Str(password), Rule.PASSWORD);
  }

  /**
   * 校验登录密码
   *
   * @param password 登录密码
   * @param rule     正则表达式
   * @return 校验结果
   */
  public static boolean isPassword(String password, String rule) {
    return matchers(SandConvert.obj2Str(password), rule);
  }

  /**
   * 校验交易密码
   * <pre>
   *   System.out.println(RegexValidator.isPayPassword("")); = false
   *   System.out.println(RegexValidator.isPayPassword(null)); = false
   *   System.out.println(RegexValidator.isPayPassword("12345")); = false
   *   System.out.println(RegexValidator.isPayPassword("1234567")); = false
   *   System.out.println(RegexValidator.isPayPassword("123456")); = true
   *   System.out.println(RegexValidator.isPayPassword("abcdef")); = false
   *   System.out.println(RegexValidator.isPayPassword("123abc")); = false
   *   System.out.println(RegexValidator.isPayPassword("abc123")); = false
   *   System.out.println(RegexValidator.isPayPassword("!@#$%1")); = false
   * </pre>
   *
   * @param payPassword 交易密码
   * @return 校验结果
   */
  public static boolean isPayPassword(String payPassword) {
    return matchers(SandConvert.obj2Str(payPassword), Rule.PAY_PASSWORD);
  }

  /**
   * 校验交易密码
   *
   * @param payPassword 交易密码
   * @param rule        正则表达式
   * @return 校验结果
   */
  public static boolean isPayPassword(String payPassword, String rule) {
    return matchers(SandConvert.obj2Str(payPassword), rule);
  }

  /**
   * 校验手机号码
   * <pre>
   *   System.out.println(RegexValidator.isMobile("")); = false
   *   System.out.println(RegexValidator.isMobile(null)); = false
   *   System.out.println(RegexValidator.isMobile("123456")); = false
   *   System.out.println(RegexValidator.isMobile("11111111111")); = false
   *   System.out.println(RegexValidator.isMobile("13312345678")); = true
   *   System.out.println(RegexValidator.isMobile("20012345678")); = false
   * </pre>
   * @param mobile 手机号码
   * @return 校验结果
   */
  public static boolean isMobile(String mobile) {
    return matchers(SandConvert.obj2Str(mobile), Rule.MOBILE);
  }

  /**
   * 校验手机号码
   *
   * @param mobile 手机号码
   * @param rule   正则表达式
   * @return 校验结果
   */
  public static boolean isMobile(String mobile, String rule) {
    return matchers(SandConvert.obj2Str(mobile), rule);
  }

  /**
   * 校验固定电话
   *
   * @param telPhone 固定电话
   * @return 校验结果
   */
  public static boolean isTelPhone(String telPhone) {
    if (telPhone.length() > 9) {
      return matchers(SandConvert.obj2Str(telPhone), Rule.AREA_TEL_NO);
    } else {
      return matchers(SandConvert.obj2Str(telPhone), Rule.TEL_NO);
    }
  }

  /**
   * 校验固定电话
   *
   * @param telPhone 固定电话
   * @param rule     正则表达式
   * @return 校验结果
   */
  public static boolean isTelPhone(String telPhone, String rule) {
    return matchers(SandConvert.obj2Str(telPhone), rule);
  }

  /**
   * 校验银行卡号
   * <pre>
   *   System.out.println(RegexValidator.isBankNo("")); = false
   *   System.out.println(RegexValidator.isBankNo(null)); = false
   *   System.out.println(RegexValidator.isBankNo("123456")); = false
   *   System.out.println(RegexValidator.isBankNo("11234567890123456789123")); = true
   *   System.out.println(RegexValidator.isBankNo("6227002090170481666")); = true
   *   System.out.println(RegexValidator.isBankNo("0123456")); = false
   *   System.out.println(RegexValidator.isBankNo("1234567")); = false
   *   System.out.println(RegexValidator.isBankNo("abcdefg")); = false
   * </pre>
   *
   * @param bankNo 银行卡号
   * @return 校验结果
   */
  public static boolean isBankNo(String bankNo) {
    return matchers(SandConvert.obj2Str(bankNo), Rule.BANK_NO);
  }

  /**
   * 校验银行卡号
   *
   * @param bankNo 银行卡号
   * @param rule   正则表达式
   * @return 校验结果
   */
  public static boolean isBankNo(String bankNo, String rule) {
    return matchers(SandConvert.obj2Str(bankNo), rule);
  }

  /**
   * 校验身份证号
   * <pre>
   *   System.out.println(RegexValidator.isIdNo("")); = false
   *   System.out.println(RegexValidator.isIdNo(null)); = false
   *   System.out.println(RegexValidator.isIdNo("123456789012345")); = false
   *   System.out.println(RegexValidator.isIdNo("123456789012345678")); = false
   *   System.out.println(RegexValidator.isIdNo("235407195106112745")); = true
   *   System.out.println(RegexValidator.isIdNo("11010119900307213X")); = true
   * </pre>
   * @param idNo 身份证号
   * @return 校验结果
   */
  public static boolean isIdNo(String idNo) {
    return matchers(SandConvert.obj2Str(idNo), Rule.ID_NO);
  }

  /**
   * 校验身份证号
   *
   * @param idNo 身份证号
   * @param rule 正则表达式
   * @return 校验结果
   */
  public static boolean isIdNo(String idNo, String rule) {
    return matchers(SandConvert.obj2Str(idNo), rule);
  }

  /**
   * 校验姓名（中文）
   * <pre>
   *   System.out.println(RegexValidator.isRealName("")); = false
   *   System.out.println(RegexValidator.isRealName(null)); = false
   *   System.out.println(RegexValidator.isRealName("123")); = false
   *   System.out.println(RegexValidator.isRealName("abc")); = false
   *   System.out.println(RegexValidator.isRealName("~!@")); = false
   *   System.out.println(RegexValidator.isRealName("呵")); = false
   *   System.out.println(RegexValidator.isRealName("呵呵")); = true
   *   System.out.println(RegexValidator.isRealName("呵呵呵")); = true
   *   System.out.println(RegexValidator.isRealName("呵呵呵呵")); = true
   *   System.out.println(RegexValidator.isRealName("呵呵呵呵呵")); = false
   * </pre>
   *
   * @param realName 姓名
   * @return 校验结果
   */
  public static boolean isRealName(String realName) {
    return matchers(SandConvert.obj2Str(realName), Rule.REAL_NAME);
  }

  /**
   * 校验姓名（自定义）
   *
   * @param realName 姓名
   * @param rule     正则表达式
   * @return 校验结果
   */
  public static boolean isRealName(String realName, String rule) {
    return matchers(SandConvert.obj2Str(realName), rule);
  }

  /**
   * 校验邮箱格式
   *
   * @param email 邮箱
   * @return 校验结果
   */
  public static boolean isEmail(String email) {
    return matchers(SandConvert.obj2Str(email), Rule.EMAIL);
  }

  /**
   * 校验邮箱格式
   *
   * @param email 邮箱
   * @param rule  正则表达式
   * @return 校验结果
   */
  public static boolean isEmail(String email, String rule) {
    return matchers(SandConvert.obj2Str(email), rule);
  }

  /**
   * 校验数字
   * <pre>
   *   System.out.println(RegexValidator.isNumeric("")); = false
   *   System.out.println(RegexValidator.isNumeric(null)); = false
   *   System.out.println(RegexValidator.isNumeric("-1")); = true
   *   System.out.println(RegexValidator.isNumeric("0")); = true
   *   System.out.println(RegexValidator.isNumeric("1")); = true
   *   System.out.println(RegexValidator.isNumeric("1.0")); = true
   *   System.out.println(RegexValidator.isNumeric("1.1")); = true
   *   System.out.println(RegexValidator.isNumeric("aaa")); = false
   *   System.out.println(RegexValidator.isNumeric("呵呵")); = false
   * </pre>
   *
   * @param numeric numeric
   * @return 校验结果
   */
  public static boolean isNumeric(String numeric) {
    return matchers(SandConvert.obj2Str(numeric), Rule.NUMERIC);
  }

  /**
   * 校验数字
   *
   * @param numeric numeric
   * @return 校验结果
   */
  public static boolean isNumeric(String numeric, String rule) {
    return matchers(SandConvert.obj2Str(numeric), rule);
  }

  /**
   * 校验整数
   * <pre>
   *   System.out.println(RegexValidator.isInteger("")); = false
   *   System.out.println(RegexValidator.isInteger(null)); = false
   *   System.out.println(RegexValidator.isInteger("1")); = true
   *   System.out.println(RegexValidator.isInteger("0.1")); = false
   *   System.out.println(RegexValidator.isInteger("1.1")); = false
   *   System.out.println(RegexValidator.isInteger("a")); = false
   *   System.out.println(RegexValidator.isInteger("呵")); = false
   * </pre>
   * @param integer integer
   * @return 校验结果
   */
  public static boolean isInteger(String integer) {
    return matchers(SandConvert.obj2Str(integer), Rule.INTEGER);
  }

  /**
   * 校验整数
   *
   * @param integer integer
   * @return 校验结果
   */
  public static boolean isInteger(String integer, String rule) {
    return matchers(SandConvert.obj2Str(integer), rule);
  }

  /**
   * 校验小数（两位正数）
   * <pre>
   *   System.out.println(RegexValidator.isDecimal("")); = false
   *   System.out.println(RegexValidator.isDecimal(null)); = false
   *   System.out.println(RegexValidator.isDecimal("1")); = true
   *   System.out.println(RegexValidator.isDecimal("0.1")); = true
   *   System.out.println(RegexValidator.isDecimal("1.1")); = true
   *   System.out.println(RegexValidator.isDecimal("a")); = false
   *   System.out.println(RegexValidator.isDecimal("呵")); = false
   * </pre>
   * @param decimal decimal
   * @return 校验结果
   */
  public static boolean isDecimal(String decimal) {
    return matchers(SandConvert.obj2Str(decimal), Rule.DECIMAL);
  }

  /**
   * 校验小数（两位正数）
   *
   * @param decimal decimal
   * @return 校验结果
   */
  public static boolean isDecimal(String decimal, String rule) {
    return matchers(SandConvert.obj2Str(decimal), rule);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate strDate
   * @return 校验结果
   */
  public static boolean isDate(String strDate) {
    return matchers(SandConvert.obj2Str(strDate), Rule.DATE);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate strDate
   * @return 校验结果
   */
  public static boolean isDate(String strDate, String rule) {
    return matchers(SandConvert.obj2Str(strDate), rule);
  }

  /**
   * 校验统一社会信用代码
   *
   * @param unifyCode unifyCode
   * @return 校验结果
   */
  public static boolean isUnifyCode(String unifyCode) {
    if ((unifyCode.equals(StringUtil.EMPTY)) || unifyCode.length() != 18) {
      return false;
    }
    if (!unifyCode.matches(Rule.UNIFY_CODE.getExpression())) {
      return false;
    }
    char[] baseCodeArray = BASE_UNIFY_CODE.toCharArray();
    Map<Character, Integer> codes = new HashMap<>();
    for (int i = 0; i < BASE_UNIFY_CODE.length(); i++) {
      codes.put(baseCodeArray[i], i);
    }
    char[] businessCodeArray = unifyCode.toCharArray();
    Character check = businessCodeArray[17];
    if (BASE_UNIFY_CODE.indexOf(check) == -1) {
      return false;
    }
    int[] wi = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};
    int sum = 0;
    for (int i = 0; i < 17; i++) {
      Character key = businessCodeArray[i];
      if (BASE_UNIFY_CODE.indexOf(key) == -1) {
        return false;
      }
      sum += (codes.get(key) * wi[i]);
    }
    int value = 31 - sum % 31;
    if (value == 31) {
      value = 0;
    }
    return value == codes.get(check);
  }

  /**
   * 判断是否为0或者正整数
   * <pre>
   *   System.out.println(RegexValidator.isZeroOrPositiveInteger("")); = false
   *   System.out.println(RegexValidator.isZeroOrPositiveInteger(null)); = false
   *   System.out.println(RegexValidator.isZeroOrPositiveInteger("-1")); = false
   *   System.out.println(RegexValidator.isZeroOrPositiveInteger("0")); = true
   *   System.out.println(RegexValidator.isZeroOrPositiveInteger("1")); = true
   *   System.out.println(RegexValidator.isZeroOrPositiveInteger("1.0")); = false
   *   System.out.println(RegexValidator.isZeroOrPositiveInteger("1.1")); = false
   *   System.out.println(RegexValidator.isZeroOrPositiveInteger("aaa")); = false
   *   System.out.println(RegexValidator.isZeroOrPositiveInteger("呵呵")); = false
   * </pre>
   *
   * @param str 字符串
   * @return true-为0或者正整数 false-不为0或者正整数
   */
  public static boolean isZeroOrPositiveInteger(String str) {
    if (StringUtils.isNotBlank(str) && StringUtils.isNumeric(str)) {
      return true;
    }
    return false;
  }

  /**
   * 正则匹配（工具自带）
   *
   * @param str  待匹配的字符串
   * @param rule 正则表达式（工具自带）
   * @return 校验结果
   */
  public static boolean matchers(String str, Rule rule) {
    return matchers(str, rule.getExpression());
  }

  /**
   * 正则匹配（用户自定义）
   *
   * @param str  待匹配的字符串
   * @param rule 正则表达式（用户自定义）
   * @return 校验结果
   */
  public static boolean matchers(String str, String rule) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    Pattern pattern = Pattern.compile(rule);
    Matcher matcher = pattern.matcher(str);
    return matcher.matches();
  }

}
