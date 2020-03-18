/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util;

import com.sand.base.constant.Constant;
import com.sand.base.web.entity.ResultEntity;
import com.sand.base.enums.CodeEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.lang3.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 功能说明：controller统一处理工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 13:30
 * 功能描述：controller统一处理工具类
 */
@Slf4j
public class ResultUtil {
  /**
   * get方法
   */
  private static final String PREFIX_GET = "get";
  /**
   * 升序
   */
  private static final String SORT_ASC = "asc";

  public ResultUtil() {
  }

  public static ResultEntity ok() {
    return ok(null);
  }

  public static ResultEntity ok(Object data) {
    return ok(data, CodeEnum.OK.getMsg());
  }

  public static ResultEntity ok(String msg) {
    return ok(null, msg);
  }

  public static ResultEntity ok(Object data, String msg) {
    return info(data, CodeEnum.OK.getCode(), msg);
  }

  public static ResultEntity error() {
    return error(CodeEnum.ERROR.getMsg());
  }

  public static ResultEntity error(String msg) {
    return info(null, CodeEnum.ERROR.getCode(), msg);
  }

  public static ResultEntity error(CodeEnum codeEnum) {
    return info(null, codeEnum);
  }

  public static ResultEntity info(int code, String msg) {
    return info(null, code, msg);
  }

  public static ResultEntity info(Object data, CodeEnum codeEnum) {
    return info(data, codeEnum.getCode(), codeEnum.getMsg());
  }

  public static ResultEntity info(Object data, int code, String msg) {
    try {
      data = formatSelectData(data);
    } catch (Exception e) {
      log.error("下拉框数据格式化异常：{}", e.getMessage());
      return ResultEntity.builder().code(CodeEnum.ERROR.getCode()).msg(CodeEnum.ERROR.getMsg()).data(null).build();
    }
    ResultEntity ret = ResultEntity.builder().code(code).msg(msg).data(data).build();
    return ret;
  }

  private static Object formatSelectData(Object data) throws Exception {
    HttpServletRequest request = ServletUtil.getRequest();
    if (!Objects.isNull(request.getHeader(Constant.SELECT_REQUEST_FORMAT))) {
      try {
        // 获取域方法
        String keyFieldName = request.getHeader(Constant.SELECT_REQUEST_KEY).trim();
        String valueFieldName = request.getHeader(Constant.SELECT_REQUEST_VALUE).trim();
        String sortFieldName = request.getHeader(Constant.SELECT_REQUEST_SORT).trim();
        // 是否保留原始数据
        boolean raw = Objects.equals("true", request.getHeader(Constant.SELECT_REQUEST_RAW).trim());
        int index = sortFieldName.indexOf(StringUtil.SPACE);
        final int asc = index > -1 ? (Objects.equals(sortFieldName.substring(index + 1), SORT_ASC) ? 1 : -1) : 1;
        sortFieldName = index > -1 ? sortFieldName.substring(0, index) : sortFieldName;
        Collection collection = (Collection) data;
        // 排序及转换
        if (!collection.isEmpty()) {
          Class clazz = collection.iterator().next().getClass();
          Method getKeyMethod = clazz.getMethod(PREFIX_GET + StringUtil.firstToUpperCase(keyFieldName));
          Method getValueMethod = clazz.getMethod(PREFIX_GET + StringUtil.firstToUpperCase(valueFieldName));
          Method getSortMethod = StringUtil.isBlank(sortFieldName) ? null : clazz.getMethod(PREFIX_GET + StringUtil.firstToUpperCase(sortFieldName));
          List<SelectData> selectDataList = (List<SelectData>) collection.stream()
              .map(object -> {
                try {
                  SelectData selectData = SelectData.builder()
                      .key(getKeyMethod.invoke(object))
                      .value(getValueMethod.invoke(object))
                      .sort(Objects.isNull(getSortMethod) ? 0 : (Comparable) getSortMethod.invoke(object))
                      .build();
                  if (raw) {
                    selectData.setRaw(object);
                  }
                  return selectData;
                } catch (Exception e) {
                  throw new LsException(e.getMessage());
                }
              })
              .sorted((a, b) -> asc * ((SelectData) a).getSort().compareTo(((SelectData) b).getSort()))
              .collect(Collectors.toList());
          return selectDataList;
        }
      } catch (Exception e) {
        throw e;
      }
    }
    return data;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  static class SelectData {
    /**
     * 请求key
     */
    private Object key;
    /**
     * 请求value
     */
    private Object value;
    /**
     * 排序
     */
    private Comparable sort;
    /**
     * 原始数据
     */
    private Object raw;
  }
}
