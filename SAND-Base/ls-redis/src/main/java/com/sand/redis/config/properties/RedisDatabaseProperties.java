/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/7    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明：数据源配置
 * 开发人员：@author liusha
 * 开发日期：2020/4/7 16:26
 * 功能描述：数据源配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sand.redis")
public class RedisDatabaseProperties {
  private List<RedisDatabase> databases = new ArrayList<>();
}
