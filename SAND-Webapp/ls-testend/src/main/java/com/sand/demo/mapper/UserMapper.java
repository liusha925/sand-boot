/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/23   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sand.demo.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2019/8/23 13:07
 * 功能描述：用户CRUD
 */
@Repository
public interface UserMapper extends BaseMapper<UserEntity> {
}
