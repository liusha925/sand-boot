/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.common.enums.OperateType;
import com.sand.common.exception.BusinessException;
import com.sand.common.util.ResultUtil;
import com.sand.common.util.Utils;
import com.sand.common.util.lang3.StringUtil;
import com.sand.common.util.validator.ModelValidator;
import com.sand.common.vo.ResultVO;
import com.sand.system.entity.SysRole;
import com.sand.system.entity.SysRoleMenu;
import com.sand.system.mapper.SysRoleMapper;
import com.sand.system.model.SysRoleModel;
import com.sand.system.service.ISysRoleMenuService;
import com.sand.system.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 功能说明：系统角色
 * 开发人员：@author liusha
 * 开发日期：2019/8/30 17:14
 * 功能描述：系统角色
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
  @Autowired
  private ISysRoleMenuService roleMenuService;

  @Override
  @Transactional(rollbackFor = BusinessException.class)
  public int add(SysRoleModel model) {
    // 参数校验
    checkModel(model, OperateType.INSERT);
    // 信息入库
    if (!super.save(model)) {
      throw new BusinessException("新增角色信息入库异常！");
    }
    // 重新授权
    reauthorize(model);
    return 0;
  }

  @Override
  @Transactional(rollbackFor = BusinessException.class)
  public int edit(SysRoleModel model) {
    // 参数校验
    checkModel(model, OperateType.UPDATE);
    // 信息入库
    if (!super.updateById(model)) {
      throw new BusinessException("修改角色信息入库异常！");
    }
    // 重新授权
    reauthorize(model);
    return 0;
  }

  @Override
  @Transactional(rollbackFor = BusinessException.class)
  public void cancelAuthorize(String roleId) {
    // 增强校验，1、新增时信息是否已入库，2、修改时ID是否有变更
    if (StringUtil.isBlank(roleId)) {
      throw new BusinessException(ResultVO.Code.PARAM_MISSING_ERROR, "角色ID未分配！");
    }
    SysRole dbRole = super.getById(roleId);
    if (Objects.isNull(dbRole)) {
      throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "角色信息不存在！");
    }
    QueryWrapper<SysRoleMenu> roleMenuWrapper = new QueryWrapper<>();
    roleMenuWrapper.eq("role_id", roleId);
    if (!roleMenuService.remove(roleMenuWrapper)) {
      throw new BusinessException("菜单权限取消异常！");
    }
  }

  @Override
  @Transactional(rollbackFor = BusinessException.class)
  public void reauthorize(SysRoleModel model) {
    // 取消旧的权限
    this.cancelAuthorize(model.getRoleId());
    // 分配新的权限
    List<SysRoleMenu> roleMenuList = new ArrayList<>();
    model.getMenuIds().forEach(menuId -> {
      SysRoleMenu roleMenu = SysRoleMenu.builder().roleId(model.getRoleId()).menuId(menuId).build();
      roleMenuList.add(roleMenu);
    });
    if (!roleMenuService.saveBatch(roleMenuList)) {
      throw new BusinessException("重新授权菜单异常！");
    }
  }

  @Override
  public ResultVO imported(List<SysRoleModel> roleList) {
    if (CollectionUtils.isEmpty(roleList)) {
      throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "导入数据不能为空哟！");
    }
    if (Utils.listHasRepeatRecord(roleList)) {
      throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "导入数据存在重复记录，请检查！");
    }
    int failNum = 0;
    StringBuilder failMsg = new StringBuilder();
    List<SysRole> checkedRoleList = new ArrayList<>();
    for (int i = 0; i < roleList.size(); i++) {
      SysRoleModel role = roleList.get(i);
      try {
        checkModel(role, OperateType.INSERT);
      } catch (Exception e) {
        failNum++;
        if (failNum < 10) {
          failMsg.append("第").append(i + 1).append("条数据：").append(e.getMessage()).append("；");
          continue;
        } else {
          // 退出循环前将上一条失败原因打印
          failMsg.append("第").append(i + 1).append("条数据：").append(e.getMessage()).append("；");
          int unCheckedNum = roleList.size() - (i + 1);
          failMsg = new StringBuilder(failMsg).append("剩余未校验数据").append(unCheckedNum).append("条，错误数据太多，请校正后再导入");
          throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, failMsg.toString());
        }
      }
      checkedRoleList.add(role);
    }
    String returnMsg;
    int size = roleList.size();
    int successNum = checkedRoleList.size();
    if (failNum == 0) {
      returnMsg = "导入数据" + size + "条，全部成功";
    } else if (successNum == 0) {
      returnMsg = "导入数据" + size + "条，全部失败，失败原因：" + failMsg;
    } else {
      returnMsg = "导入数据" + size + "条，成功" + successNum + "条，失败" + failNum + "条，失败原因：" + failMsg;
    }
    // 信息入库
    if (!super.saveBatch(checkedRoleList)) {
      throw new BusinessException("导入角色信息入库异常！");
    }
    log.info(returnMsg);
    return ResultUtil.ok(returnMsg);
  }

  /**
   * 参数校验
   *
   * @param model dto
   */
  private void checkModel(SysRoleModel model, OperateType operate) {
    // 表单注解校验，非空，长度，正则等校验
    ModelValidator.checkModel(model);
    // 菜单名查询条件组装
    QueryWrapper<SysRole> roleNameWrapper = new QueryWrapper<>();
    roleNameWrapper.eq("role_name", model.getRoleName());
    if (Objects.equals(operate, OperateType.INSERT)) {
      if (StringUtil.isNotBlank(model.getRoleId())) {
        SysRole dbRole = super.getById(model.getRoleId());
        if (Objects.nonNull(dbRole)) {
          throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "此角色信息已存在！");
        }
      }
    } else if (Objects.equals(operate, OperateType.UPDATE)) {
      if (StringUtil.isBlank(model.getRoleId())) {
        throw new BusinessException(ResultVO.Code.PARAM_MISSING_ERROR, "角色ID不能为空！");
      }
      SysRole dbRole = super.getById(model.getRoleId());
      if (Objects.isNull(dbRole)) {
        throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "此角色信息不存在！");
      }
      roleNameWrapper.ne("role_id", model.getRoleId());
    }
    // 校验角色名称是否重复
    List<SysRole> roleNameList = super.list(roleNameWrapper);
    if (roleNameList.size() > 0) {
      throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "此角色名称已存在！");
    }
  }

}
