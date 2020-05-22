/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/2    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sand.sys.entity.SysRoleMenu;
import org.springframework.stereotype.Repository;

/**
 * 功能说明：角色-菜单关联
 * 开发人员：@author liusha
 * 开发日期：2019/9/2 10:17
 * 功能描述：角色-菜单关联  角色1-N菜单
 */
@Repository
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
}
