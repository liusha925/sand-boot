/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sand.user.entity.AuthUser;
import org.springframework.stereotype.Repository;

/**
 * 功能说明：用户信息
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 16:56
 * 功能描述：用户信息
 */
@Repository
public interface AuthUserMapper extends BaseMapper<AuthUser> {

}
