/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.tree.builder;

import com.sand.base.util.tree.Tree;

import java.util.Collection;

/**
 * 功能说明：树节点
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 17:07
 * 功能描述：树节点
 */
public interface ITree {
  /**
   * 获取树高度
   *
   * @return
   */
  int getHeight();

  /**
   * 获取树节点数量
   *
   * @return
   */
  int getAmount();

  /**
   * 获取叶子节点数量
   *
   * @return
   */
  int getLeafAmount();

  /**
   * 添加分支
   *
   * @param tree
   */
  void addBranch(Tree tree);

  /**
   * 构建平级的树
   * 无根无支的叶子树
   *
   * @param children
   * @param builder
   * @param <K>
   * @return
   */
  <K> Tree buildTree(Collection<K> children, ITreeBuilder<K> builder);

  /**
   * 构建真正的树
   * 无根的分支叶子树
   *
   * @param trees
   * @return
   */
  Tree buildTree(Collection<Tree> trees);

}
