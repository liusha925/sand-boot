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
 * 功能描述：正则验证器，需要RegexEnum辅助使用
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
   * @param loginName
   * @return
   */
  public static boolean isLoginName(String loginName) {
    return isLoginName(loginName, RegexEnum.LOGIN_NAME);
  }

  /**
   * 校验登录用户名
   *
   * @param loginName
   * @param regexEnum
   * @return
   */
  public static boolean isLoginName(String loginName, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(loginName), regexEnum);
  }

  /**
   * 校验登录密码
   *
   * @param password
   * @return
   */
  public static boolean isPassword(String password) {
    return isPassword(password, RegexEnum.PASSWORD);
  }

  /**
   * 校验登录密码
   *
   * @param password
   * @param regexEnum
   * @return
   */
  public static boolean isPassword(String password, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(password), regexEnum);
  }

  /**
   * 校验交易密码
   *
   * @param payPassword
   * @return
   */
  public static boolean isPayPassword(String payPassword) {
    return isPayPassword(payPassword, RegexEnum.PAY_PASSWORD);
  }

  /**
   * 校验交易密码
   *
   * @param payPassword
   * @param regexEnum
   * @return
   */
  public static boolean isPayPassword(String payPassword, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(payPassword), regexEnum);
  }

  /**
   * 校验手机号码
   *
   * @param mobilePhone
   * @return
   */
  public static boolean isMobilePhone(String mobilePhone) {
    return isMobilePhone(mobilePhone, RegexEnum.MOBILE_NUMBER);
  }

  /**
   * 校验手机号码
   *
   * @param mobilePhone
   * @param regexEnum
   * @return
   */
  public static boolean isMobilePhone(String mobilePhone, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(mobilePhone), regexEnum);
  }

  /**
   * 校验固定电话
   *
   * @param telPhone
   * @return
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
   * @param telPhone
   * @param regexEnum
   * @return
   */
  public static boolean isTelPhone(String telPhone, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(telPhone), regexEnum);
  }

  /**
   * 校验银行卡号
   *
   * @param bankCard
   * @return
   */
  public static boolean isBankCard(String bankCard) {
    return isBankCard(bankCard, RegexEnum.BANK_NUMBER);
  }

  /**
   * 校验银行卡号
   *
   * @param bankCard
   * @param regexEnum
   * @return
   */
  public static boolean isBankCard(String bankCard, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(bankCard), regexEnum);
  }

  /**
   * 校验身份证号
   *
   * @param cardId
   * @return
   */
  public static boolean isCardId(String cardId) {
    return isCardId(cardId, RegexEnum.ID_NUMBER);
  }

  /**
   * 校验身份证号
   *
   * @param cardId
   * @param regexEnum
   * @return
   */
  public static boolean isCardId(String cardId, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(cardId), regexEnum);
  }

  /**
   * 校验姓名
   *
   * @param realName
   * @return
   */
  public static boolean isRealName(String realName) {
    return isRealName(realName, RegexEnum.REAL_NAME);
  }

  /**
   * 校验姓名
   *
   * @param realName
   * @param regexEnum
   * @return
   */
  public static boolean isRealName(String realName, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(realName), regexEnum);
  }

  /**
   * 校验邮箱格式
   *
   * @param email
   * @return
   */
  public static boolean isEmail(String email) {
    return isEmail(email, RegexEnum.EMAIL);
  }

  /**
   * 校验邮箱格式
   *
   * @param email
   * @param regexEnum
   * @return
   */
  public static boolean isEmail(String email, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(email), regexEnum);
  }

  /**
   * 校验数字
   *
   * @param number
   * @return
   */
  public static boolean isNumber(String number) {
    return isNumber(number, RegexEnum.NUMBER);
  }

  /**
   * 校验数字
   *
   * @param number
   * @return
   */
  public static boolean isNumber(String number, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(number), regexEnum);
  }

  /**
   * 校验整数
   *
   * @param integer
   * @return
   */
  public static boolean isInteger(String integer) {
    return isInteger(integer, RegexEnum.INTEGER);
  }

  /**
   * 校验整数
   *
   * @param integer
   * @return
   */
  public static boolean isInteger(String integer, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(integer), regexEnum);
  }

  /**
   * 校验小数（两位正数）
   *
   * @param decimal
   * @return
   */
  public static boolean isDecimal(String decimal) {
    return isDecimal(decimal, RegexEnum.DECIMAL);
  }

  /**
   * 校验小数（两位正数）
   *
   * @param decimal
   * @return
   */
  public static boolean isDecimal(String decimal, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(decimal), regexEnum);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate
   * @return
   */
  public static boolean isDate(String strDate) {
    return isDate(strDate, RegexEnum.DATE);
  }

  /**
   * 判断字符串是否为日期格式
   *
   * @param strDate
   * @return
   */
  public static boolean isDate(String strDate, RegexEnum regexEnum) {
    return matchers(LsConvert.obj2Str(strDate), regexEnum);
  }

  /**
   * 校验统一社会信用代码
   *
   * @param unifyCode
   * @return
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
   * @return
   */
  public static boolean matchers(String str, RegexEnum regex) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    Pattern pattern = Pattern.compile(regex.getExpression());
    Matcher matcher = pattern.matcher(str);
    return matcher.matches();
  }

}
