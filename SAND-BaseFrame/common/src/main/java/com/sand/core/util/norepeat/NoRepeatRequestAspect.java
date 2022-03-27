package com.sand.core.util.norepeat;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能说明：防重复请求 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/3/27/027 21:16 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Aspect
@Slf4j
@Component
public class NoRepeatRequestAspect {
    @Autowired
    private Cache<Object, Object> repeatCache;

    @Pointcut("@annotation(com.sand.core.util.norepeat.NoRepeatRequest)")
    public void noRepeat() {
    }

    @Around("noRepeat()")
    public Object repeatRequestCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        Object[] args = joinPoint.getArgs();
        if (ArrayUtil.isNotEmpty(args)) {
            Object key = args[0];
            log.info("防重复请求缓存实时数量：{}", repeatCache.size());
            if (repeatCache.getIfPresent(key) == null) {
                repeatCache.put(key, System.currentTimeMillis());
                result = joinPoint.proceed();
            } else {
                throw new Exception("请勿重复提交！");
            }
        } else {
            result = joinPoint.proceed();
        }

        return result;
    }
}
