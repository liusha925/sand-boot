/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.web.util;

import com.sand.base.web.service.IBaseUserDetails;
import com.sand.base.util.ServletUtil;
import com.sand.base.util.lang3.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * 功能说明：token工具类
 * 开发人员：@author liusha
 * 开发日期：2019/11/26 15:44
 * 功能描述：定义token实现类，过期时间相关信息定义
 */
@Data
@Slf4j
public abstract class AbstractTokenUtil {
  /**
   * Token 类型
   */
  public static final String TOKEN_TYPE_BEARER = "Bearer";
  /**
   * 用户唯一ID
   */
  public static final String USER_UNIQUE_ID = "user_unique_id";
  /**
   * 缓存的权限：标识
   */
  public static final String CACHED_PERMISSION = "cached_permission";
  /**
   * 密钥
   */
  private String secret;
  /**
   * 过期时间
   */
  private Long expiration;

  /**
   * 生成token
   *
   * @param userDetails 用户信息
   * @return token
   */
  public String generateToken(IBaseUserDetails userDetails) {
    HttpServletRequest request = ServletUtil.getRequest();
    String userKey = userDetails.getUserId();
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

  /**
   * 根据token获取用户名
   *
   * @param token Token
   * @return String
   */
  public String getUsernameFromToken(String token) {
    String userKey = getUserKeyFromToken(token);
    return userKey.substring(0, userKey.lastIndexOf(":"));
  }

  /**
   * 验证 Token
   *
   * @param token Token
   * @return Boolean
   */
  public Boolean validateToken(String token) {
    if (StringUtil.isNotEmpty(token) && !isTokenExpired(token)) {
      return true;
    }
    return false;
  }

  /**
   * 判断Token是否过期
   *
   * @param token Token
   * @return Boolean
   */
  private Boolean isTokenExpired(String token) {
    Date expirationDate = getExpiredFromToken(token);
    return expirationDate.before(new Date());
  }

  /**
   * 获取过期时间
   *
   * @param token Token
   * @return Date
   */
  public Date getExpiredFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return Objects.nonNull(claims) ? claims.getExpiration() : null;
  }

  /**
   * 获取token解密信息
   *
   * @param token Token
   * @return
   */
  public String getUserKeyFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return Objects.nonNull(claims) ? claims.getSubject() : null;
  }

  /**
   * 获得 Claims
   *
   * @param token Token
   * @return Claims
   */
  private Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser()
          .setSigningKey(secret)
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      throw e;
    }
    return claims;
  }
}
