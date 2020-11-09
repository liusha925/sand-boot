/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/29    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * 功能说明：流关闭工具类
 * 开发人员：@author liusha
 * 开发日期：2019/9/29 10:02
 * 功能描述：暂时将实现了AutoCloseable接口的流进行关闭，后期使用try-with-resources
 */
@Slf4j
public class CloseableUtil {
  /**
   * 关闭流操作
   *
   * @param closeables
   */
  public static void close(AutoCloseable... closeables) {
    Arrays.stream(closeables).forEach(closeable -> {
      if (Objects.nonNull(closeable)) {
        try {
          closeable.close();
        } catch (Exception e) {
          log.error("关闭流操作出现异常，", e);
          throw new RuntimeException("close stream error", e);
        }
      }
    });
  }
}
