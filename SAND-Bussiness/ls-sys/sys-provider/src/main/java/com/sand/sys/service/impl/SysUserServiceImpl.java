/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.sys.entity.SysUser;
import com.sand.sys.mapper.SysUserMapper;
import com.sand.sys.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 16:59
 * 功能描述：用户信息
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
}
