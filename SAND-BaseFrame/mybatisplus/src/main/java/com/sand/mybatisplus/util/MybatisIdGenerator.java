package com.sand.mybatisplus.util;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 功能说明：自定义分布式下ID生成器 <br>
 * 开发人员：@author shaohua.huang <br>
 * 开发时间：2022/3/29 8:43 <br>
 * 功能描述：要求MyBatis-plus版本>=3.3.0
 * 详见https://baomidou.com/pages/568eb2/#spring-boot <br>
 */
@Component
public class MybatisIdGenerator {
    @Bean
    public DefaultIdentifierGenerator defaultIdentifierGenerator() {
        return new DefaultIdentifierGenerator();
    }

    @Autowired
    private DefaultIdentifierGenerator defaultIdentifierGenerator;

    /**
     * <p>
     * 功能描述：生成唯一ID
     * </p>
     * 开发人员：@author shaohua.huang
     * 开发时间：2022/3/29 13:47
     * 修改记录：新建
     * 使用说明：
     * @Autowired
     * private MybatisIdGenerator mybatisIdGenerator;
     * mybatisIdGenerator.nextId()
     *
     * @return java.lang.Number 唯一ID
     */
    public Number nextId() {
        return defaultIdentifierGenerator.nextId(null);
    }
}
