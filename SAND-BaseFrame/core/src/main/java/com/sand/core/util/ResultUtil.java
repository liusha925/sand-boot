/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util;

import com.sand.core.vo.ResultVO;
import com.sand.core.exception.BusinessException;
import com.sand.core.util.lang3.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
  /**
   * 选择下拉框标识
   */
  public static final String SELECT_REQUEST_FORMAT = "format";
  /**
   * 选择下拉框值
   */
  public static final String SELECT_REQUEST_KEY = "key";
  /**
   * 选择下拉框显示名称
   */
  public static final String SELECT_REQUEST_VALUE = "value";
  /**
   * 选择下拉框排序
   */
  public static final String SELECT_REQUEST_SORT = "sort";
  /**
   * 选择下拉框保留原始数据
   */
  public static final String SELECT_REQUEST_RAW = "raw";

  public ResultUtil() {
  }

  public static ResultVO ok() {
    return ok(null);
  }

  public static ResultVO ok(Object data) {
    return ok(data, ResultVO.Code.OK.getName());
  }

  public static ResultVO ok(String msg) {
    return ok(null, msg);
  }

  public static ResultVO ok(Object data, String msg) {
    return info(data, ResultVO.Code.OK.getValue(), msg);
  }

  public static ResultVO error() {
    return error(ResultVO.Code.ERROR.getName());
  }

  public static ResultVO error(String msg) {
    return info(null, ResultVO.Code.ERROR.getValue(), msg);
  }

  public static ResultVO error(ResultVO.Code code) {
    return info(null, code);
  }

  public static ResultVO info(int code, String msg) {
    return info(null, code, msg);
  }

  public static ResultVO info(Object data, ResultVO.Code code) {
    return info(data, code.getValue(), code.getName());
  }

  public static ResultVO info(Object data, int code, String msg) {
    try {
      data = formatSelectData(data);
    } catch (Exception e) {
      log.error("下拉框数据格式化异常：{}", e.getMessage());
      return ResultVO.builder().code(ResultVO.Code.ERROR.getValue()).msg(ResultVO.Code.ERROR.getName()).data(null).build();
    }
    ResultVO ret = ResultVO.builder().code(code).msg(msg).data(data).build();
    return ret;
  }

  private static Object formatSelectData(Object data) throws Exception {
    HttpServletRequest request = ServletUtil.getRequest();
    if (!Objects.isNull(request.getHeader(SELECT_REQUEST_FORMAT))) {
      try {
        // 获取域方法
        String keyFieldName = request.getHeader(SELECT_REQUEST_KEY).trim();
        String valueFieldName = request.getHeader(SELECT_REQUEST_VALUE).trim();
        String sortFieldName = request.getHeader(SELECT_REQUEST_SORT).trim();
        // 是否保留原始数据
        boolean raw = Objects.equals("true", request.getHeader(SELECT_REQUEST_RAW).trim());
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
                  throw new BusinessException(e.getMessage());
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

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
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
