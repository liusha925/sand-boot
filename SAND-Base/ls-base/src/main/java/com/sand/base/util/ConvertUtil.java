/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/19   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util;

import com.sand.base.exception.LsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 功能说明：类型转换工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/19 13:31
 * 功能描述：类型转换工具类，可以自定义转换结果
 */
@Slf4j
public class ConvertUtil<K, T> {

  public ConvertUtil() {
  }

  /**
   * List集合类转换
   *
   * @return
   */
  public Function<List, List> converter() {
    return entities -> convert(entities);
  }

  /**
   * List集合类转换
   *
   * @param entities
   * @return
   */
  public List<T> convert(List<K> entities) {
    return entities.stream()
        .map(entity -> convert(entity))
        .collect(Collectors.toList());
  }

  /**
   * 对象转换
   *
   * @param entity
   * @return
   */
  public T convert(K entity) {
    if (Objects.isNull(entity)) {
      return null;
    }
    ParameterizedType pType = (ParameterizedType) this.getClass().getGenericSuperclass();
    Class<T> clazz = (Class<T>) pType.getActualTypeArguments()[1];
    T t;
    try {
      t = clazz.newInstance();
    } catch (Exception e) {
      log.error("转换器类型实例化错误", e);
      throw new LsException("转换器类型实例化错误");
    }
    beforeConvert(entity, t);
    BeanUtils.copyProperties(entity, t);
    afterConvert(entity, t);
    return t;
  }

  public void beforeConvert(K k, T t) {

  }

  public void afterConvert(K k, T t) {

  }
}
