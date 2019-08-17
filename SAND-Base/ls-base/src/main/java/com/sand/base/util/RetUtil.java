/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.util;

import com.sand.base.Ret;
import com.sand.base.constant.Constant;
import com.sand.base.enums.RetEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.common.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 功能说明：controller统一处理工具类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 13:30
 * 功能描述：controller统一处理工具类
 */
@Slf4j
public class RetUtil {
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  static class SelectData {
    private Object key;
    private Object value;
    private Comparable sort;
    private Object raw;
  }

  private static final String SET_PREFIX = "set";
  private static final String GET_PREFIX = "get";
  private static final String ASC = "asc";

  public static Ret success() {
    return success(null);
  }

  public static Ret success(Object object) {
    return success(object, RetEnum.SUCCESS.getMsg());
  }

  public static Ret success(Object object, String msg) {
    return result(object, RetEnum.SUCCESS.getCode(), msg);
  }

  public static Ret error() {
    return error(RetEnum.ERROR.getMsg());
  }

  public static Ret error(String msg) {
    return result(null, RetEnum.ERROR.getCode(), msg);
  }

  public static Ret error(RetEnum retEnum) {
    return result(null, retEnum);
  }

  public static Ret result(Object object, RetEnum retEnum) {
    return result(object, retEnum.getCode(), retEnum.getMsg());
  }

  public static Ret result(Object obj, int code, String msg) {
    try {
      obj = formatSelectData(obj);
    } catch (Exception e) {
      log.error("下拉框数据格式化异常：{}", e.getMessage());
      return Ret.builder().code(RetEnum.ERROR.getCode()).msg(RetEnum.ERROR.getMsg()).data(null).build();
    }
    Ret Ret = com.sand.base.Ret.builder().code(code).msg(msg).data(obj).build();
    return Ret;
  }

  private static Object formatSelectData(Object object) throws Exception {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    if (!Objects.isNull(request.getHeader(Constant.SELECT_REQUEST_FORMAT))) {
      try {
        // 获取域方法
        String keyFieldName = request.getHeader(Constant.SELECT_REQUEST_KEY).trim();
        String valueFieldName = request.getHeader(Constant.SELECT_REQUEST_VALUE).trim();
        String sortFieldName = request.getHeader(Constant.SELECT_REQUEST_SORT).trim();
        // 是否保留原始数据
        boolean raw = Objects.equals("true", request.getHeader(Constant.SELECT_REQUEST_RAW).trim());
        int index = sortFieldName.indexOf(" ");
        final int asc = index > -1 ? (Objects.equals(sortFieldName.substring(index + 1), ASC) ? 1 : -1) : 1;
        sortFieldName = index > -1 ? sortFieldName.substring(0, index) : sortFieldName;
        Collection collection = (Collection) object;
        // 排序及转换
        if (!collection.isEmpty()) {
          Class clazz = collection.iterator().next().getClass();
          Method getKeyMethod = clazz.getMethod(GET_PREFIX + StringUtil.upperCaseFirstLetter(keyFieldName));
          Method getValueMethod = clazz.getMethod(GET_PREFIX + StringUtil.upperCaseFirstLetter(valueFieldName));
          Method getSortMethod = StringUtil.isBlank(sortFieldName) ? null : clazz.getMethod(GET_PREFIX + StringUtil.upperCaseFirstLetter(sortFieldName));
          List<SelectData> selectData = (List<SelectData>) collection.stream()
              .map(obj -> {
                try {
                  SelectData data = SelectData.builder()
                      .key(getKeyMethod.invoke(obj))
                      .value(getValueMethod.invoke(obj))
                      .sort(Objects.isNull(getSortMethod) ? 0 : (Comparable) getSortMethod.invoke(obj))
                      .build();
                  if (raw) {
                    data.setRaw(obj);
                  }
                  return data;
                } catch (Exception e) {
                  throw new LsException(e.getMessage());
                }
              })
              .sorted((a, b) -> asc * ((SelectData) a).getSort().compareTo(((SelectData) b).getSort()))
              .collect(Collectors.toList());
          return selectData;
        }
      } catch (Exception e) {
        throw e;
      }
    }
    return object;
  }
}
