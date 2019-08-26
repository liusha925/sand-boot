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
import java.util.List;
import java.util.Map;

/**
 * 功能说明：树节点
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 17:07
 * 功能描述：树节点
 */
public interface ITree {
  /**
   * 获取树节点高度
   *
   * @return
   */
  int getLength();

  /**
   * 获取树节点数量
   *
   * @return
   */
  int getAmount();

  /**
   * 获取子节点数量
   *
   * @return
   */
  int getSubAmount();

  /**
   * 获取根节点
   *
   * @return
   */
  Tree getRoot();

  /**
   * 获取分支树
   */
  Tree getTree(String id);

  /**
   * 获取子分支树
   *
   * @param id
   * @return
   */
  List<Tree> getChildrenTrees(String id);

  /**
   * 获取父分支树
   *
   * @param id
   * @return
   */
  Tree getParentTree(String id);

  /**
   * 根据偏移量获取父分支树
   *
   * @param id
   * @param offset
   * @return
   */
  Tree getTree(String id, int offset);

  /**
   * 获取节点
   *
   * @return
   */
  List<Tree> getNodes();

  /**
   * 获取节点
   *
   * @param trees
   * @return
   */
  List<Tree> getNodes(Collection<Tree> trees);

  /**
   * 获取节点
   *
   * @param trees
   * @param result
   * @return
   */
  List<Tree> getNodes(Collection<Tree> trees, List<Tree> result);

  /**
   * 获取叶子节点
   *
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getLeaves(Class<K> clz);

  /**
   * 获取叶子节点
   *
   * @param id
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getLeaves(String id, Class<K> clz);

  /**
   * 获取叶子节点
   *
   * @param trees
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getLeaves(Collection<Tree> trees, Class<K> clz);

  /**
   * 获取叶子节点
   *
   * @param trees
   * @param clz
   * @param result
   * @param <K>
   * @return
   */
  <K> List<K> getLeaves(Collection<Tree> trees, Class<K> clz, List<K> result);

  /**
   * 根据偏移量获取节点信息
   *
   * @param id
   * @param offset
   * @param result
   * @return
   */
  List<Object> getContent(String id, int offset, List<Object> result);

  /**
   * 根据偏移量获取节点信息
   *
   * @param id
   * @param offset
   * @param result
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getContent(String id, int offset, List<K> result, Class<K> clz);

  /**
   * 获取直接子节点
   *
   * @param id
   * @return
   */
  List<Object> getChildren(String id);

  /**
   * 获取直接子节点
   *
   * @param id
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getChildren(String id, Class<K> clz);

  /**
   * 获取子节点
   *
   * @param id
   * @param length
   * @return
   */
  List<Object> getChildren(String id, int length);

  /**
   * 获取子节点
   *
   * @param id
   * @param length
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getChildren(String id, int length, Class<K> clz);

  /**
   * 获取子节点和自己
   *
   * @param id
   * @param length
   * @return
   */
  List<Object> getChildrenAndSelf(String id, int length);

  /**
   * 获取子节点和自己
   *
   * @param id
   * @param length
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getChildrenAndSelf(String id, int length, Class<K> clz);

  /**
   * 获取子节点和自己
   *
   * @param id
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getChildrenAndSelf(String id, Class<K> clz);

  /**
   * 获取子节点和自己
   *
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getChildrenAndSelf(Class<K> clz);

  /**
   * 获取父节点信息
   *
   * @param id
   * @return
   */
  List<Object> getParent(String id);

  /**
   * 获取父节点信息
   *
   * @param id
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getParent(String id, Class<K> clz);

  /**
   * 获取父节点信息
   *
   * @param id
   * @param length
   * @return
   */
  List<Object> getParents(String id, int length);

  /**
   * 获取父节点信息
   *
   * @param id
   * @param length
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getParents(String id, int length, Class<K> clz);

  /**
   * 获取所有父节点信息
   *
   * @param id
   * @return
   */
  List<Object> getParents(String id);

  /**
   * 获取所有父节点信息
   *
   * @param id
   * @param clz
   * @param <K>
   * @return
   */
  <K> List<K> getParents(String id, Class<K> clz);

  /**
   * 添加节点数据
   */
  <K> Tree add(Map<ITreeBuilder<K>, Collection<K>> nodes);

  /**
   * 添加节点数据
   *
   * @param builder
   * @param nodes
   * @param <K>
   */
  <K> Tree add(ITreeBuilder<K> builder, Collection<K> nodes);

  /**
   * 添加节点数据
   *
   * @param trees
   */
  Tree add(Collection<Tree> trees);

  /**
   * 树为空
   *
   * @return
   */
  boolean isEmpty();

  /**
   * 树节点数量
   *
   * @return
   */
  int size();
}
