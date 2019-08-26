/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.result;

import com.sand.base.constant.Constant;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.enums.ResultEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.common.ServletUtil;
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
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 13:30
 * 功能描述：controller统一处理工具类
 */
@Slf4j
public class ResultUtil {
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
    private Object raw;
  }

  private static final String GET_PREFIX = "get";
  private static final String ASC = "asc";

  public ResultUtil() {
  }

  public static ResultEntity ok() {
    return ok(null);
  }

  public static ResultEntity ok(Object obj) {
    return ok(obj, ResultEnum.SUCCESS.getMsg());
  }

  public static ResultEntity ok(Object obj, String msg) {
    return result(obj, ResultEnum.SUCCESS.getCode(), msg);
  }

  public static ResultEntity fail() {
    return fail(ResultEnum.ERROR.getMsg());
  }

  public static ResultEntity fail(String msg) {
    return result(null, ResultEnum.ERROR.getCode(), msg);
  }

  public static ResultEntity fail(ResultEnum resultEnum) {
    return result(null, resultEnum);
  }

  public static ResultEntity result(Object obj, ResultEnum resultEnum) {
    return result(obj, resultEnum.getCode(), resultEnum.getMsg());
  }

  public static ResultEntity result(Object obj, int code, String msg) {
    try {
      obj = formatSelectData(obj);
    } catch (Exception e) {
      log.error("下拉框数据格式化异常：{}", e.getMessage());
      return ResultEntity.builder().code(ResultEnum.ERROR.getCode()).msg(ResultEnum.ERROR.getMsg()).data(null).build();
    }
    ResultEntity ret = ResultEntity.builder().code(code).msg(msg).data(obj).build();
    return ret;
  }

  private static Object formatSelectData(Object obj) throws Exception {
    HttpServletRequest request = ServletUtil.getRequest();
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
        Collection collection = (Collection) obj;
        // 排序及转换
        if (!collection.isEmpty()) {
          Class clazz = collection.iterator().next().getClass();
          Method getKeyMethod = clazz.getMethod(GET_PREFIX + StringUtil.firstToUpperCase(keyFieldName));
          Method getValueMethod = clazz.getMethod(GET_PREFIX + StringUtil.firstToUpperCase(valueFieldName));
          Method getSortMethod = StringUtil.isBlank(sortFieldName) ? null : clazz.getMethod(GET_PREFIX + StringUtil.firstToUpperCase(sortFieldName));
          List<SelectData> selectData = (List<SelectData>) collection.stream()
              .map(object -> {
                try {
                  SelectData data = SelectData.builder()
                      .key(getKeyMethod.invoke(object))
                      .value(getValueMethod.invoke(object))
                      .sort(Objects.isNull(getSortMethod) ? 0 : (Comparable) getSortMethod.invoke(object))
                      .build();
                  if (raw) {
                    data.setRaw(object);
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
    return obj;
  }
}
