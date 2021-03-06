/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/10/2    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util;

import com.google.common.collect.Lists;
import com.sand.core.util.convert.SandConvert;
import com.sand.core.util.lang3.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 功能说明：反射工具类
 * 开发人员：@author liusha
 * 开发日期：2019/10/2 21:41
 * 功能描述：反射工具类
 */
@Slf4j
public class ReflectUtil {
  /**
   * get方法前缀
   */
  public static final String GETTER_PREFIX = "get";
  /**
   * set方法前缀
   */
  public static final String SETTER_PREFIX = "set";
  /**
   * class分隔符
   */
  public static final String CGLIB_CLASS_SEPARATOR = "$$";

  /**
   * 调用Getter方法.
   * 支持多级，如：对象名.对象名.方法
   */
  public static <E> E invokeGetter(Object obj, String propertyName) {
    Object object = obj;
    for (String name : StringUtil.split(propertyName, ".")) {
      String getterMethodName = GETTER_PREFIX + StringUtil.capitalize(name);
      object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
    }
    return (E) object;
  }

  /**
   * 调用Setter方法, 仅匹配方法名。
   * 支持多级，如：对象名.对象名.方法
   */
  public static <E> void invokeSetter(Object obj, E value, String propertyName) {
    Object object = obj;
    String[] names = StringUtil.split(propertyName, ".");
    for (int i = 0; i < names.length; i++) {
      if (i < names.length - 1) {
        String getterMethodName = GETTER_PREFIX + StringUtil.capitalize(names[i]);
        object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
      } else {
        String setterMethodName = SETTER_PREFIX + StringUtil.capitalize(names[i]);
        invokeMethodByName(object, setterMethodName, new Object[]{value});
      }
    }
  }

  /**
   * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
   */
  public static <E> E getFieldValue(final Object obj, final String fieldName) {
    Field field = getAccessibleField(obj, fieldName);
    if (field == null) {
      log.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
      return null;
    }
    E result = null;
    try {
      result = (E) field.get(obj);
    } catch (IllegalAccessException e) {
      log.error("不可能抛出的异常{}", e.getMessage());
    }
    return result;
  }

  /**
   * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
   */
  public static <E> void setFieldValue(final Object obj, final String fieldName, final E value) {
    Field field = getAccessibleField(obj, fieldName);
    if (field == null) {
      log.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
      return;
    }
    try {
      field.set(obj, value);
    } catch (IllegalAccessException e) {
      log.error("不可能抛出的异常: {}", e.getMessage());
    }
  }

  /**
   * 直接调用对象方法, 无视private/protected修饰符.
   * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
   * 同时匹配方法名+参数类型，
   */
  public static <E> E invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
    if (obj == null || methodName == null) {
      return null;
    }
    Method method = getAccessibleMethod(obj, methodName, parameterTypes);
    if (method == null) {
      log.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
      return null;
    }
    try {
      return (E) method.invoke(obj, args);
    } catch (Exception e) {
      String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
      throw convertReflectionExceptionToUnchecked(msg, e);
    }
  }

  /**
   * 直接调用对象方法, 无视private/protected修饰符，
   * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
   * 只匹配函数名，如果有多个同名函数调用第一个。
   */
  public static <E> E invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
    Method method = getAccessibleMethodByName(obj, methodName, args.length);
    if (method == null) {
      // 如果为空不报错，直接返回空。
      log.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
      return null;
    }
    try {
      // 类型转换（将参数数据类型转换为目标方法参数类型）
      Class<?>[] cs = method.getParameterTypes();
      for (int i = 0; i < cs.length; i++) {
        if (args[i] != null && !args[i].getClass().equals(cs[i])) {
          if (cs[i] == String.class) {
            args[i] = SandConvert.obj2Str(args[i]);
            if (StringUtil.endsWith((String) args[i], ".0")) {
              args[i] = StringUtil.substringBefore((String) args[i], ".0");
            }
          } else if (cs[i] == Integer.class) {
            args[i] = SandConvert.obj2Int(args[i]);
          } else if (cs[i] == Long.class) {
            args[i] = SandConvert.obj2Long(args[i]);
          } else if (cs[i] == Double.class) {
            args[i] = SandConvert.obj2Double(args[i]);
          } else if (cs[i] == Float.class) {
            args[i] = SandConvert.obj2Float(args[i]);
          } else if (cs[i] == Date.class) {
            if (args[i] instanceof String) {
              args[i] = com.sand.core.util.lang3.DateUtil.parseDate(SandConvert.obj2Str(args[i]));
            } else {
              args[i] = DateUtil.getJavaDate((Double) args[i]);
            }
          }
        }
      }
      return (E) method.invoke(obj, args);
    } catch (Exception e) {
      String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
      throw convertReflectionExceptionToUnchecked(msg, e);
    }
  }

  /**
   * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
   * 如向上转型到Object仍无法找到, 返回null.
   */
  public static Field getAccessibleField(final Object obj, final String fieldName) {
    // 为空不报错。直接返回 null
    if (obj == null) {
      return null;
    }
    Validate.notBlank(fieldName, "fieldName can't be blank");
    for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
      try {
        Field field = superClass.getDeclaredField(fieldName);
        makeAccessible(field);
        return field;
      } catch (NoSuchFieldException e) {
        continue;
      }
    }
    return null;
  }

  /**
   * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
   * 如向上转型到Object仍无法找到, 返回null.
   * 匹配函数名+参数类型。
   * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
   */
  public static Method getAccessibleMethod(final Object obj, final String methodName,
                                           final Class<?>... parameterTypes) {
    // 为空不报错。直接返回 null
    if (obj == null) {
      return null;
    }
    Validate.notBlank(methodName, "methodName can't be blank");
    for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
      try {
        Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
        makeAccessible(method);
        return method;
      } catch (NoSuchMethodException e) {
        continue;
      }
    }
    return null;
  }

  /**
   * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
   * 如向上转型到Object仍无法找到, 返回null.
   * 只匹配函数名。
   * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
   */
  public static Method getAccessibleMethodByName(final Object obj, final String methodName, int argsNum) {
    // 为空不报错。直接返回 null
    if (obj == null) {
      return null;
    }
    Validate.notBlank(methodName, "methodName can't be blank");
    for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
      Method[] methods = searchType.getDeclaredMethods();
      for (Method method : methods) {
        if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum) {
          makeAccessible(method);
          return method;
        }
      }
    }
    return null;
  }

  /**
   * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
   */
  public static void makeAccessible(Method method) {
    if (!method.isAccessible()) {
      if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
        method.setAccessible(true);
      }
    }
  }

  /**
   * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
   */
  public static void makeAccessible(Field field) {
    if (!field.isAccessible()) {
      if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) {
        field.setAccessible(true);
      }
    }
  }

  /**
   * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
   * 如无法找到, 返回Object.class.
   */
  public static <T> Class<T> getClassGenericType(final Class clazz) {
    return getClassGenericType(clazz, 0);
  }

  /**
   * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
   * 如无法找到, 返回Object.class.
   */
  public static Class getClassGenericType(final Class clazz, final int index) {
    Type genType = clazz.getGenericSuperclass();

    if (!(genType instanceof ParameterizedType)) {
      log.debug(clazz.getSimpleName() + "'s superclass not ParameterizedType");
      return Object.class;
    }

    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

    if (index >= params.length || index < 0) {
      log.debug("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
          + params.length);
      return Object.class;
    }
    if (!(params[index] instanceof Class)) {
      log.debug(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
      return Object.class;
    }

    return (Class) params[index];
  }

  public static Class<?> getUserClass(Object instance) {
    if (instance == null) {
      throw new RuntimeException("Instance must not be null");
    }
    Class clazz = instance.getClass();
    if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
      Class<?> superClass = clazz.getSuperclass();
      if (superClass != null && !Object.class.equals(superClass)) {
        return superClass;
      }
    }
    return clazz;

  }

  /**
   * 将反射时的checked exception转换为unchecked exception.
   */
  public static RuntimeException convertReflectionExceptionToUnchecked(String msg, Exception e) {
    if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException) {
      return new IllegalArgumentException(msg, e);
    } else if (e instanceof InvocationTargetException) {
      return new RuntimeException(msg, ((InvocationTargetException) e).getTargetException());
    }
    return new RuntimeException(msg, e);
  }

  /**
   * 对类的所成员属性排序
   *
   * @param clz
   * @param orderAnnotation
   * @return
   */
  public static List<Field> getOrderDeclaredFields(Class<?> clz, final Class<? extends Annotation> orderAnnotation) {
    List<Field> tempFields = getDeclaredFields(clz);
    List<Field> orderedFields = Lists.newArrayList();
    if (CollectionUtils.isEmpty(tempFields)) {
      return tempFields;
    }
    tempFields.forEach(field -> {
      Annotation annotation = field.getAnnotation(orderAnnotation);
      if (Objects.nonNull(annotation)) {
        orderedFields.add(field);
      }
    });
    try {
      orderAnnotation.getDeclaredMethod("order");
    } catch (Exception e) {
      return orderedFields;
    }
    Collections.sort(orderedFields, (o1, o2) -> {
      try {
        Method orderAnnotationMethod = orderAnnotation.getDeclaredMethod("order");
        int order1 = (int) orderAnnotationMethod.invoke(o1.getAnnotation(orderAnnotation));
        int order2 = (int) orderAnnotationMethod.invoke(o2.getAnnotation(orderAnnotation));
        return order1 - order2;
      } catch (Exception e) {
        return 0;
      }
    });
    return orderedFields;
  }

  /**
   * 获取类的所成员属性（包含父类）
   *
   * @param clz
   * @return
   */
  public static List<Field> getDeclaredFields(Class<?> clz) {
    List<Field> tempFields = Lists.newArrayList();
    Class<?> tempClz = clz;
    tempFields.addAll(Arrays.asList(clz.getDeclaredFields()));
    while (Objects.nonNull(tempClz)) {
      tempClz = tempClz.getSuperclass();
      if (Objects.nonNull(tempClz)) {
        tempFields.addAll(Arrays.asList(tempClz.getDeclaredFields()));
      }
    }
    return tempFields;
  }
}
