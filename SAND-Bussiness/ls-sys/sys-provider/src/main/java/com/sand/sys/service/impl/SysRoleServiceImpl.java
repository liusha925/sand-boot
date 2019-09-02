/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.base.enums.OperateEnum;
import com.sand.base.enums.ResultEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.lang3.StringUtil;
import com.sand.sys.entity.SysRole;
import com.sand.sys.entity.SysRoleMenu;
import com.sand.sys.enums.RoleEnum;
import com.sand.sys.mapper.SysRoleMapper;
import com.sand.sys.service.ISysRoleMenuService;
import com.sand.sys.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 功能说明：系统角色
 * 开发人员：@author liusha
 * 开发日期：2019/8/30 17:14
 * 功能描述：系统角色
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
  @Autowired
  private ISysRoleMenuService roleMenuService;

  @Override
  @Transactional(rollbackFor = LsException.class)
  public int add(SysRole role) {
    // 参数校验
    checkedSysRole(role, OperateEnum.INSERT);
    // 信息入库
    if (!super.save(role)) {
      throw new LsException("新增角色信息入库异常！");
    }
    // 重新授权
    reauthorize(role);
    return 0;
  }

  @Override
  @Transactional(rollbackFor = LsException.class)
  public int edit(SysRole role) {
    // 参数校验
    checkedSysRole(role, OperateEnum.UPDATE);
    // 信息入库
    if (!super.updateById(role)) {
      throw new LsException("修改角色信息入库异常！");
    }
    // 重新授权
    reauthorize(role);
    return 0;
  }

  @Override
  @Transactional(rollbackFor = LsException.class)
  public void cancelAuthorize(String roleId) {
    // 增强校验，1、新增时信息是否已入库，2、修改时ID是否有变更
    if (StringUtil.isBlank(roleId)) {
      throw new LsException(ResultEnum.PARAM_MISSING_ERROR, "角色ID未分配！");
    }
    QueryWrapper<SysRoleMenu> roleMenuWrapper = new QueryWrapper<>();
    roleMenuWrapper.eq("role_id", roleId);
    if (!roleMenuService.remove(roleMenuWrapper)) {
      throw new LsException("菜单权限取消异常！");
    }
  }

  @Override
  @Transactional(rollbackFor = LsException.class)
  public void reauthorize(SysRole role) {
    // 取消旧的权限
    this.cancelAuthorize(role.getRoleId());
    // 分配新的权限
    List<SysRoleMenu> roleMenuList = new ArrayList<>();
    role.getMenuIds().forEach(menuId -> {
      SysRoleMenu roleMenu = SysRoleMenu.builder().roleId(role.getRoleId()).menuId(menuId).build();
      roleMenuList.add(roleMenu);
    });
    if (!roleMenuService.saveBatch(roleMenuList)) {
      throw new LsException("重新授权菜单异常！");
    }
  }

  /**
   * 参数校验
   *
   * @param role dto
   */
  private void checkedSysRole(SysRole role, OperateEnum operate) {
    // 新增/修改通用参数非空校验
    if (StringUtil.isBlank(role.getRoleName())) {
      throw new LsException(ResultEnum.PARAM_MISSING_ERROR, "角色名称不能为空！");
    }
    if (StringUtil.isBlank(role.getRoleKey())) {
      throw new LsException(ResultEnum.PARAM_MISSING_ERROR, "权限字符不能为空！");
    }
    // 校验角色状态是否存在
    if (Objects.nonNull(role.getStatus())) {
      RoleEnum.Status status = RoleEnum.Status.getByStatus(role.getStatus());
      if (Objects.isNull(status)) {
        throw new LsException(ResultEnum.PARAM_CHECKED_ERROR, "此角色状态不存在！");
      }
    }
    // 校验删除标志是否存在
    if (Objects.nonNull(role.getDelFlag())) {
      RoleEnum.DelFlag delFlag = RoleEnum.DelFlag.getByFlag(role.getDelFlag());
      if (Objects.isNull(delFlag)) {
        throw new LsException(ResultEnum.PARAM_CHECKED_ERROR, "此删除标志不存在！");
      }
    }
    // 校验数据范围是否存在
    if (Objects.nonNull(role.getDataScope())) {
      RoleEnum.DataScope dataScope = RoleEnum.DataScope.getByScope(role.getDataScope());
      if (Objects.isNull(dataScope)) {
        throw new LsException(ResultEnum.PARAM_CHECKED_ERROR, "此数据范围不存在！");
      }
    }
    // 菜单名查询条件组装
    QueryWrapper<SysRole> roleNameWrapper = new QueryWrapper<>();
    roleNameWrapper.eq("role_name", role.getRoleName());
    if (Objects.equals(operate, OperateEnum.INSERT)) {
      if (StringUtil.isNotBlank(role.getRoleId())) {
        SysRole dbRole = super.getById(role.getRoleId());
        if (Objects.nonNull(dbRole)) {
          throw new LsException(ResultEnum.PARAM_CHECKED_ERROR, "此角色信息已存在！");
        }
      }
    } else if (Objects.equals(operate, OperateEnum.UPDATE)) {
      if (StringUtil.isBlank(role.getRoleId())) {
        throw new LsException(ResultEnum.PARAM_MISSING_ERROR, "角色ID不能为空！");
      }
      SysRole dbRole = super.getById(role.getRoleId());
      if (Objects.isNull(dbRole)) {
        throw new LsException(ResultEnum.PARAM_CHECKED_ERROR, "此角色信息不存在！");
      }
      roleNameWrapper.ne("role_id", role.getRoleId());
    }
    // 校验角色名称是否重复
    List<SysRole> roleNameList = super.list(roleNameWrapper);
    if (roleNameList.size() > 0) {
      throw new LsException(ResultEnum.PARAM_CHECKED_ERROR, "此角色名称已存在！");
    }
  }

}
