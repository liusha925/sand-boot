/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/6   liusha      新增
 * =========  ===========  =====================
 */
package com.sand.common.util.lang3;

import com.sand.common.constant.Constant;
import org.apache.commons.lang3.AnnotationUtils;
import org.aspectj.lang.JoinPoint;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 功能说明：注解工具类
 * 开发人员：@author liusha
 * 开发时间：2019/8/6 23:08
 * 功能描述：继承org.apache.commons.lang3.AnnotationUtils类
 */
public class AnnotationUtil extends AnnotationUtils {

  public AnnotationUtil() {
    super();
  }

  /**
   * 获取某个包下被clazz注解的类
   * <pre>
   *    System.out.println(AnnotationUtil.getAnnotatedClasses("com.sand.common", Component.class));
   *    输出结果：
   *    [
   *      class com.sand.common.util.file.concrete.BIOFileExecutor,
   *      class com.sand.common.util.file.concrete.NIOFileExecutor,
   *      class com.sand.common.util.file.AbstractFileExecutor,
   *      class com.sand.common.util.spring.SpringUtil
   *    ]
   * </pre>
   *
   * @param packageName 包路径
   * @param clz         注解类
   * @return 被注解类集合
   */
  public static Set<Class> getAnnotatedClasses(String packageName, Class clz) {
    Reflections reflections = new Reflections(packageName);
    Set<Class> clzSet = reflections.getTypesAnnotatedWith(clz);
    return clzSet;
  }

  /**
   * 获取某些类下面被annotation注解的成员变量信息
   * <pre>
   *   Set<Class> clzSet = AnnotationUtil.getAnnotatedClasses("com.sand.common", JsonPropertyOrder.class);
   *   System.out.println(AnnotationUtil.getAnnotatedFieldsMap(clzSet, JsonProperty.class));
   *   输出结果：
   *   {
   *     com.sand.common.util.tree.Tree#entity=@com.fasterxml.jackson.annotation.JsonProperty(index=-1, access=READ_WRITE, value=, required=false, defaultValue=)
   *   }
   * </pre>
   *
   * @param clzSet     目标类集合
   * @param annotation 注解类
   * @return key-目标类名#成员变量 value-注解类信息
   */
  public static Map<String, Object> getAnnotatedFieldsMap(Collection<Class> clzSet, Class annotation) {
    Map<String, Object> filterFields = new HashMap<>();
    for (Class clz : clzSet) {
      Field[] fields = clz.getDeclaredFields();
      for (Field field : fields) {
        Object annotationInstance = field.getAnnotation(annotation);
        if (Objects.nonNull(annotationInstance)) {
          filterFields.put(clz.getName() + Constant.CLASS_FIELD_SPLIT_SYMBOL + field.getName(), annotationInstance);
        }
      }
    }
    return filterFields;
  }

  /**
   * 获取某个类下面被annotation注解的成员变量
   * <pre>
   *   System.out.println(AnnotationUtil.getAnnotatedFieldsMap(BaseEntity.class, JsonFormat.class));
   *   输出结果：
   *   {
   *      private java.util.Date com.sand.common.entity.BaseEntity.updateTime=@com.fasterxml.jackson.annotation.JsonFormat(with=[], shape=ANY, timezone=##default, pattern=yyyy-MM-dd HH:mm:ss, locale=##default, lenient=DEFAULT, without=[]),
   *      private java.util.Date com.sand.common.entity.BaseEntity.createTime=@com.fasterxml.jackson.annotation.JsonFormat(with=[], shape=ANY, timezone=##default, pattern=yyyy-MM-dd HH:mm:ss, locale=##default, lenient=DEFAULT, without=[])
   *   }
   * </pre>
   *
   * @param clz        目标类
   * @param annotation 注解类
   * @return key-成员变量信息，value-注解类信息
   */
  public static Map<Field, Object> getAnnotatedFieldsMap(Class clz, Class annotation) {
    Map<Field, Object> filterFields = new HashMap<>();
    Field[] fields = clz.getDeclaredFields();
    for (Field field : fields) {
      Object annotationInstance = field.getAnnotation(annotation);
      if (Objects.nonNull(annotationInstance)) {
        filterFields.put(field, annotationInstance);
      }
    }
    return filterFields;
  }

  /**
   * 获取某个类下面被annotation注解的方法
   * <pre>
   *   System.out.println(AnnotationUtil.getAnnotatedMethods(DefaultExceptionHandler.class, ExceptionHandler.class));
   *   输出结果：
   *   [
   *    public com.sand.common.entity.ResultEntity com.sand.common.exception.handler.DefaultExceptionHandler.handleMissingServletRequestParameterException(org.springframework.web.bind.MissingServletRequestParameterException),
   *    public com.sand.common.entity.ResultEntity com.sand.common.exception.handler.DefaultExceptionHandler.handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException),
   *    public com.sand.common.entity.ResultEntity com.sand.common.exception.handler.DefaultExceptionHandler.handleBusinessException(com.sand.common.exception.BusinessException),
   *    public com.sand.common.entity.ResultEntity com.sand.common.exception.handler.DefaultExceptionHandler.handleException(java.lang.Exception),
   *    public com.sand.common.entity.ResultEntity com.sand.common.exception.handler.DefaultExceptionHandler.handleThrowable(java.lang.Throwable)
   *   ]
   * </pre>
   *
   * @param clz        目标类
   * @param annotation 注解类
   * @return
   */
  public static List<Method> getAnnotatedMethods(Class clz, Class annotation) {
    Method[] methods = clz.getDeclaredMethods();
    List<Method> filterMethods = new ArrayList<>();
    for (Method method : methods) {
      if (Objects.nonNull(method.getAnnotation(annotation))) {
        filterMethods.add(method);
      }
    }
    return filterMethods;
  }

  /**
   * 获取某个类下面被annotation注解的成员变量集合
   * <pre>
   *   System.out.println(AnnotationUtil.getAnnotatedFields(BaseEntity.class, JsonFormat.class));
   *   输出结果：
   *  [
   *    private java.util.Date com.sand.common.entity.BaseEntity.createTime,
   *    private java.util.Date com.sand.common.entity.BaseEntity.updateTime
   *  ]
   * </pre>
   *
   * @param clz        目标类
   * @param annotation 注解类
   * @return 成员变量集合
   */
  public static List<Field> getAnnotatedFields(Class clz, Class annotation) {
    Field[] fields = clz.getDeclaredFields();
    List<Field> filterFields = new ArrayList<>();
    for (Field field : fields) {
      if (Objects.nonNull(field.getAnnotation(annotation))) {
        filterFields.add(field);
      }
    }
    return filterFields;
  }

  /**
   * 获取切面注解信息
   *
   * @param joinPoint 切面
   * @return 成员方法
   * @throws ClassNotFoundException
   */
  public static Method getAnnotationMethod(JoinPoint joinPoint) throws ClassNotFoundException {
    String targetName = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    Object[] arguments = joinPoint.getArgs();
    Class targetClass = Class.forName(targetName);
    Method[] methods = targetClass.getMethods();
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        Class[] clzs = method.getParameterTypes();
        if (clzs.length == arguments.length) {
          return method;
        }
      }
    }
    return null;
  }
}
