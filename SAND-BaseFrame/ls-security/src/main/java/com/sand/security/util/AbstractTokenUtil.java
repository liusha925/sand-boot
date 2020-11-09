/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.security.util;

import com.sand.core.util.ServletUtil;
import com.sand.core.util.lang3.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Slf4j
public abstract class AbstractTokenUtil {
  /**
   * 令牌密钥
   */
  protected String secret;
  /**
   * 令牌过期时间
   */
  protected Long expiration;
  /**
   * 令牌前缀
   */
  public static final String TOKEN_PREFIX = "Bearer";
  /**
   * 令牌头部
   */
  public static final String TOKEN_HEADER = "Authorization";

  /**
   * 获取token
   *
   * @return token值
   */
  public String getTokenFromRequestHeader() {
    HttpServletRequest request = ServletUtil.getRequest();
    String tokenHeader = request.getHeader(TOKEN_HEADER);
    if (StringUtil.isBlank(tokenHeader)) {
      return null;
    }
    String token = tokenHeader;
    if (token.indexOf(" ") > -1) {
      token = tokenHeader.substring(tokenHeader.indexOf(" ") + 1);
    }
    return token;
  }

  /**
   * 根据token获取userId
   *
   * @param token Token
   * @return String
   */
  public String getUserIdFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.getSubject();
  }

  /**
   * 验证 Token 是否有效
   *
   * @param token Token
   * @return Boolean
   */
  public Boolean checkTokenEffective(String token) {
    if (StringUtil.isNotBlank(token) && !checkTokenExpired(token)) {
      return true;
    }
    return false;
  }

  /**
   * 验证 Token 是否过期
   *
   * @param token Token
   * @return Boolean
   */
  private Boolean checkTokenExpired(String token) {
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
