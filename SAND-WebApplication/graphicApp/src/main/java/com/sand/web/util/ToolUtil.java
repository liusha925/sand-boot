/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/25    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.util;

import com.sand.common.util.convert.SandConvert;

import java.util.List;
import java.util.Map;

/**
 * 功能说明：个性化工具
 * 开发人员：@author liusha
 * 开发日期：2020/8/25 9:04
 * 功能描述：只为解决此项目的问题
 */
public class ToolUtil {
  /**
   * 计算得分
   *
   * @param strValue 值
   * @param rules    规则
   * @return 得分
   */
  public static int calculateScore(String strValue, List<String> rules) {
    int score = 0;
    int intValue = SandConvert.obj2Int(strValue);
    for (int i = 0, size = rules.size(); i < size; i++) {
      if (i < (size - 1)) {
        int rule = SandConvert.obj2Int(rules.get(i));
        int ruleNext = SandConvert.obj2Int(rules.get(i + 1));
        if (intValue >= rule && intValue < ruleNext) {
          score = 20 * (i + 1);
          break;
        }
      } else {
        score = 100;
      }
    }
    return score;
  }

  public static String getRiskAdvice(int num1, int num2, int num3) {
    String riskAdvice = "";
    int level = 1;
    int i;
    if (num1 < 10) {
      i = 1;
    } else if (num1 <= 50) {
      i = 2;
    } else {
      i = 3;
    }
    level = (i > level) ? i : level;
    if (num2 < 3) {
      i = 1;
    } else if (num2 < 6) {
      i = 2;
    } else {
      i = 3;
    }
    level = (i > level) ? i : level;
    if (num2 < 3) {
      i = 1;
    } else if (num2 < 4) {
      i = 2;
    } else {
      i = 3;
    }
    level = (i > level) ? i : level;
    switch (level) {
      case 1:
        riskAdvice = "暂无/较少负面舆情支持全品类产品";
        break;
      case 2:
        riskAdvice = "适度关注负面舆情支持以非银结算模式为主、银行结算为辅产品";
        break;
      case 3:
        riskAdvice = "密切关注负面舆情跟进风险债券处置进度只支持非银结算模式产品";
        break;
      default:
        break;
    }
    return riskAdvice;
  }

  public static String getRiskAdvice(List<Map<String, Object>> riskList) {
    int num1 = 0;
    int num2 = 0;
    int num3 = 0;
    for (Map<String, Object> hashMap : riskList) {
      String name = hashMap.get("name").toString();
      if ("负面舆情".equals(name)) {
        num1 = Integer.parseInt(hashMap.get("max").toString());
      } else if ("投资监督提示".equals(name)) {
        num2 = Integer.parseInt(hashMap.get("max").toString());
      } else {
        num3 = Integer.parseInt(hashMap.get("max").toString());
      }
    }
    return getRiskAdvice(num1, num2, num3);
  }

}
