/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.tree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sand.base.util.tree.builder.ITree;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：树抽象类
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 17:06
 * 功能描述：使用复合将此类作为适配器
 */
@Data
public abstract class AbstractTree implements ITree {
  /**
   * 除根节点外的子节点树
   */
  protected List<Tree> children = new ArrayList<>();
  /**
   * 构建树警戒时间
   */
  protected static final long BUILD_WARN_TIME = 1000;
  /**
   * 临时存放平级树
   * 此处不做展示，要展示设置JsonProperty.Access.READ_WRITE或直接注释
   */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  protected Map<String, Tree> tempTree = new HashMap<>();

  /**
   * 树高度
   * 如果不想展示，设置JsonProperty.Access.WRITE_ONLY
   *
   * @return
   */
  @Override
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  public int getHeight() {
    int temp;
    int length = 0;
    for (Tree tree : children) {
      temp = (tree.getType() == Tree.TreeType.LEAF ? 1 : tree.getHeight());
      length = length < temp ? temp : length;
    }
    return length + 1;
  }

  /**
   * 节点数量
   * 如果不想展示，设置JsonProperty.Access.WRITE_ONLY
   *
   * @return
   */
  @Override
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  public int getAmount() {
    int amount = 0;
    for (Tree tree : children) {
      amount += (tree.getType() == Tree.TreeType.LEAF ? 1 : tree.getAmount());
    }
    return amount + 1;
  }

  /**
   * 叶子节点数量
   * 如果不想展示，设置JsonProperty.Access.WRITE_ONLY
   *
   * @return
   */
  @Override
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  public int getLeafAmount() {
    int amount = 0;
    for (Tree tree : children) {
      amount += (tree.getType() == Tree.TreeType.LEAF ? 1 : tree.getLeafAmount());
    }
    return amount;
  }
}
