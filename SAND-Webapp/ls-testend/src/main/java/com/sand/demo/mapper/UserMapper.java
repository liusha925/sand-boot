/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/23   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.demo.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.sand.demo.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * 功能说明：用户信息
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/23 13:07
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Repository
public interface UserMapper extends BaseMapper<UserEntity> {
}
