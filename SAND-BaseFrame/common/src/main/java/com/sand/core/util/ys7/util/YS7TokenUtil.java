package com.sand.core.util.ys7.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sand.core.util.ys7.enums.YS7Status;
import com.sand.core.util.ys7.model.YS7Token;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能说明：萤石Token工具类 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/8/24 10:39 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class YS7TokenUtil {
    /**
     * 锁处理
     */
    public static Lock tokenLock = new ReentrantLock();
    /**
     * 缓存Token
     */
    public static Map<String, YS7Token> tokenCache = new HashMap<>();
    /**
     * 萤石请求地址
     */
    public static final String TOKEN_URL = "https://open.ys7.com/api/lapp/token/get";

    /**
     * <p>
     * 功能描述：获取accessToken
     * 文档地址：https://open.ys7.com/doc/zh/book/index/user.html
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/23 18:08
     * 修改记录：新建
     *
     * @param appKey    appKey
     * @param appSecret appSecret
     * @return 授权accessToken
     * @throws java.lang.Exception java.lang.Exception
     */
    public static String getAccessToken(String appKey, String appSecret) throws Exception {
        return getToken(appKey, appSecret).getAccessToken();
    }

    /**
     * <p>
     * 功能描述：获取Token
     * 文档地址：https://open.ys7.com/doc/zh/book/index/user.html
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/23 18:08
     * 修改记录：新建
     *
     * @param appKey    appKey
     * @param appSecret appSecret
     * @return 授权Token
     * @throws java.lang.Exception java.lang.Exception
     */
    public static YS7Token getToken(String appKey, String appSecret) throws Exception {
        tokenLock.lock();
        try {
            YS7Token ys7Token = tokenCache.get(appKey);
            if (null != ys7Token) {
                // 在有效期內
                if (ys7Token.getExpireTime() - System.currentTimeMillis() > 0) {
                    return ys7Token;
                }
            }
            Map<String, Object> param = new HashMap<>(4);
            param.put("appKey", appKey);
            param.put("appSecret", appSecret);
            String retStr = HttpUtil.post(TOKEN_URL, param);
            JSONObject retJson = JSONUtil.parseObj(retStr);
            int code = retJson.getInt("code");
            if (YS7Status.CODE_200.getCode() == code) {
                String retDataStr = retJson.getStr("data");
                ys7Token = JSONUtil.toBean(retDataStr, YS7Token.class);
                tokenCache.put(appKey, ys7Token);
                return ys7Token;
            } else {
                String msg = retJson.getStr("msg");
                throw new Exception("萤石【获取accessToken】失败：" + msg);
            }
        } finally {
            tokenLock.unlock();
        }
    }
}
