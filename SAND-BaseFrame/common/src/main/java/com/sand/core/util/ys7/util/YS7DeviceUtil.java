package com.sand.core.util.ys7.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sand.core.util.ys7.enums.YS7Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：萤石设备工具类 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/8/25 19:31 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class YS7DeviceUtil extends YS7TokenUtil {
    /**
     * 萤石设备请求地址
     */
    public static final String DEVICE_URL = "https://open.ys7.com/api/lapp/device";

    /**
     * 设备抓拍图片
     * <p>
     * 功能描述：抓拍设备当前画面，该接口仅适用于IPC或者关联IPC的DVR设备，该接口并非预览时的截图功能。海康型号设备可能不支持萤石协议抓拍功能，使用该接口可能返回不支持或者超时
     * 参考地址：https://open.ys7.com/doc/zh/book/index/device_option.html#device_option-api4
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/25 19:38
     * 修改记录：新建
     *
     * @param appKey       appKey
     * @param appSecret    appSecret
     * @param deviceSerial 设备序列号,存在英文字母的设备序列号，字母需为大写
     * @param channelNo    通道号，IPC设备填1
     * @return 抓拍后的图片路径，图片保存有效期为2小时
     * @throws Exception java.lang.Exception
     */
    public static String getCapture(String appKey, String appSecret, String deviceSerial, int channelNo) throws Exception {
        String accessToken = YS7TokenUtil.getAccessToken(appKey, appSecret);
        return getCapture(accessToken, deviceSerial, channelNo);
    }

    /**
     * 设备抓拍图片
     * <p>
     * 功能描述：抓拍设备当前画面，该接口仅适用于IPC或者关联IPC的DVR设备，该接口并非预览时的截图功能。海康型号设备可能不支持萤石协议抓拍功能，使用该接口可能返回不支持或者超时
     * 参考地址：https://open.ys7.com/doc/zh/book/index/device_option.html#device_option-api4
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/25 19:38
     * 修改记录：新建
     *
     * @param accessToken  授权过程获取的access_token
     * @param deviceSerial 设备序列号,存在英文字母的设备序列号，字母需为大写
     * @param channelNo    通道号，IPC设备填1
     * @return 抓拍后的图片路径，图片保存有效期为2小时
     * @throws Exception java.lang.Exception
     */
    @SuppressWarnings({"unchecked"})
    public static String getCapture(String accessToken, String deviceSerial, int channelNo) throws Exception {
        Map<String, Object> param = new HashMap<>(4);
        param.put("accessToken", accessToken);
        param.put("deviceSerial", deviceSerial);
        param.put("channelNo", channelNo);
        String retStr = HttpUtil.post(DEVICE_URL + "/capture", param);
        JSONObject retJson = JSONUtil.parseObj(retStr);
        int code = retJson.getInt("code");
        if (YS7Status.CODE_200.getCode() == code) {
            String retDataStr = retJson.getStr("data");
            Map<String, Object> retMap = JSONUtil.toBean(retDataStr, Map.class);
            return MapUtil.getStr(retMap, "picUrl");
        } else {
            String msg = retJson.getStr("msg");
            throw new Exception("萤石【设备抓拍图片】失败：" + msg);
        }
    }

    /**
     * 获取摄像头列表
     * <p>
     * 功能描述：获取监控点列表
     * 参考地址：https://open.ys7.com/doc/zh/book/index/device_select.html#device_select-api3
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/31 15:32
     * 修改记录：新建
     *
     * @param appKey    appKey
     * @param appSecret appSecret
     * @return 监控点列表
     * @throws Exception java.lang.Exception
     */
    public static List<Map> getCameraList(String appKey, String appSecret) throws Exception {
        return getCameraList(YS7TokenUtil.getAccessToken(appKey, appSecret));
    }

    /**
     * 获取摄像头列表
     * <p>
     * 功能描述：获取监控点列表
     * 参考地址：https://open.ys7.com/doc/zh/book/index/device_select.html#device_select-api3
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/31 15:32
     * 修改记录：新建
     *
     * @param accessToken 授权过程获取的 access_token
     * @return 监控点列表
     * @throws Exception java.lang.Exception
     */
    public static List<Map> getCameraList(String accessToken) throws Exception {
        return getCameraList(accessToken, 0, 50);
    }

    /**
     * 获取摄像头列表
     * <p>
     * 功能描述：获取监控点列表
     * 参考地址：https://open.ys7.com/doc/zh/book/index/device_select.html#device_select-api3
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/31 15:32
     * 修改记录：新建
     *
     * @param accessToken 授权过程获取的 access_token
     * @param pageStart   分页起始页，从0开始
     * @param pageSize    分页大小，默认为10，最大为50
     * @return 监控点列表
     * @throws Exception java.lang.Exception
     */
    public static List<Map> getCameraList(String accessToken, int pageStart, int pageSize) throws Exception {
        Map<String, Object> param = new HashMap<>(4);
        param.put("accessToken", accessToken);
        param.put("pageStart", pageStart);
        param.put("pageSize", pageSize);
        String retStr = HttpUtil.post("https://open.ys7.com/api/lapp/camera/list", param);

        JSONObject retJson = JSONUtil.parseObj(retStr);
        int code = retJson.getInt("code");
        if (YS7Status.CODE_200.getCode() == code) {
            String retDataStr = retJson.getStr("data");
            List<Map> resultList = JSONUtil.toList(retDataStr, Map.class);
            int totalPageNum = getTotalPageNum(retJson);
            // 页面起止从0开始，所以(page+1)处理
            int start = (pageStart + 1);
            if (start < totalPageNum) {
                resultList.addAll(getCameraList(accessToken, start, pageSize));
            }

            return resultList;
        } else {
            String msg = retJson.getStr("msg");
            throw new Exception("萤石【获取摄像头列表】失败：" + msg);
        }
    }

    public static void main(String[] args) throws Exception {
        final List<Map> cameraList = YS7DeviceUtil.getCameraList("6e853c15e196429d8e68efb217a64766", "83f144ae193630cf11a56c34e16172b4");
        System.out.println(cameraList);
    }
}
