/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sand.common.util.convert.SandConvert;
import com.sand.common.util.crypt.des.DesCryptUtil;
import com.sand.common.util.global.Config;
import com.sand.redis.config.properties.RedisDatabase;
import com.sand.redis.config.properties.RedisDatabaseProperties;
import com.sand.redis.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：redis配置
 * 开发人员：@author liusha
 * 开发日期：2020/3/30 17:19
 * 功能描述：在启动系统时候，加载配置信息，由spring.factories配置
 */
@Slf4j
@Configuration
public class RedisConfig {
  @Autowired
  private RedisDatabaseProperties databaseProperties;

  public static Map<Integer, RedisTemplate<String, String>> redisTemplateMap = new HashMap<>();

  @PostConstruct
  public void initRedisTemp() throws Exception {
    log.info("### S 初始化 Redis 连接池 S ###");
    int database = SandConvert.obj2Int(Config.getProperty("redis.database", "0"));
    List<RedisDatabase> databases = databaseProperties.getDatabases();
    if (CollectionUtils.isEmpty(databases)) {
      log.info("初始化 Redis 连接池 【默认库】");
      redisTemplateMap.put(database, redisTemplate(database));
    } else {
      databases.forEach(data -> {
        log.info("初始化 配置库{}【{}】", data.getDbIndex(), data.getDbName());
        try {
          redisTemplateMap.put(data.getDbIndex(), redisTemplate(data.getDbIndex()));
        } catch (Exception e) {
          log.info("初始化 配置库{}【{}】异常！", data.getDbIndex(), data.getDbName());
          e.printStackTrace();
        }
      });
    }
    log.info("### E 初始化 Redis 连接池 E ###");
  }

  /**
   * 注册工具到容器中，使用默认的数据库
   *
   * @return
   */
  @Bean
  public RedisRepository redisRepository() {
    int database = SandConvert.obj2Int(Config.getProperty("redis.database", "0"));
    RedisTemplate<String, String> redisTemplate = redisTemplateMap.get(database);
    return new RedisRepository(redisTemplate);
  }

  /**
   * 工具方法，根据需要选择不同的库
   *
   * @param dbIndex 数据库索引
   * @return
   */
  public RedisRepository getRedisRepository(int dbIndex) {
    RedisTemplate<String, String> redisTemplate = redisTemplateMap.get(dbIndex);
    return new RedisRepository(redisTemplate);
  }

  /**
   * 自定义RedisTemplate
   *
   * @param dbIndex 数据库索引
   * @return
   * @throws Exception
   */
  public RedisTemplate<String, String> redisTemplate(int dbIndex) throws Exception {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory(dbIndex, jedisPoolConfig()));
    serializer(redisTemplate);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  /**
   * Redis连接工厂
   *
   * @param dbIndex         数据库索引
   * @param jedisPoolConfig 连接池配置信息
   * @return
   */
  public RedisConnectionFactory redisConnectionFactory(int dbIndex, JedisPoolConfig jedisPoolConfig) {
    // 单机版jedis
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    // 设置redis服务器的host或者ip地址
    String hostName = Config.getProperty("redis.host");
    redisStandaloneConfiguration.setHostName(hostName);
    // 设置默认使用的数据库
    redisStandaloneConfiguration.setDatabase(dbIndex);
    // 设置密码（密码由DES加密，此处需要先解密处理）
    String password = DesCryptUtil.decrypt(Config.getProperty("redis.password"));
    redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
    // 设置redis的服务的端口号
    int port = SandConvert.obj2Int(Config.getProperty("redis.port", "6379"));
    redisStandaloneConfiguration.setPort(port);
    // 获得默认的连接池构造器
    JedisClientConfiguration.JedisPoolingClientConfigurationBuilder configurationBuilder =
        (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
    // 指定jedisPoolConifig来修改默认的连接池构造器
    configurationBuilder.poolConfig(jedisPoolConfig);
    // 通过构造器来构造jedis客户端配置
    JedisClientConfiguration jedisClientConfiguration = configurationBuilder.build();
    // 单机配置 + 客户端配置 = jedis连接工厂
    return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
  }

  /**
   * 连接池配置信息
   *
   * @return
   */
  @Bean
  public JedisPoolConfig jedisPoolConfig() {
    int minIdle = SandConvert.obj2Int(Config.getProperty("redis.pool.min-idle", "0"));
    int maxIdle = SandConvert.obj2Int(Config.getProperty("redis.pool.max-idle", "8"));
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxIdle(maxIdle);
    poolConfig.setMinIdle(minIdle);
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    poolConfig.setTestWhileIdle(true);
    poolConfig.setNumTestsPerEvictionRun(10);
    poolConfig.setTimeBetweenEvictionRunsMillis(60000);
    // 当池内没有可用的连接时，最大等待时间
    poolConfig.setMaxWaitMillis(10000);
    // TODO 其他属性
    return poolConfig;
  }

  /**
   * 序列化操作
   *
   * @param redisTemplate RedisTemplate
   */
  private void serializer(RedisTemplate<String, String> redisTemplate) {
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    // 在使用String的数据结构的时候使用这个来更改序列化方式
    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setValueSerializer(stringSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);
    redisTemplate.setHashValueSerializer(stringSerializer);
  }

}
