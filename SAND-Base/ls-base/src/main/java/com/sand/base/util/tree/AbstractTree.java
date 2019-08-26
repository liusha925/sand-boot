/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.tree;

import com.sand.base.enums.TreeEnum;
import com.sand.base.util.tree.builder.ITree;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明：树抽象类
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 17:06
 * 功能描述：使用复合将此类作为适配器
 */
@Data
public abstract class AbstractTree implements ITree {

  protected List<Tree> children = new ArrayList<>();

  @Override
  public int getLength() {
    int temp;
    int length = 0;
    for (Tree tree : children) {
      temp = (tree.getType() == TreeEnum.LEAF ? 1 : tree.getLength());
      length = length < temp ? temp : length;
    }
    return length + 1;
  }

  @Override
  public int getAmount() {
    int amount = 0;
    for (Tree tree : children) {
      amount += (tree.getType() == TreeEnum.LEAF ? 1 : tree.getAmount());
    }
    return amount + 1;
  }

  @Override
  public int getSubAmount() {
    int amount = 0;
    for (Tree tree : children) {
      amount += (tree.getType() == TreeEnum.LEAF ? 1 : tree.getSubAmount());
    }
    return amount;
  }
}
