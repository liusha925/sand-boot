package com.sand.redis.util.norepeat;

/**
 * 功能说明：防重复请求常量类 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/3/27/027 21:39 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public interface NoRepeatRequestConstant {
    /**
     * UUID
     */
    String UUID = "UUID";
    /**
     * 防重复请求标识
     */
    String NO_REPEAT_REQUEST_PREFIX = "NO_REPEAT_REQUEST_PREFIX:";
}
