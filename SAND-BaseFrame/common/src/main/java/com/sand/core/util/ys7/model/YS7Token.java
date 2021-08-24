package com.sand.core.util.ys7.model;

/**
 * 功能说明：萤石令牌信息 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/8/23 18:03 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class YS7Token {
    /**
     * 令牌信息
     */
    private String accessToken;
    /**
     * 具体过期时间，精确到毫秒
     */
    private long expireTime;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
