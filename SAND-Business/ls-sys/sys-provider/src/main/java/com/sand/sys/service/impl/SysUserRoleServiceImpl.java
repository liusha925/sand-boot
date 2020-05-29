/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.sys.entity.SysUserRole;
import com.sand.sys.mapper.SysUserRoleMapper;
import com.sand.sys.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能说明：用户角色关系
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 17:04
 * 功能描述：用户角色关系
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

  @Override
  public List<Object> findRoleIdsByUserId(String userId) {
    QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("role_id").eq("user_id", userId);
    return this.listObjs(queryWrapper);
  }
}
