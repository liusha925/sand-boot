package com.sand.core.util.norepeat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 功能说明：缓存配置 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/3/27/027 21:14 <br>
 * 功能描述：使用谷歌的guava <br>
 */
@Configuration
public class CacheConfig {
    @Bean
    public Cache<Object, Object> repeatCache() {
        final Cache<Object, Object> repeatCache = CacheBuilder.newBuilder()
                // cache的初始大小
                .initialCapacity(20)
                // 并发数
                .concurrencyLevel(10)
                // cache中的数据在写入之后的存活时间为3秒
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build();
        return repeatCache;
    }
}
