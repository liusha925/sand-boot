/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/19   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.validator;

import com.sand.base.core.text.LsConvert;
import com.sand.base.enums.RegexEnum;
import com.sand.base.util.lang3.StringUtil;
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
   * 校验登录用户名
   *
   * @param loginName 登录用户名
   * @return 校验结果
   */
  public static boolean isLoginName(String loginName) {
    return isLoginName(loginName, RegexEnum.LOGIN_NAME);
  }

  /**
   * 校验登录用户名
   *
   * @param loginName 登录用户名
   * @param regex     正则表达式
   * @return 校验结果
   */
  public static boolean isLoginName(String loginName, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(loginName), regex);
  }

  /**
   * 校验登录用户名
   *
   * @param loginName 登录用户名
   * @param regex     正则表达式
   * @return 校验结果
   */
  public static boolean isLoginName(String loginName, String regex) {
    return matchers(LsConvert.obj2Str(loginName), regex);
  }

  /**
   * 校验登录密码
   *
   * @param password 登录密码
   * @return 校验结果
   */
  public static boolean isPassword(String password) {
    return isPassword(password, RegexEnum.PASSWORD);
  }

  /**
   * 校验登录密码
   *
   * @param password 登录密码
   * @param regex    正则表达式
   * @return 校验结果
   */
  public static boolean isPassword(String password, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(password), regex);
  }

  /**
   * 校验登录密码
   *
   * @param password 登录密码
   * @param regex    正则表达式
   * @return 校验结果
   */
  public static boolean isPassword(String password, String regex) {
    return matchers(LsConvert.obj2Str(password), regex);
  }

  /**
   * 校验交易密码
   *
   * @param payPassword 交易密码
   * @return 校验结果
   */
  public static boolean isPayPassword(String payPassword) {
    return isPayPassword(payPassword, RegexEnum.PAY_PASSWORD);
  }

  /**
   * 校验交易密码
   *
   * @param payPassword 交易密码
   * @param regex       正则表达式
   * @return 校验结果
   */
  public static boolean isPayPassword(String payPassword, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(payPassword), regex);
  }

  /**
   * 校验交易密码
   *
   * @param payPassword 交易密码
   * @param regex       正则表达式
   * @return 校验结果
   */
  public static boolean isPayPassword(String payPassword, String regex) {
    return matchers(LsConvert.obj2Str(payPassword), regex);
  }

  /**
   * 校验手机号码
   *
   * @param mobilePhone 手机号码
   * @return 校验结果
   */
  public static boolean isMobilePhone(String mobilePhone) {
    return isMobilePhone(mobilePhone, RegexEnum.MOBILE_NUMBER);
  }

  /**
   * 校验手机号码
   *
   * @param mobilePhone 手机号码
   * @param regex       正则表达式
   * @return 校验结果
   */
  public static boolean isMobilePhone(String mobilePhone, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(mobilePhone), regex);
  }

  /**
   * 校验手机号码
   *
   * @param mobilePhone 手机号码
   * @param regex       正则表达式
   * @return 校验结果
   */
  public static boolean isMobilePhone(String mobilePhone, String regex) {
    return matchers(LsConvert.obj2Str(mobilePhone), regex);
  }

  /**
   * 校验固定电话
   *
   * @param telPhone 固定电话
   * @return 校验结果
   */
  public static boolean isTelPhone(String telPhone) {
    if (telPhone.length() > 9) {
      return isTelPhone(telPhone, RegexEnum.AREA_TEL_NUMBER);
    } else {
      return isTelPhone(telPhone, RegexEnum.TEL_NUMBER);
    }
  }

  /**
   * 校验固定电话
   *
   * @param telPhone 固定电话
   * @param regex    正则表达式
   * @return 校验结果
   */
  public static boolean isTelPhone(String telPhone, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(telPhone), regex);
  }

  /**
   * 校验固定电话
   *
   * @param telPhone 固定电话
   * @param regex    正则表达式
   * @return 校验结果
   */
  public static boolean isTelPhone(String telPhone, String regex) {
    return matchers(LsConvert.obj2Str(telPhone), regex);
  }

  /**
   * 校验银行卡号
   *
   * @param bankCard 银行卡号
   * @return 校验结果
   */
  public static boolean isBankCard(String bankCard) {
    return isBankCard(bankCard, RegexEnum.BANK_NUMBER);
  }

  /**
   * 校验银行卡号
   *
   * @param bankCard 银行卡号
   * @param regex    正则表达式
   * @return 校验结果
   */
  public static boolean isBankCard(String bankCard, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(bankCard), regex);
  }

  /**
   * 校验银行卡号
   *
   * @param bankCard 银行卡号
   * @param regex    正则表达式
   * @return 校验结果
   */
  public static boolean isBankCard(String bankCard, String regex) {
    return matchers(LsConvert.obj2Str(bankCard), regex);
  }

  /**
   * 校验身份证号
   *
   * @param cardId 身份证号
   * @return 校验结果
   */
  public static boolean isCardId(String cardId) {
    return isCardId(cardId, RegexEnum.ID_NUMBER);
  }

  /**
   * 校验身份证号
   *
   * @param cardId 身份证号
   * @param regex  正则表达式
   * @return 校验结果
   */
  public static boolean isCardId(String cardId, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(cardId), regex);
  }

  /**
   * 校验身份证号
   *
   * @param cardId 身份证号
   * @param regex  正则表达式
   * @return 校验结果
   */
  public static boolean isCardId(String cardId, String regex) {
    return matchers(LsConvert.obj2Str(cardId), regex);
  }

  /**
   * 校验姓名
   *
   * @param realName 姓名
   * @return 校验结果
   */
  public static boolean isRealName(String realName) {
    return isRealName(realName, RegexEnum.REAL_NAME);
  }

  /**
   * 校验姓名
   *
   * @param realName 姓名
   * @param regex    正则表达式
   * @return 校验结果
   */
  public static boolean isRealName(String realName, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(realName), regex);
  }

  /**
   * 校验姓名
   *
   * @param realName 姓名
   * @param regex    正则表达式
   * @return 校验结果
   */
  public static boolean isRealName(String realName, String regex) {
    return matchers(LsConvert.obj2Str(realName), regex);
  }

  /**
   * 校验邮箱格式
   *
   * @param email 邮箱
   * @return 校验结果
   */
  public static boolean isEmail(String email) {
    return isEmail(email, RegexEnum.EMAIL);
  }

  /**
   * 校验邮箱格式
   *
   * @param email 邮箱
   * @param regex 正则表达式
   * @return 校验结果
   */
  public static boolean isEmail(String email, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(email), regex);
  }

  /**
   * 校验邮箱格式
   *
   * @param email 邮箱
   * @param regex 正则表达式
   * @return 校验结果
   */
  public static boolean isEmail(String email, String regex) {
    return matchers(LsConvert.obj2Str(email), regex);
  }

  /**
   * 校验数字
   *
   * @param number
   * @return 校验结果
   */
  public static boolean isNumber(String number) {
    return isNumber(number, RegexEnum.NUMBER);
  }

  /**
   * 校验数字
   *
   * @param number
   * @return 校验结果
   */
  public static boolean isNumber(String number, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(number), regex);
  }

  /**
   * 校验数字
   *
   * @param number
   * @return 校验结果
   */
  public static boolean isNumber(String number, String regex) {
    return matchers(LsConvert.obj2Str(number), regex);
  }

  /**
   * 校验整数
   *
   * @param integer
   * @return 校验结果
   */
  public static boolean isInteger(String integer) {
    return isInteger(integer, RegexEnum.INTEGER);
  }

  /**
   * 校验整数
   *
   * @param integer
   * @return 校验结果
   */
  public static boolean isInteger(String integer, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(integer), regex);
  }

  /**
   * 校验整数
   *
   * @param integer
   * @return 校验结果
   */
  public static boolean isInteger(String integer, String regex) {
    return matchers(LsConvert.obj2Str(integer), regex);
  }

  /**
   * 校验小数（两位正数）
   *
   * @param decimal
   * @return 校验结果
   */
  public static boolean isDecimal(String decimal) {
    return isDecimal(decimal, RegexEnum.DECIMAL);
  }

  /**
   * 校验小数（两位正数）
   *
   * @param decimal
   * @return 校验结果
   */
  public static boolean isDecimal(String decimal, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(decimal), regex);
  }

  /**
   * 校验小数（两位正数）
   *
   * @param decimal
   * @return 校验结果
   */
  public static boolean isDecimal(String decimal, String regex) {
    return matchers(LsConvert.obj2Str(decimal), regex);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate
   * @return 校验结果
   */
  public static boolean isDate(String strDate) {
    return isDate(strDate, RegexEnum.DATE);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate
   * @return 校验结果
   */
  public static boolean isDate(String strDate, RegexEnum regex) {
    return matchers(LsConvert.obj2Str(strDate), regex);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate
   * @return 校验结果
   */
  public static boolean isDate(String strDate, String regex) {
    return matchers(LsConvert.obj2Str(strDate), regex);
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
    if (!unifyCode.matches(RegexEnum.UNIFY_CODE.getExpression())) {
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
   * @param str   待匹配的字符串
   * @param regex 正则表达式
   * @return 校验结果
   */
  public static boolean matchers(String str, RegexEnum regex) {
    return matchers(str, regex.getExpression());
  }

  /**
   * 正则匹配
   *
   * @param str   待匹配的字符串
   * @param regex 正则表达式
   * @return 校验结果
   */
  public static boolean matchers(String str, String regex) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(str);
    return matcher.matches();
  }

}
