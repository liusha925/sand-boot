/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.config;

import com.sand.base.util.ServletUtil;
import com.sand.security.web.util.AbstractTokenUtil;
import com.sand.sys.entity.SysUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 功能说明：security安全配置
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 15:35
 * 功能描述：从application.yml配置文件中读取安全认证信息，密钥、token有效期等
 */
@Data
@Component
@ConfigurationProperties("security.jwt")
public class TokenConfig extends AbstractTokenUtil {
  /**
   * 生成token
   *
   * @param user 用户信息
   * @return token
   */
  public String generateToken(SysUser user) {
    String userKey = user.getUserId();
    HttpServletRequest request = ServletUtil.getRequest();
    String userValue = new StringBuilder(userKey).append(":").append(request.getAttribute(USER_UNIQUE_ID)).toString();
    String token = Jwts.builder()
        .setSubject(userValue)
        .setExpiration(generateExpired())
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
    // TODO redis存储token信息
    return token;
  }

  /**
   * 计算过期时间
   *
   * @return Date
   */
  private Date generateExpired() {
    return new Date(System.currentTimeMillis() + expiration * 1000);
  }

}
