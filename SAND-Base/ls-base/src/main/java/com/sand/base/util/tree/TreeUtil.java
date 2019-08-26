/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
  private static final String TREE_ROOT = "treeRoot";

  /**
   * 树合成
   *
   * @param trees
   * @return
   */
  public static final Tree combine(List<Tree> trees) {
    if (trees.size() == 1) {
      return trees.get(0);
    }
    List<Tree> treeComponents = trees.stream()
        .flatMap(tree -> tree.getTreeMap().values().stream())
        .collect(Collectors.toList());
    treeComponents.forEach(component -> component.setChildren(new ArrayList<>()));
    Tree tree = new Tree();
    tree.add(treeComponents);
    return tree;
  }

  /**
   * 添加根节点
   *
   * @param components
   * @param rootName
   * @return
   */
  public static final Tree addRoot(Collection<Tree> components, String rootName) {
    Tree tree = rebuildTree(components);
    return addRoot(tree, rootName);
  }

  public static final Tree addRoot(Tree tree, String rootName) {
    Map<String, Object> rootContent = new HashMap<>();
    rootContent.put("name", rootName);
    rootContent.put("id", TREE_ROOT);
    Tree root = Tree.builder()
        .id(TREE_ROOT)
        .name(rootName)
        .content(rootContent)
        .build();
    return addRoot(tree, root);
  }

  public static final Tree addRoot(Collection<Tree> components, Tree root) {
    Tree tree = rebuildTree(components);
    return addRoot(tree, root);
  }

  public static final Tree addRoot(Tree tree, Tree root) {
    List<Tree> components = tree.getChildren();
    if (components.size() > 0) {
      components.forEach(e -> e.setPid(root.getId()));
    }

    tree.add(Arrays.asList(root));
    return tree;
  }

  private static final Tree rebuildTree(Collection<Tree> components) {
    Tree tree = new Tree();
    if (Objects.nonNull(components) && components.size() > 0) {
      Set<Tree> allNodes = new HashSet<>();
      while (true) {
        allNodes.addAll(components);
        components = components.stream()
            .flatMap(e -> e.getChildren().stream())
            .collect(Collectors.toList());
        if (components.size() == 0) {
          break;
        }
      }
      allNodes.forEach(e -> e.setChildren(new ArrayList<>()));

      tree.add(allNodes);
    }
    return tree;
  }
}
