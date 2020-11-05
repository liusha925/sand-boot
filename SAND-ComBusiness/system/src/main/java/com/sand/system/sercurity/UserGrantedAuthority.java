/**
 * 软件版权：流沙
 * 修改记录：
 * 修改日期   修改人员     修改说明
 * =========  ===========  ====================================
 * 2020/9/5/005   liusha      新增
 * =========  ===========  ====================================
 */
package com.sand.system.sercurity;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * 功能说明：用户权限信息 <br>
 * 开发人员：@author liusha
 * 开发日期：2020/9/5/005 <br>
 * 功能描述：自定义用户权限信息 <br>
 */
@Data
@Builder
public class UserGrantedAuthority implements GrantedAuthority {

  /**
   * 权限名称
   */
  private String authName;


  @Override
  public String getAuthority() {
    return null;
  }
}
