package com.sand.redis.util.norepeat;

import com.alibaba.fastjson.JSONObject;
import com.sand.core.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 功能说明：防重复提交切面类 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/3/27/027 21:35 <br>
 * 功能描述：根据Header中的唯一标识判断请求是否重复 <br>
 */
@Aspect
@Component
public class NoRepeatRequestAspect {
    private RedisTemplate redisTemplate;
    /**
     * <p>
     * 功能描述：进行序列化（防止key乱码）
     * </p>
     * 开发人员：@author shaohua.huang
     * 开发时间：2022/3/29 16:39
     * 修改记录：新建
     *
     * @param redisTemplate redisTemplate
     */
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        // 解决key的序列化问题
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // 解决value的序列化问题
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.sand.redis.util.norepeat.NoRepeatRequest)")
    public void noRepeatRequest() {
    }

    /**
     * <p>
     * 功能描述：环绕通知 （可以控制目标方法前中后期执行操作，目标方法执行前后分别执行一些代码）
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2022/3/27/027 21:56
     * 修改记录：新建
     *
     * @param joinPoint joinPoint
     * @return java.lang.Object
     * @throws Exception java.lang.Throwable
     */
    @Around("noRepeatRequest()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Assert.notNull(request, "request cannot be null.");

        // 获取执行方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 获取防重复提交注解
        NoRepeatRequest annotation = method.getAnnotation(NoRepeatRequest.class);

        // 获取UUID以及方法标记，生成redisKey和redisValue
        String uuid = request.getHeader(NoRepeatRequestConstant.UUID);
        String redisKey = NoRepeatRequestConstant.NO_REPEAT_REQUEST_PREFIX
                .concat(uuid).concat("$")
                .concat(getMethodSign(method, joinPoint.getArgs()));
        String redisValue = redisKey.concat(annotation.value()).concat(" submit duplication");

        if (!redisTemplate.hasKey(redisKey)) {
            // 设置防重复请求限时标记（前置通知）
            redisTemplate.opsForValue().set(redisKey, redisValue, annotation.expireSeconds(), TimeUnit.SECONDS);
            try {
                // 正常执行方法并返回，ProceedingJoinPoint类型参数可以决定是否执行目标方法，且环绕通知必须要有返回值，返回值即为目标方法的返回值
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                // 确保方法执行异常实时释放限时标记（异常后置通知）
                redisTemplate.delete(redisKey);
                throw new Exception(throwable);
            }
        } else {
            throw new BusinessException("请勿重复提交！");
        }
    }

    /**
     * <p>
     * 功能描述：生成方法标记：采用数字签名算法SHA1对方法签名字符串加签
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2022/3/27/027 21:49
     * 修改记录：新建
     *
     * @param method method
     * @param args   args
     * @return java.lang.String 40位的签名串，例：30c9b14ee8b282f12009e0b2b06542145f263e4f
     */
    private String getMethodSign(Method method, Object... args) {
        String sb = Arrays.stream(args)
                .map(this::toString)
                .collect(Collectors.joining("", method.toString(), ""));
        return DigestUtils.sha1DigestAsHex(sb);
    }

    /**
     * <p>
     * 功能描述：转string
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2022/3/27/027 21:46
     * 修改记录：新建
     *
     * @param arg arg
     * @return java.lang.String
     */
    private String toString(Object arg) {
        if (Objects.isNull(arg)) {
            return "null";
        }
        if (arg instanceof Number) {
            return arg.toString();
        }
        return JSONObject.toJSONString(arg);
    }

}
