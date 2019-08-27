/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.tree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sand.base.enums.TreeEnum;
import com.sand.base.util.common.StringUtil;
import com.sand.base.util.tree.builder.ITreeBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 功能说明：树形构建
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 17:33
 * 功能描述：树形构建
 */
@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "pid", "name", "type", "height", "amount", "leafAmount", "content", "children"})
public class Tree extends AbstractTree {
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
  /**
   * 节点类型
   */
  private TreeEnum type;
  /**
   * 树对象信息（菜单树、角色树等）
   * 如果不想展示，设置JsonProperty.Access.WRITE_ONLY
   */
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  private Object content;

  @Override
  public void add(Tree tree) {
    if (this.type == TreeEnum.LEAF) {
      this.type = TreeEnum.BRANCH;
    }
    children.add(tree);
  }

  @Override
  public <K> Tree buildTree(Collection<K> children, ITreeBuilder<K> builder) {
    if (Objects.isNull(children) || children.size() == 0) {
      return this;
    }
    List<Tree> trees = new ArrayList<>();
    children.stream()
        .forEach(e -> {
          Tree tree = Tree.builder()
              .id(builder.getId(e))
              .pid(builder.getPid(e))
              .name(builder.getName(e))
              .content(e)
              .build();
          trees.add(tree);
        });
    this.buildTree(trees);
    return this;
  }

  @Override
  public Tree buildTree(Collection<Tree> trees) {
    long startTime = System.currentTimeMillis();
    // 全部看作叶子节点
    trees.forEach(tree -> {
      tempTree.put(tree.getId(), tree);
      tree.setType(TreeEnum.LEAF);
    });
    for (Tree tree : trees) {
      // 寻找根节点
      if (StringUtil.isBlank(tree.getPid()) || !tempTree.containsKey(tree.getPid())) {
        tree.setType(TreeEnum.ROOT);
        children.add(tree);
      } else {
        // 下挂到根节点
        tempTree.get(tree.getPid()).add(tree);
      }
      // 判断是否为根节点
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
    long buildTime = System.currentTimeMillis() - startTime;
    if (buildTime > BUILD_WARN_TIME) {
      log.warn("树形构建完成，耗时{}ms", buildTime);
    }
    return this;
  }

}
