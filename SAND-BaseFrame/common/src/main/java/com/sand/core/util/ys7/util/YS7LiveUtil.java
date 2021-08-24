package com.sand.core.util.ys7.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sand.core.util.ys7.enums.YS7Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：萤石直播工具类 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/8/23 18:00 <br>
 * 功能描述：参考文档 https://open.ys7.com/doc/zh <br>
 */
public class YS7LiveUtil {
    /**
     * 萤石直播请求地址
     */
    public static final String LIVE_URL = "https://open.ys7.com/api/lapp/live";
    /**
     * [获取用户下直播视频列表，获取指定有效期的直播地址，开通直播功能，获取直播地址、关闭直播功能]
     */
    public static final String[] URL = {"/video/list", "/address/limited", "/video/open", "/address/get", "/video/close"};

    /**
     * 获取用户下直播视频列表
     * <p>
     * 功能描述：该接口适用于已经开通过直播的用户，用以获取账号下的视频地址列表
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api1
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 9:05
     * 修改记录：新建
     *
     * @param appKey    appKey
     * @param appSecret appSecret
     * @return 直播视频列表
     * @throws java.lang.Exception java.lang.Exception
     */
    public static List<Map> getVideoList(String appKey, String appSecret) throws Exception {
        return getVideoList(YS7TokenUtil.getAccessToken(appKey, appSecret));
    }

    /**
     * 获取用户下直播视频列表
     * <p>
     * 功能描述：该接口适用于已经开通过直播的用户，用以获取账号下的视频地址列表
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api1
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 8:37
     * 修改记录：新建
     *
     * @param accessToken 授权过程获取的 access_token
     * @return 直播视频列表
     * @throws java.lang.Exception java.lang.Exception
     */
    public static List<Map> getVideoList(String accessToken) throws Exception {
        return getVideoList(accessToken, 0, 50);
    }

    /**
     * 获取用户下直播视频列表
     * <p>
     * 功能描述：该接口适用于已经开通过直播的用户，用以获取账号下的视频地址列表
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api1
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 8:37
     * 修改记录：新建
     *
     * @param accessToken 授权过程获取的 access_token
     * @param pageStart   分页起始页，从0开始
     * @param pageSize    分页大小，默认为10，最大为50
     * @return 直播视频列表
     * @throws java.lang.Exception java.lang.Exception
     */
    public static List<Map> getVideoList(String accessToken, int pageStart, int pageSize) throws Exception {
        Map<String, Object> param = new HashMap<>(4);
        param.put("accessToken", accessToken);
        param.put("pageStart", pageStart);
        param.put("pageSize", pageSize);
        String retStr = HttpUtil.post(LIVE_URL + URL[0], param);
        JSONObject retJson = JSONUtil.parseObj(retStr);
        int code = retJson.getInt("code");
        if (YS7Status.CODE_200.getCode() == code) {
            String retDataStr = retJson.getStr("data");
            return JSONUtil.toList(retDataStr, Map.class);
        } else {
            String msg = retJson.getStr("msg");
            throw new Exception("萤石【获取用户下直播视频列表】失败：" + msg);
        }
    }

    /**
     * 获取指定有效期的直播地址
     * <p>
     * 功能描述：该接口适用于已经开通过直播的用户，用以根据设备序列号和通道号获取指定有效期的直播地址，可用于防盗链
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api2
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:00
     * 修改记录：新建
     *
     * @param appKey       appKey
     * @param appSecret    appSecret
     * @param deviceSerial 设备序列号,存在英文字母的设备序列号，字母需为大写
     * @param channelNo    通道号，IPC设备填1
     * @return 指定有效期的直播地址
     * @throws java.lang.Exception java.lang.Exception
     */
    public static Map getLimitedAddress(String appKey, String appSecret, String deviceSerial, int channelNo) throws Exception {
        String accessToken = YS7TokenUtil.getAccessToken(appKey, appSecret);
        return getLimitedAddress(accessToken, deviceSerial, channelNo);
    }

    /**
     * 获取指定有效期的直播地址
     * <p>
     * 功能描述：该接口适用于已经开通过直播的用户，用以根据设备序列号和通道号获取指定有效期的直播地址，可用于防盗链
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api2
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:00
     * 修改记录：新建
     *
     * @param accessToken  授权过程获取的access_token
     * @param deviceSerial 设备序列号,存在英文字母的设备序列号，字母需为大写
     * @param channelNo    通道号，IPC设备填1
     * @return 指定有效期的直播地址
     * @throws java.lang.Exception java.lang.Exception
     */
    public static Map getLimitedAddress(String accessToken, String deviceSerial, int channelNo) throws Exception {
        return getLimitedAddress(accessToken, deviceSerial, channelNo, null);
    }

    /**
     * 获取指定有效期的直播地址
     * <p>
     * 功能描述：该接口适用于已经开通过直播的用户，用以根据设备序列号和通道号获取指定有效期的直播地址，可用于防盗链
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api2
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:00
     * 修改记录：新建
     *
     * @param accessToken  授权过程获取的access_token
     * @param deviceSerial 设备序列号,存在英文字母的设备序列号，字母需为大写
     * @param channelNo    通道号，IPC设备填1
     * @param expireTime   地址过期时间：单位秒数，最大默认62208000（即720天），最小默认300（即5分钟）。非必选参数，为空时返回对应设备和通道的永久地址
     * @return 指定有效期的直播地址
     * @throws java.lang.Exception java.lang.Exception
     */
    public static Map getLimitedAddress(String accessToken, String deviceSerial, int channelNo, Integer expireTime) throws Exception {
        Map<String, Object> param = new HashMap<>(8);
        param.put("accessToken", accessToken);
        param.put("deviceSerial", deviceSerial);
        param.put("channelNo", channelNo);
        param.put("expireTime", expireTime);
        String retStr = HttpUtil.post(LIVE_URL + URL[1], param);
        JSONObject retJson = JSONUtil.parseObj(retStr);
        int code = retJson.getInt("code");
        if (YS7Status.CODE_200.getCode() == code) {
            String retDataStr = retJson.getStr("data");
            return JSONUtil.toBean(retDataStr, Map.class);
        } else {
            String msg = retJson.getStr("msg");
            throw new Exception("萤石【获取指定有效期的直播地址】失败：" + msg);
        }
    }

    /**
     * 开通直播功能
     * <p>
     * 功能描述：该接口用于根据序列号和通道号批量开通直播功能（只支持可观看视频的设备）
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api3
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:28
     * 修改记录：新建
     *
     * @param appKey    appKey
     * @param appSecret appSecret
     * @param source    直播源，[设备序列号]:[通道号],[设备序列号]:[通道号]的形式，例如427734222:1,423344555:3，均采用英文符号，限制50个
     * @return 开通结果列表
     * @throws java.lang.Exception java.lang.Exception
     */
    public static List<Map> openVideo(String appKey, String appSecret, String source) throws Exception {
        String accessToken = YS7TokenUtil.getAccessToken(appKey, appSecret);
        return openVideo(accessToken, source);
    }

    /**
     * 开通直播功能
     * <p>
     * 功能描述：该接口用于根据序列号和通道号批量开通直播功能（只支持可观看视频的设备）
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api3
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:28
     * 修改记录：新建
     *
     * @param accessToken 授权过程获取的access_token
     * @param source      直播源，[设备序列号]:[通道号],[设备序列号]:[通道号]的形式，例如427734222:1,423344555:3，均采用英文符号，限制50个
     * @return 开通结果列表
     * @throws java.lang.Exception java.lang.Exception
     */
    public static List<Map> openVideo(String accessToken, String source) throws Exception {
        return getLiveResultList(accessToken, source, LIVE_URL + URL[2]);
    }

    /**
     * 获取直播地址
     * <p>
     * 功能描述：该接口用于根据序列号和通道号批量获取设备的直播地址信息，开通直播后才可获取直播地址 该接口获取直播地址固定不变,永久有效
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api4
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:28
     * 修改记录：新建
     *
     * @param appKey    appKey
     * @param appSecret appSecret
     * @param source    直播源，[设备序列号]:[通道号],[设备序列号]:[通道号]的形式，例如427734222:1,423344555:3，均采用英文符号，限制50个
     * @return 开通结果列表
     * @throws java.lang.Exception java.lang.Exception
     */
    public static List<Map> getAddress(String appKey, String appSecret, String source) throws Exception {
        String accessToken = YS7TokenUtil.getAccessToken(appKey, appSecret);
        return getAddress(accessToken, source);
    }

    /**
     * 获取直播地址
     * <p>
     * 功能描述：该接口用于根据序列号和通道号批量获取设备的直播地址信息，开通直播后才可获取直播地址 该接口获取直播地址固定不变,永久有效
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api4
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:28
     * 修改记录：新建
     *
     * @param accessToken 授权过程获取的access_token
     * @param source      直播源，[设备序列号]:[通道号],[设备序列号]:[通道号]的形式，例如427734222:1,423344555:3，均采用英文符号，限制50个
     * @return 开通结果列表
     * @throws java.lang.Exception java.lang.Exception
     */
    public static List<Map> getAddress(String accessToken, String source) throws Exception {
        return getLiveResultList(accessToken, source, LIVE_URL + URL[3]);
    }

    /**
     * 关闭直播功能
     * <p>
     * 功能描述：该接口用于根据序列号和通道号批量关闭直播功能。
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api5
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:28
     * 修改记录：新建
     *
     * @param appKey    appKey
     * @param appSecret appSecret
     * @param source    直播源，[设备序列号]:[通道号],[设备序列号]:[通道号]的形式，例如427734222:1,423344555:3，均采用英文符号，限制50个
     * @return 开通结果列表
     * @throws java.lang.Exception java.lang.Exception
     */
    public static List<Map> closeVideo(String appKey, String appSecret, String source) throws Exception {
        String accessToken = YS7TokenUtil.getAccessToken(appKey, appSecret);
        return openVideo(accessToken, source);
    }

    /**
     * 关闭直播功能
     * <p>
     * 功能描述：该接口用于根据序列号和通道号批量关闭直播功能。
     * 参考地址：https://open.ys7.com/doc/zh/book/index/address.html#address-api5
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:28
     * 修改记录：新建
     *
     * @param accessToken 授权过程获取的access_token
     * @param source      直播源，[设备序列号]:[通道号],[设备序列号]:[通道号]的形式，例如427734222:1,423344555:3，均采用英文符号，限制50个
     * @return 开通结果列表
     * @throws java.lang.Exception java.lang.Exception
     */
    public static List<Map> closeVideo(String accessToken, String source) throws Exception {
        return getLiveResultList(accessToken, source, LIVE_URL + URL[4]);
    }

    /**
     * 获取直播结果列表
     * <p>
     * 功能描述：封装通用方法调用
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/24 10:59
     * 修改记录：新建
     *
     * @param accessToken accessToken
     * @param source      source
     * @param url         url
     * @return 直播结果列表
     * @throws java.lang.Exception java.lang.Exception
     */
    private static List<Map> getLiveResultList(String accessToken, String source, String url) throws Exception {
        Map<String, Object> param = new HashMap<>(4);
        param.put("accessToken", accessToken);
        param.put("source", source);
        String retStr = HttpUtil.post(url, param);
        JSONObject retJson = JSONUtil.parseObj(retStr);
        int code = retJson.getInt("code");
        if (YS7Status.CODE_200.getCode() == code) {
            String retDataStr = retJson.getStr("data");
            return JSONUtil.toList(retDataStr, Map.class);
        } else {
            String msg = retJson.getStr("msg");
            throw new Exception(url + "萤石【获取直播结果】失败：" + msg);
        }
    }

}
