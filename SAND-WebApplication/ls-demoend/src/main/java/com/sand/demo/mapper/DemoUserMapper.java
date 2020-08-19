/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/15   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sand.demo.entity.DemoUser;
import org.springframework.stereotype.Repository;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2020/8/15 13:07
 * 功能描述：用户CRUD
 */
@Repository
public interface DemoUserMapper extends BaseMapper<DemoUser> {
}
