/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.config;

import com.google.gson.Gson;
import com.sand.common.util.convert.SandConvert;
import com.sand.common.util.global.Config;
import com.sand.redis.config.RedisConfig;
import com.sand.redis.manager.repository.RedisRepository;
import com.sand.security.util.AbstractTokenUtil;
import com.sand.sys.entity.SysUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 功能说明：security安全配置
 * 开发人员：@author liusha
 * 开发日期：2020/3/19 15:35
 * 功能描述：从application.yml配置文件中读取安全认证信息，密钥、token有效期等
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Component
@ConfigurationProperties("sand.security.jwt")
public class JwtTokenUtil extends AbstractTokenUtil {
  /**
   * 用户token信息缓存关键字
   */
  public static final String REDIS_USER_KEY_TOKEN = "user.token";
  /**
   * 用户详细信息缓存关键字
   */
  public static final String REDIS_USER_KEY_DETAIL = "user.detail";

  /**
   * 生成token
   *
   * @param user 用户信息
   * @return token
   */
  public String generateToken(SysUser user) {
    String token = Jwts.builder()
        .setSubject(user.getUserId())
        .setExpiration(generateExpired())
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
    return token;
  }

  /**
   * 存储用户token信息
   *
   * @param user  用户信息
   * @param token token
   */
  public void putUserToken(SysUser user, String token) {
    int dbIndex = SandConvert.obj2Int(Config.getProperty("redis.database.user-session", "0"));
    RedisRepository redisRepository = RedisConfig.getRedisRepository(dbIndex);
    String key = new StringBuilder(REDIS_USER_KEY_TOKEN).append("_").append(user.getUserId()).toString();
    redisRepository.expireHashValue(key, REDIS_USER_KEY_TOKEN, new Gson().toJson(token), expiration);
  }

  /**
   * 存储用户详细信息
   *
   * @param user 用户信息
   */
  public void putUserDetail(SysUser user) {
    int dbIndex = SandConvert.obj2Int(Config.getProperty("redis.database.user-info", "0"));
    RedisRepository redisRepository = RedisConfig.getRedisRepository(dbIndex);
    String key = new StringBuilder(REDIS_USER_KEY_DETAIL).append("_").append(user.getUserId()).toString();
    redisRepository.expireHashValue(key, REDIS_USER_KEY_DETAIL, new Gson().toJson(user), expiration);
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
