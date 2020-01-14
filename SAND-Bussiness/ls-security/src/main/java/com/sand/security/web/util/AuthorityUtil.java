/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/25    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web.util;

import com.sand.base.core.service.IBaseAuthority;
import com.sand.security.web.bean.AuthCustomAuthority;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 功能说明：权限处理工具类
 * 开发人员：@author liusha
 * 开发日期：2019/11/25 15:19
 * 功能描述：权限处理工具类
 */
public class AuthorityUtil {
  public static List<AuthCustomAuthority> base2Authority(List<IBaseAuthority> authorityList) {
    return Objects.isNull(authorityList) ? null
        : authorityList.stream()
        .map(authority -> new AuthCustomAuthority(authority.getAuthority()))
        .collect(Collectors.toList());
  }
}
