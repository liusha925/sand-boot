/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/15   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.demo.entity.DemoUser;
import com.sand.demo.mapper.DemoUserMapper;
import com.sand.demo.service.IDemoUserService;
import org.springframework.stereotype.Service;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2020/8/15 13:20
 * 功能描述：用户CRUD
 */
@Service
public class DemoUserServiceImpl extends ServiceImpl<DemoUserMapper, DemoUser> implements IDemoUserService {
}
