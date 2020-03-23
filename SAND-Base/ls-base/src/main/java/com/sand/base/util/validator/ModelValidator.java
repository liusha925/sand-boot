/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.validator;

import com.sand.base.enums.CodeEnum;
import com.sand.base.exception.BusinessException;
import com.sand.base.util.lang3.StringUtil;
import com.sand.base.util.spring.SpringUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 功能说明：表单验证器
 * 开发人员：@author liusha
 * 开发日期：2019/9/30 13:57
 * 功能描述：表单校验，验证非空、长度、正则等等
 */
public class ModelValidator {
  /**
   * 校验失败条数key值
   */
  public static final String CHECKED_FAIL_NUM = "checkedFailNum";
  /**
   * 校验失败原因key值
   */
  public static final String CHECKED_FAIL_MSG = "checkedFailMsg";
  /**
   * 校验通过的数据列表key值
   */
  public static final String CHECKED_ENTITY_LIST = "checkedEntityList";

  /**
   * 表单验证（对应实体类配置注解）
   *
   * @param entity 校验对象
   * @param <T>
   */
  public static <T extends Object> void checkModel(T entity) {
    checkModel(entity, null);
  }

  /**
   * 表单验证指定字段（对应实体类配置注解）
   *
   * @param entity 校验对象
   * @param <T>
   */
  public static <T extends Object> void checkModel(T entity, String fieldName) {
    Set<ConstraintViolation<T>> violationSet;
    if (StringUtil.isNotBlank(fieldName)) {
      violationSet = SpringUtil.getBean(Validator.class).validateProperty(entity, fieldName);
    } else {
      violationSet = SpringUtil.getBean(Validator.class).validate(entity);
    }
    if (violationSet.size() > 0) {
      String errorMsg = violationSet.iterator().next().getMessage();
      if (StringUtil.isBlank(errorMsg)) {
        errorMsg = "请求参数有误";
      }
      throw new BusinessException(CodeEnum.PARAM_CHECKED_ERROR, errorMsg);
    }
  }

  /**
   * 批量表单验证（对应实体类配置注解）
   *
   * @param entityList 校验对象列表
   * @param <T>
   * @return 校验结果
   */
  public static <T extends Object> Map<String, Object> checkModelList(List<T> entityList) {
    // 校验失败条数
    int checkedFailNum = 0;
    // 校验失败原因
    StringBuilder checkedFailMsg = new StringBuilder();
    // 校验通过的数据列表
    List<T> checkedEntityList = new ArrayList<>();
    for (int i = 0; i < entityList.size(); i++) {
      try {
        checkModel(entityList.get(i));
      } catch (Exception e) {
        checkedFailNum++;
        checkedFailMsg.append("第" + (i + 1) + "条数据：" + e.getMessage() + "；");
        continue;
      }
      checkedEntityList.add(entityList.get(i));
    }
    Map<String, Object> checkedMap = new HashMap<>();
    checkedMap.put(CHECKED_FAIL_NUM, checkedFailNum);
    checkedMap.put(CHECKED_FAIL_MSG, checkedFailMsg);
    checkedMap.put(CHECKED_ENTITY_LIST, checkedEntityList);
    return checkedMap;
  }
}
