/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sand.base.enums.TreeEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.common.StringUtil;
import com.sand.base.util.tree.builder.ITreeBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 功能说明：
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 17:33
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "pid", "length", "type", "content", "nodes"})
public class Tree extends AbstractTree {
  /**
   * 树的最大高度
   */
  public static final int MAX_LENGTH = 10000;
  /**
   * 节点ID
   */
  private String id;
  /**
   * 节点父ID
   */
  private String pid;
  /**
   * 节点名称
   */
  private String name;
  private Object content;
  /**
   * 节点类型
   */
  private TreeEnum type;
  /**
   * 节点树，每个节点对应的树
   */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Map<String, Tree> treeMap = new HashMap<>();

  public void add(Tree tree) {
    if (this.type == TreeEnum.LEAF) {
      this.type = TreeEnum.BRANCH;
    }
    children.add(tree);
  }

  @Override
  @JsonIgnore
  public Tree getRoot() {
    if (this.getChildren().size() == 1) {
      return this.getChildren().get(0);
    }
    throw new LsException("根节点不唯一");
  }

  @Override
  public Tree getTree(String id) {
    return treeMap.get(id);
  }

  @Override
  public List<Tree> getChildrenTrees(String id) {
    List<Tree> list = treeMap.values().stream()
        .filter(e -> Objects.equals(e.getPid(), id))
        .collect(Collectors.toList());
    return list;
  }

  @Override
  public Tree getParentTree(String id) {
    Tree tree = treeMap.get(id);
    return treeMap.get(tree.getPid());
  }

  @Override
  public Tree getTree(String id, int offset) {
    Tree tree = treeMap.get(id);
    if (offset < 1) {
      return tree;
    }
    for (int i = 0; i < offset; i++) {
      tree = treeMap.get(tree.getPid());
    }
    return tree;
  }

  @Override
  @JsonIgnore
  public List<Tree> getNodes() {
    return getNodes(this.getChildren());
  }

  @Override
  public List<Tree> getNodes(Collection<Tree> trees) {
    List<Tree> result = new ArrayList<>();
    return getNodes(trees, result);
  }

  @Override
  public List<Tree> getNodes(Collection<Tree> trees, List<Tree> retTrees) {
    if (trees.size() == 0) {
      return retTrees;
    }
    for (Tree tree : trees) {
      retTrees.add(tree);
      if (tree.getChildren().size() > 0) {
        getNodes(tree.getChildren(), retTrees);
      }
    }
    return retTrees;
  }

  @Override
  public <K> List<K> getLeaves(Class<K> clz) {
    return getLeaves(this.getChildren(), clz);
  }

  @Override
  public <K> List<K> getLeaves(String id, Class<K> clz) {
    return getLeaves(getChildrenTrees(id), clz);
  }

  @Override
  public <K> List<K> getLeaves(Collection<Tree> trees, Class<K> clz) {
    List<K> result = new ArrayList<>();
    return getLeaves(trees, clz, result);
  }

  @Override
  public <K> List<K> getLeaves(Collection<Tree> trees, Class<K> clz, List<K> result) {
    if (trees.size() == 0) {
      return result;
    }
    List<Tree> branches = new ArrayList<>();
    for (Tree tree : trees) {
      if (tree.getType() == TreeEnum.LEAF &&
          clz.isAssignableFrom(tree.getContent().getClass())) {
        result.add((K) tree.getContent());
      } else {
        branches.addAll(tree.getChildren());
      }
    }
    result.addAll(getLeaves(branches, clz));
    return result;
  }

  @Override
  public List<Object> getContent(String id, int offset, List<Object> result) {
    return getContent(id, offset, result, Object.class);
  }

  @Override
  public <K> List<K> getContent(String id, int offset, List<K> result, Class<K> clz) {
    Tree tree = treeMap.get(id);
    if (tree.getType() == TreeEnum.BRANCH || tree.getType() == TreeEnum.ROOT) {
      if (offset < 0) {
        getContent(tree.getPid(), offset + 1, result, clz);
      } else if (offset > 0) {
        for (Tree child : tree.getChildren()) {
          getContent(child.getId(), offset - 1, result, clz);
        }
      }
    }
    if (clz.isAssignableFrom(tree.getContent().getClass())) {
      result.add((K) tree.getContent());
    }
    return result;
  }

  @Override
  public List<Object> getChildren(String id) {
    return getChildren(id, Object.class);
  }

  @Override
  public <K> List<K> getChildren(String id, Class<K> clz) {
    List<K> list = new ArrayList<>();
    list = getContent(id, MAX_LENGTH, list, clz);
    list.remove(list.size() - 1);
    return list;
  }

  @Override
  public List<Object> getChildren(String id, int length) {
    return getChildren(id, length, Object.class);
  }

  @Override
  public <K> List<K> getChildren(String id, int length, Class<K> clz) {
    List<K> list = new ArrayList<>();
    list = getContent(id, length, list, clz);
    list.remove(list.size() - 1);
    return list;
  }

  @Override
  public List<Object> getChildrenAndSelf(String id, int length) {
    return getChildrenAndSelf(id, length, Object.class);
  }

  @Override
  public <K> List<K> getChildrenAndSelf(String id, int length, Class<K> clz) {
    List<K> list = new ArrayList<>();
    list = getContent(id, length, list, clz);
    return list;
  }

  @Override
  public <K> List<K> getChildrenAndSelf(String id, Class<K> clz) {
    Tree treeComponent = treeMap.get(id);
    return getChildrenAndSelf(id, treeComponent.getLength(), clz);
  }

  @Override
  public <K> List<K> getChildrenAndSelf(Class<K> clz) {
    String id = this.getChildren().get(0).getId();
    return getChildrenAndSelf(id, clz);
  }

  @Override
  public List<Object> getParent(String id) {
    return getParent(id, Object.class);
  }

  @Override
  public <K> List<K> getParent(String id, Class<K> clz) {
    List<K> list = new ArrayList<>();
    list = getContent(id, -1, list, clz);
    list.remove(list.size() - 1);
    return list;
  }

  @Override
  public List<Object> getParents(String id, int length) {
    return getParents(id, length, Object.class);
  }

  @Override
  public <K> List<K> getParents(String id, int length, Class<K> clz) {
    List<K> list = new ArrayList<>();
    list = getContent(id, -length, list, clz);
    list.remove(list.size() - 1);
    return list;
  }

  @Override
  public List<Object> getParents(String id) {
    return getParents(id, Object.class);
  }

  @Override
  public <K> List<K> getParents(String id, Class<K> clz) {
    List<K> list = new ArrayList<>();
    list = getContent(id, -MAX_LENGTH, list, clz);
    list.remove(list.size() - 1);
    return list;
  }

  @Override
  public <K> Tree add(Map<ITreeBuilder<K>, Collection<K>> data) {
    // 将原始数据组装成树节点
    for (Map.Entry<ITreeBuilder<K>, Collection<K>> entry : data.entrySet()) {
      ITreeBuilder builder = entry.getKey();
      add(builder, entry.getValue());
    }
    return this;
  }

  @Override
  public <K> Tree add(ITreeBuilder<K> builder, Collection<K> children) {
    if (Objects.isNull(children) || children.size() == 0) {
      return this;
    }
    List<Tree> trees = new ArrayList<>();
    children.stream()
        .forEach(e -> {
          Tree tree = new Tree();
          tree.setId(builder.getId(e));
          tree.setPid(builder.getPid(e));
          tree.setName(builder.getName(e));
          tree.setContent(e);
          trees.add(tree);
        });
    add(trees);
    return this;
  }

  @Override
  public Tree add(Collection<Tree> trees) {
    long time = System.currentTimeMillis();
    trees.forEach(tree -> {
      treeMap.put(tree.getId(), tree);
      tree.setType(TreeEnum.LEAF);
    });
    for (Tree tree : trees) {
      if (StringUtil.isBlank(tree.getPid()) || !treeMap.containsKey(tree.getPid())) {
        // 父节点不存在，即为根节点的情况
        // 添加到根节点集合
        tree.setType(TreeEnum.ROOT);
        children.add(tree);
      } else {
        // 下挂到父节点
        treeMap.get(tree.getPid()).add(tree);
      }
      // 判断是否为根节点的父
      for (int i = children.size() - 1; i >= 0; i--) {
        Tree root = children.get(i);
        if (Objects.equals(root.getPid(), tree.getId())) {
          // 原有根节点下挂到新的根，并变成分支节点
          tree.add(root);
          root.setType(TreeEnum.BRANCH);
          children.remove(root);
        }
      }
    }
    time = System.currentTimeMillis() - time;
    if (time > 1000) {
      log.warn("树形构建完成，耗时{}ms", time);
    }
    return this;
  }

  @Override
  public boolean isEmpty() {
    return this.treeMap.isEmpty();
  }

  @Override
  public int size() {
    return this.treeMap.size();
  }
}
