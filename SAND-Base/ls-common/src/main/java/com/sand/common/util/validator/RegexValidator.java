/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/19   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.validator;

import com.sand.common.util.convert.SandConvert;
import com.sand.common.util.lang3.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能说明：正则验证器
 * 开发人员：@author liusha
 * 开发日期：2019/8/19 13:43
 * 功能描述：正则验证器，可以借助RegexEnum辅助使用
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
   * 功能描述：正则表达式枚举类
   */
  @Getter
  @AllArgsConstructor
  public enum Rule {
    // 系统规则
    PASSWORD("^[A-Za-z0-9]{8,16}$", "登录密码"),
    PAY_PASSWORD("^[A-Za-z0-9]{4,10}$", "交易密码"),
    LOGIN_NAME("^[A-Za-z0-9]{8,16}$", "登录用户名"),
    // 四要素规则
    BANK_NUMBER("^[1-9]{1}[0-9]{7,22}$", "银行卡号"),
    REAL_NAME("^([\\u4e00-\\u9fa5]){2,4}$", "真实姓名"),
    MOBILE_NUMBER("^((13[0-9])|(14[5,7,9])|(15([0-9]))|(16[6,7,8,9])|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))[0-9]{8}$", "手机号码"),
    ID_NUMBER("(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)" +
        "|(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)", "身份证号"),
    // 通用规则
    INTEGER("\\d*", "整数"),
    NUMBER("^[0-9]*.[0-9]*$", "数字"),
    DECIMAL("^([0-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])?$", "小数"),
    EMAIL("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", "电子邮箱"),
    TEL_NUMBER("^[1-9]{1}[0-9]{5,8}$", "电话号码"),
    AREA_TEL_NUMBER("^[0][1-9]{2,3}-[1-9]{1}[0-9]{5,8}$", "电话号码-带区号"),
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
   *
   * @param loginName 登录用户名
   * @return 校验结果
   */
  public static boolean isLoginName(String loginName) {
    return isLoginName(loginName, Rule.LOGIN_NAME);
  }

  /**
   * 校验登录用户名
   *
   * @param loginName 登录用户名
   * @param rule      正则表达式
   * @return 校验结果
   */
  public static boolean isLoginName(String loginName, Rule rule) {
    return matchers(SandConvert.obj2Str(loginName), rule);
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
   *
   * @param password 登录密码
   * @return 校验结果
   */
  public static boolean isPassword(String password) {
    return isPassword(password, Rule.PASSWORD);
  }

  /**
   * 校验登录密码
   *
   * @param password 登录密码
   * @param rule     正则表达式
   * @return 校验结果
   */
  public static boolean isPassword(String password, Rule rule) {
    return matchers(SandConvert.obj2Str(password), rule);
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
   *
   * @param payPassword 交易密码
   * @return 校验结果
   */
  public static boolean isPayPassword(String payPassword) {
    return isPayPassword(payPassword, Rule.PAY_PASSWORD);
  }

  /**
   * 校验交易密码
   *
   * @param payPassword 交易密码
   * @param rule        正则表达式
   * @return 校验结果
   */
  public static boolean isPayPassword(String payPassword, Rule rule) {
    return matchers(SandConvert.obj2Str(payPassword), rule);
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
   *
   * @param mobilePhone 手机号码
   * @return 校验结果
   */
  public static boolean isMobilePhone(String mobilePhone) {
    return isMobilePhone(mobilePhone, Rule.MOBILE_NUMBER);
  }

  /**
   * 校验手机号码
   *
   * @param mobilePhone 手机号码
   * @param rule        正则表达式
   * @return 校验结果
   */
  public static boolean isMobilePhone(String mobilePhone, Rule rule) {
    return matchers(SandConvert.obj2Str(mobilePhone), rule);
  }

  /**
   * 校验手机号码
   *
   * @param mobilePhone 手机号码
   * @param rule        正则表达式
   * @return 校验结果
   */
  public static boolean isMobilePhone(String mobilePhone, String rule) {
    return matchers(SandConvert.obj2Str(mobilePhone), rule);
  }

  /**
   * 校验固定电话
   *
   * @param telPhone 固定电话
   * @return 校验结果
   */
  public static boolean isTelPhone(String telPhone) {
    if (telPhone.length() > 9) {
      return isTelPhone(telPhone, Rule.AREA_TEL_NUMBER);
    } else {
      return isTelPhone(telPhone, Rule.TEL_NUMBER);
    }
  }

  /**
   * 校验固定电话
   *
   * @param telPhone 固定电话
   * @param rule     正则表达式
   * @return 校验结果
   */
  public static boolean isTelPhone(String telPhone, Rule rule) {
    return matchers(SandConvert.obj2Str(telPhone), rule);
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
   *
   * @param bankCard 银行卡号
   * @return 校验结果
   */
  public static boolean isBankCard(String bankCard) {
    return isBankCard(bankCard, Rule.BANK_NUMBER);
  }

  /**
   * 校验银行卡号
   *
   * @param bankCard 银行卡号
   * @param rule     正则表达式
   * @return 校验结果
   */
  public static boolean isBankCard(String bankCard, Rule rule) {
    return matchers(SandConvert.obj2Str(bankCard), rule);
  }

  /**
   * 校验银行卡号
   *
   * @param bankCard 银行卡号
   * @param rule     正则表达式
   * @return 校验结果
   */
  public static boolean isBankCard(String bankCard, String rule) {
    return matchers(SandConvert.obj2Str(bankCard), rule);
  }

  /**
   * 校验身份证号
   *
   * @param cardId 身份证号
   * @return 校验结果
   */
  public static boolean isCardId(String cardId) {
    return isCardId(cardId, Rule.ID_NUMBER);
  }

  /**
   * 校验身份证号
   *
   * @param cardId 身份证号
   * @param rule   正则表达式
   * @return 校验结果
   */
  public static boolean isCardId(String cardId, Rule rule) {
    return matchers(SandConvert.obj2Str(cardId), rule);
  }

  /**
   * 校验身份证号
   *
   * @param cardId 身份证号
   * @param rule   正则表达式
   * @return 校验结果
   */
  public static boolean isCardId(String cardId, String rule) {
    return matchers(SandConvert.obj2Str(cardId), rule);
  }

  /**
   * 校验姓名
   *
   * @param realName 姓名
   * @return 校验结果
   */
  public static boolean isRealName(String realName) {
    return isRealName(realName, Rule.REAL_NAME);
  }

  /**
   * 校验姓名
   *
   * @param realName 姓名
   * @param rule     正则表达式
   * @return 校验结果
   */
  public static boolean isRealName(String realName, Rule rule) {
    return matchers(SandConvert.obj2Str(realName), rule);
  }

  /**
   * 校验姓名
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
    return isEmail(email, Rule.EMAIL);
  }

  /**
   * 校验邮箱格式
   *
   * @param email 邮箱
   * @param rule  正则表达式
   * @return 校验结果
   */
  public static boolean isEmail(String email, Rule rule) {
    return matchers(SandConvert.obj2Str(email), rule);
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
   *
   * @param number
   * @return 校验结果
   */
  public static boolean isNumber(String number) {
    return isNumber(number, Rule.NUMBER);
  }

  /**
   * 校验数字
   *
   * @param number
   * @return 校验结果
   */
  public static boolean isNumber(String number, Rule rule) {
    return matchers(SandConvert.obj2Str(number), rule);
  }

  /**
   * 校验数字
   *
   * @param number
   * @return 校验结果
   */
  public static boolean isNumber(String number, String rule) {
    return matchers(SandConvert.obj2Str(number), rule);
  }

  /**
   * 校验整数
   *
   * @param integer
   * @return 校验结果
   */
  public static boolean isInteger(String integer) {
    return isInteger(integer, Rule.INTEGER);
  }

  /**
   * 校验整数
   *
   * @param integer
   * @return 校验结果
   */
  public static boolean isInteger(String integer, Rule rule) {
    return matchers(SandConvert.obj2Str(integer), rule);
  }

  /**
   * 校验整数
   *
   * @param integer
   * @return 校验结果
   */
  public static boolean isInteger(String integer, String rule) {
    return matchers(SandConvert.obj2Str(integer), rule);
  }

  /**
   * 校验小数（两位正数）
   *
   * @param decimal
   * @return 校验结果
   */
  public static boolean isDecimal(String decimal) {
    return isDecimal(decimal, Rule.DECIMAL);
  }

  /**
   * 校验小数（两位正数）
   *
   * @param decimal
   * @return 校验结果
   */
  public static boolean isDecimal(String decimal, Rule rule) {
    return matchers(SandConvert.obj2Str(decimal), rule);
  }

  /**
   * 校验小数（两位正数）
   *
   * @param decimal
   * @return 校验结果
   */
  public static boolean isDecimal(String decimal, String rule) {
    return matchers(SandConvert.obj2Str(decimal), rule);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate
   * @return 校验结果
   */
  public static boolean isDate(String strDate) {
    return isDate(strDate, Rule.DATE);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate
   * @return 校验结果
   */
  public static boolean isDate(String strDate, Rule rule) {
    return matchers(SandConvert.obj2Str(strDate), rule);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate
   * @return 校验结果
   */
  public static boolean isDate(String strDate, String rule) {
    return matchers(SandConvert.obj2Str(strDate), rule);
  }

  /**
   * 校验统一社会信用代码
   *
   * @param unifyCode
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
   * 正则匹配
   *
   * @param str  待匹配的字符串
   * @param rule 正则表达式
   * @return 校验结果
   */
  public static boolean matchers(String str, Rule rule) {
    return matchers(str, rule.getExpression());
  }

  /**
   * 正则匹配
   *
   * @param str  待匹配的字符串
   * @param rule 正则表达式
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
