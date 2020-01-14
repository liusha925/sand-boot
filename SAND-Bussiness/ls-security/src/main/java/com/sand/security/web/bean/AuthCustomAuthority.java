/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/25    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * 功能说明：权限校验
 * 开发人员：@author liusha
 * 开发日期：2019/11/25 15:21
 * 功能描述：权限校验
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthCustomAuthority implements GrantedAuthority {
  @JsonIgnore
  private String role;

  @Override
  public String getAuthority() {
    return role;
  }
}
