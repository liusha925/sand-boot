/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能说明：树形静态工具
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 17:53
 * 功能描述：树形静态工具
 */
public final class TreeUtil {
  /**
   * 根节点标识
   */
  public static final String TREE_ROOT = "treeRoot";

  /**
   * 添加根节点
   *
   * @param trees    原有的树形结构
   * @param rootObj  根节点实体对象
   * @param rootName 根节点实体名称
   * @return
   */
  public static final Tree addRoot(Collection<Tree> trees, Object rootObj, String rootName) {
    Tree tree = reBuildTree(trees);
    return addRoot(tree, rootObj, rootName);
  }

  /**
   * 添加根节点
   *
   * @param tree     原有的树形结构
   * @param rootObj  根节点实体对象
   * @param rootName 根节点实体名称
   * @return
   */
  public static final Tree addRoot(Tree tree, Object rootObj, String rootName) {
    Tree root = Tree.builder()
        .id(TREE_ROOT)
        .name(rootName)
        .entity(rootObj)
        .build();
    return buildTree(tree, root);
  }

  /**
   * 添加根节点
   *
   * @param trees
   * @param root
   * @return
   */
  public static final Tree addRoot(Collection<Tree> trees, Tree root) {
    Tree tree = reBuildTree(trees);
    return buildTree(tree, root);
  }

  /**
   * 构建有根的树
   *
   * @param tree
   * @param root
   * @return
   */
  public static final Tree buildTree(Tree tree, Tree root) {
    List<Tree> trees = tree.getChildren();
    if (trees.size() > 0) {
      trees.forEach(e -> e.setPid(root.getId()));
    }
    tree.buildTree(Arrays.asList(root));
    return tree;
  }

  /**
   * 重新构建树
   *
   * @param trees
   * @return
   */
  private static final Tree reBuildTree(Collection<Tree> trees) {
    Tree tree = new Tree();
    if (Objects.nonNull(trees) && trees.size() > 0) {
      Set<Tree> allNodes = new HashSet<>();
      while (true) {
        allNodes.addAll(trees);
        trees = trees.stream()
            .flatMap(e -> e.getChildren().stream())
            .collect(Collectors.toList());
        if (trees.size() == 0) {
          break;
        }
      }
      allNodes.forEach(e -> e.setChildren(new ArrayList<>()));
      tree.buildTree(allNodes);
    }
    return tree;
  }

  /**
   * 树合成
   *
   * @param trees
   * @return
   */
  public static final Tree complex(List<Tree> trees) {
    if (trees.size() == 1) {
      return trees.get(0);
    }
    List<Tree> treeNodes = trees.stream()
        .flatMap(tree -> tree.getTempTree().values().stream())
        .collect(Collectors.toList());
    treeNodes.forEach(node -> node.setChildren(new ArrayList<>()));
    Tree tree = new Tree();
    tree.buildTree(treeNodes);
    return tree;
  }
}
