/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.base.enums.OperateEnum;
import com.sand.base.enums.ResultEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.common.StringUtil;
import com.sand.base.util.tree.Tree;
import com.sand.base.util.tree.TreeUtil;
import com.sand.base.util.tree.builder.ITreeBuilder;
import com.sand.sys.entity.SysMenu;
import com.sand.sys.enums.MenuEnum;
import com.sand.sys.mapper.SysMenuMapper;
import com.sand.sys.service.ISysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 16:12
 * 功能描述：系统菜单
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
  @Override
  public Tree buildMenuTree(boolean needButton) {
    List<SysMenu> menuList = super.list();
    // 筛选出除按钮级别的菜单
    if (!needButton) {
      menuList = menuList.stream()
          .filter(menu -> !Objects.equals(menu.getMenuType(), MenuEnum.MenuType.F.getType()))
          .collect(Collectors.toList());
    }
    Tree menuTree = buildTree(menuList);
    TreeUtil.addRoot(menuTree, "主目录");
    return menuTree;
  }

  @Override
  @Transactional(rollbackFor = LsException.class)
  public int add(SysMenu menu) {
    // 参数校验
    checkSysMenu(menu, OperateEnum.INSERT);
    if (!super.save(menu)) {
      throw new LsException("新增菜单信息入库异常！");
    }
    return 0;
  }

  @Override
  @Transactional(rollbackFor = LsException.class)
  public int edit(SysMenu menu) {
    // 参数校验
    checkSysMenu(menu, OperateEnum.UPDATE);
    if (!super.updateById(menu)) {
      throw new LsException("修改菜单信息入库异常！");
    }
    return 0;
  }

  /**
   * 构建树
   *
   * @param menuList
   * @return
   */
  private Tree buildTree(List<SysMenu> menuList) {
    return new Tree().buildTree(menuList, new ITreeBuilder<SysMenu>() {
      @Override
      public String getId(SysMenu menu) {
        return menu.getMenuId();
      }

      @Override
      public String getPid(SysMenu menu) {
        return menu.getParentId();
      }

      @Override
      public String getName(SysMenu menu) {
        return menu.getMenuName();
      }
    });
  }

  /**
   * 参数校验
   *
   * @param menu
   */
  private void checkSysMenu(SysMenu menu, OperateEnum operate) {
    // 新增/修改通用参数非空校验
    if (StringUtil.isBlank(menu.getParentId())) {
      throw new LsException(ResultEnum.PARAM_MISSING_ERROR, "父级菜单ID不能为空！");
    }
    if (StringUtil.isBlank(menu.getMenuName())) {
      throw new LsException(ResultEnum.PARAM_MISSING_ERROR, "菜单名称不能为空！");
    }
    if (StringUtil.isBlank(menu.getMenuName())) {
      throw new LsException(ResultEnum.PARAM_MISSING_ERROR, "菜单类型不能为空！");
    }
    // 不为主目录时需要校验父级菜单
    if (!Objects.equals(menu.getParentId(), TreeUtil.TREE_ROOT)) {
      SysMenu parentMenu = super.getById(menu.getParentId());
      if (Objects.isNull(parentMenu)) {
        throw new LsException(ResultEnum.PARAM_CHECK_ERROR, "父级菜单不存在！");
      }
    }
    // 校验菜单类型是否存在
    MenuEnum.MenuType menuType = MenuEnum.MenuType.getByType(menu.getMenuType());
    if (Objects.isNull(menuType)) {
      throw new LsException(ResultEnum.PARAM_CHECK_ERROR, "此菜单类型不存在！");
    }
    // 菜单名查询条件组装
    QueryWrapper<SysMenu> menuNameWrapper = new QueryWrapper<>();
    menuNameWrapper.eq("parent_id", menu.getParentId()).eq("menu_name", menu.getMenuName());
    if (Objects.equals(operate, OperateEnum.INSERT)) {
      if (StringUtil.isNotBlank(menu.getMenuId())) {
        SysMenu dbMenu = super.getById(menu.getMenuId());
        if (Objects.nonNull(dbMenu)) {
          throw new LsException(ResultEnum.PARAM_CHECK_ERROR, "此菜单信息已存在！");
        }
      }
    } else if (Objects.equals(operate, OperateEnum.UPDATE)) {
      if (StringUtil.isBlank(menu.getMenuId())) {
        throw new LsException(ResultEnum.PARAM_MISSING_ERROR, "菜单ID不能为空！");
      }
      SysMenu dbMenu = super.getById(menu.getMenuId());
      if (Objects.isNull(dbMenu)) {
        throw new LsException(ResultEnum.PARAM_CHECK_ERROR, "此菜单信息不存在！");
      }
      menuNameWrapper.ne("menu_id", menu.getMenuId());
    }
    // 校验同级目录下菜单名是否重复
    List<SysMenu> menuNameList = super.list(menuNameWrapper);
    if (menuNameList.size() > 0) {
      throw new LsException(ResultEnum.PARAM_CHECK_ERROR, "此菜单名称已存在！");
    }
  }
}
