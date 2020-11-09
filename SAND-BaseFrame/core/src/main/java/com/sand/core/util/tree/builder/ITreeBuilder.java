/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.tree.builder;

import com.sand.core.util.lang3.StringUtil;

/**
 * 功能说明：构建树
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 17:00
 * 功能描述：构建树
 */
public interface ITreeBuilder<T> {
  /**
   * 获取节点ID
   *
   * @param t
   * @return
   */
  String getId(T t);

  /**
   * 获取节点父ID
   *
   * @param t
   * @return
   */
  String getPid(T t);

  /**
   * 获取节点名称
   *
   * @param t
   * @return
   */
  default String getName(T t) {
    return StringUtil.EMPTY;
  }
}
