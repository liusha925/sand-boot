/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/13    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.config;

import com.sand.common.util.lang3.StringUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

/**
 * 功能说明：获取文件路径
 * 开发人员：@author liusha
 * 开发日期：2020/3/13 10:03
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
@NoArgsConstructor
public class PathUtil {

  /**
   * <p>获取文件路径.</p>
   * <pre>
   *   PathUtil.getClassPath(new PathUtil()) = "...\\com\\sand\\common\\util\\config"
   * </pre>
   *
   * @param obj 对象
   * @return obj路径
   */
  public static String getClassPath(Object obj) {
    return getClassPath(obj.getClass());
  }

  /**
   * <p>获取文件路径.</p>
   * <pre>
   *   PathUtil.getClassPath(PathUtil.class) = "...\\com\\sand\\common\\util\\config"
   * </pre>
   *
   * @param clz class
   * @return class路径
   */
  public static String getClassPath(Class<?> clz) {
    String path = clz.getResource(StringUtil.EMPTY).getPath();
    return new File(path).getAbsolutePath();
  }

  /**
   * <p>获取对象包的路径.</p>
   * <pre>
   *    PathUtil.getPackagePath(new PathUtil()) = "com/sand/common/util/config"
   * </pre>
   *
   * @param obj 对象
   * @return package路径
   */
  public static String getPackagePath(Object obj) {
    return getPackagePath(obj.getClass());
  }

  /**
   * <p>获取类包的路径.</p>
   * <pre>
   *    PathUtil.getPackagePath(PathUtil.class) = "com/sand/common/util/config"
   * </pre>
   *
   * @param clz class
   * @return package路径
   */
  public static String getPackagePath(Class<?> clz) {
    Package p = clz.getPackage();
    return Objects.isNull(p) ? StringUtil.EMPTY : p.getName().replaceAll("\\.", "/");
  }

  /**
   * 获取ClassLoader
   *
   * @return ClassLoader
   */
  public static ClassLoader getClassLoader() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    return Objects.nonNull(classLoader) ? classLoader : PathUtil.class.getClassLoader();
  }

}
