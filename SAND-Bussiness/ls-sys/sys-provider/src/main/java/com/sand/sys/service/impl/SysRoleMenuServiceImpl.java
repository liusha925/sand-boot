/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/2    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.sys.entity.SysRoleMenu;
import com.sand.sys.mapper.SysRoleMenuMapper;
import com.sand.sys.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 功能说明：角色-菜单关联
 * 开发人员：@author liusha
 * 开发日期：2019/9/2 10:20
 * 功能描述：角色-菜单关联  角色1-N菜单
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {
}
