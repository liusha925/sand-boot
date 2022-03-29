package com.sand.mybatisplus.util;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.sand.core.util.idworker.SnowflakeIdWorker;
import org.springframework.stereotype.Component;

/**
 * 功能说明：自定义ID生成器 <br>
 * 开发人员：@author shaohua.huang <br>
 * 开发时间：2022/3/29 8:43 <br>
 * 功能描述：要求MyBatis-plus版本>=3.3.0
 * 详见https://baomidou.com/pages/568eb2/#spring-boot <br>
 */
@Component
public abstract class MybatisKeyGenerator implements IdentifierGenerator {

    // /**
    //  * 方式一：采用自定义雪花算法
    //  *
    //  * @param entity entity
    //  * @return 唯一ID
    //  */
    // @Override
    // public Number nextId(Object entity) {
    //     return new SnowflakeIdWorker().nextId();
    // }

    /**
     * 方式二：使用Mybatis-plus自带生成器
     *
     * @param entity entity
     * @return 唯一ID
     */
    @Override
    public Number nextId(Object entity) {
        return new DefaultIdentifierGenerator().nextId(entity);
    }
}
