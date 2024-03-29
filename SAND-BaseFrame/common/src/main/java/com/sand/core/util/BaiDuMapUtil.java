package com.sand.core.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明：百度地图工具类 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2021/7/21 16:51 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Slf4j
public class BaiDuMapUtil {
    /**
     * <p>
     * 功能描述：获取百度地图经度
     * </p>
     * <pre>
     *   System.out.println(BaiDuMapUtil.getBaiDuMapLongitude("浙江省/杭州市/西湖区/三坝雅苑")); = 120.1059612010161
     * </pre>
     * 开发人员：@author hsh
     * 开发时间：2021/7/21 17:51
     * 修改记录：新建
     *
     * @param address address
     * @return double
     */
    @SuppressWarnings({"unchecked"})
    public static double getBaiDuMapLongitude(String address) {
        Map baiDuLenLet = getBaiDuMapLngAndLat(address);
        return MapUtil.getObjectValue(baiDuLenLet, "lng", Double.class);
    }

    /**
     * <p>
     * 功能描述：获取百度地图纬度
     * </p>
     * <pre>
     *   System.out.println(BaiDuMapUtil.getBaiDuMapLatitude("浙江省/杭州市/西湖区/三坝雅苑")); = 30.31159304861119
     * </pre>
     * 开发人员：@author hsh
     * 开发时间：2021/7/21 17:50
     * 修改记录：新建
     *
     * @param address address
     * @return double
     */
    @SuppressWarnings({"unchecked"})
    public static double getBaiDuMapLatitude(String address) {
        Map baiDuLenLet = getBaiDuMapLngAndLat(address);
        return MapUtil.getObjectValue(baiDuLenLet, "lat", Double.class);
    }

    /**
     * <p>
     * 功能描述：根据地址获取百度地图经、纬度
     * </p>
     * <pre>
     *   System.out.println(BaiDuMapUtil.getBaiDuMapLngAndLat("浙江省/杭州市/西湖区/三坝雅苑")); = {lng=120.1059612010161, lat=30.31159304861119}
     * </pre>
     * 开发人员：@author hsh
     * 开发时间：2021/7/21 17:50
     * 修改记录：新建
     *
     * @param address address
     * @return java.util.Map
     */
    @SuppressWarnings({"unchecked"})
    public static Map getBaiDuMapLngAndLat(String address) {
        Map baiDuLenLet = new HashMap(8);
        String baiDuMapInfo = getBaiDuMapInfo(address);
        if (StringUtils.isNotBlank(baiDuMapInfo)) {
            Map<String, Object> baiDuLenLetMap = JSONObject.parseObject(baiDuMapInfo, Map.class);
            int status = MapUtil.getIntValue(baiDuLenLetMap, "status", -1);
            if (0 == status) {
                Map resultMap = MapUtil.getObjectValue(baiDuLenLetMap, "result", Map.class);
                baiDuLenLet = MapUtil.getObjectValue(resultMap, "location", Map.class);
            }
        }
        return baiDuLenLet;
    }

    /**
     * <p>
     * 功能描述：根据地址获取百度地图信息
     * </p>
     * <pre>
     *   System.out.println(BaiDuMapUtil.getBaiDuMapInfo("浙江省/杭州市/西湖区/三坝雅苑")); = {"status":0,"result":{"location":{"lng":120.1059612010161,"lat":30.31159304861119},"precise":1,"confidence":70,"comprehension":100,"level":"地产小区"}}
     * </pre>
     * 开发人员：@author hsh
     * 开发时间：2021/7/21 17:49
     * 修改记录：新建
     *
     * @param address address
     * @return java.lang.String
     */
    public static String getBaiDuMapInfo(String address) {
        String baiDuMapInfo = StringUtils.EMPTY;
        try {
            String postData = "ak=E14c7930b62ffae7a67e6a269facfb4b&output=json&address=" + address;
            URL url = new URL("https://api.map.baidu.com/geocoder/v2/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();
            if (conn.getResponseCode() != 200) {
                System.out.println("connect failed!");
                return null;
            }
            StringBuilder result = new StringBuilder();
            String line;
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result.append(line).append("\n");
            }

            in.close();
            baiDuMapInfo = result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("地址[{}]百度地图信息获取异常", address);
        }
        log.info("地址[{}]百度地图信息：{}", address, baiDuMapInfo);
        return baiDuMapInfo;
    }

}