/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sand.common.vo.ResultVO;
import com.sand.user.entity.AuthUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Map;

/**
 * 功能说明：用户安全认证信息
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 9:00
 * 功能描述：用户安全认证信息
 */
public interface IAuthUserService extends IService<AuthUser> {

}
