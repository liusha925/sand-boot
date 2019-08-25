/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.http;

import com.alibaba.fastjson.JSON;
import com.sand.base.exception.LsException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 功能说明：OkHttp3工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 15:27
 * 功能描述：使用okhttp3共享同一个socket，通过连接池来减小响应延迟
 */
@Slf4j
public class OkHttp3Util {

  public static final String HEADER_PREFIX = "Header:";

  public OkHttp3Util() {
  }

  private static OkHttpClient client = new OkHttpClient.Builder()
      .connectTimeout(100, TimeUnit.SECONDS)
      .writeTimeout(100, TimeUnit.SECONDS)
      .readTimeout(100, TimeUnit.SECONDS)
      .build();

  /**
   * @param url      下载连接
   * @param saveDir  储存下载文件的SDCard目录
   * @param listener 下载监听，模拟下载进度条
   */
  public static synchronized String download(final String url, final String saveDir, final FileDownloadListener listener) {
    Request request = new Request.Builder().url(url).build();
    String fileName = null;
    try {
      Response response = client.newCall(request).execute();
      InputStream is = null;
      byte[] buf = new byte[2048];
      int len = 0;
      FileOutputStream fos = null;
      // 储存下载文件的目录
      String savePath = isExistDir(saveDir);
      try {
        String fileInfo = response.header("Content-Disposition");
        if (Objects.isNull(fileInfo)) {
          throw new LsException(response.body().string());
        }
        fileName = URLDecoder.decode(fileInfo.split("=")[1], "utf-8");
        is = response.body().byteStream();
        // 监听接口处理文件名
        long total = response.body().contentLength();
        String modifiedFileName = listener.start(fileName, total);
        File file = new File(savePath, modifiedFileName);
        fos = new FileOutputStream(file);
        long sum = 0;
        while ((len = is.read(buf)) != -1) {
          fos.write(buf, 0, len);
          sum += len;
          int progress = (int) (sum * 1.0f / total * 100);
          // 下载中
          listener.downloading(progress);
        }
        fos.flush();
        // 下载完成
        listener.success(savePath, modifiedFileName);
      } catch (Exception e) {
        listener.fail(e);
      } finally {
        try {
          if (is != null) {
            is.close();
          }
        } catch (IOException e) {
        }
        try {
          if (fos != null) {
            fos.close();
          }
        } catch (IOException e) {
        }
      }
    } catch (IOException e) {
      listener.fail(e);
    }
    return fileName;
  }

  /**
   * 使用MultipartBody同时传递键值对参数和File对象
   *
   * @param url
   * @param map
   * @param file
   * @return
   */
  public static String httpPostMultipartBody(String url, Map<String, Object> map, File file) {
    Headers headers = handleHeaders(map);
    MultipartBody.Builder builder = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", file.getName(), okhttp3.RequestBody.create(MediaType.parse("file/*"), file));
    for (Map.Entry entry : map.entrySet()) {
      if (!entry.getKey().toString().startsWith(HEADER_PREFIX)) {
        builder.addFormDataPart(entry.getKey() + "", entry.getValue() + "");
      }
    }
    final Request request = new Request.Builder().url(url).post(builder.build()).headers(headers).build();
    return proceedRequest(request);
  }

  /**
   * get请求
   *
   * @param url
   * @return
   */
  public static String httpGetJson(String url) {
    return httpGetJson(url, new HashMap<>());
  }

  /**
   * get请求
   *
   * @param url
   * @param map
   * @return
   */
  public static String httpGetJson(String url, Map<String, Object> map) {
    Headers headers = handleHeaders(map);
    return httpGetJson(url, headers);
  }

  /**
   * get请求
   *
   * @param url
   * @param headers
   * @return
   */
  public static String httpGetJson(String url, Headers headers) {
    Request request = new Request.Builder().url(url).get().headers(headers).build();
    return proceedRequest(request);
  }

  /**
   * post请求
   *
   * @param url
   * @param map
   * @return
   */
  public static String httpPostJson(String url, Map<String, Object> map) {
    Headers headers = handleHeaders(map);
    return httpPostJson(url, JSON.toJSONString(map), headers);
  }

  /**
   * post请求
   *
   * @param url
   * @param jsonStr
   * @param headers
   * @return
   */
  public static String httpPostJson(String url, String jsonStr, Headers headers) {
    log.info("url:" + url + " 参数：" + jsonStr);
    MediaType JSON = MediaType.parse("application/json; charset=UTF-8");
    okhttp3.RequestBody body = okhttp3.RequestBody.create(JSON, jsonStr);
    Request request = new Request.Builder().url(url).post(body).headers(headers).build();
    return proceedRequest(request);
  }

  /**
   * 同步请求
   *
   * @param request
   * @return
   */
  public static String proceedRequest(Request request) {
    String res = "";
    try (okhttp3.Response response = client.newCall(request).execute()) {
      okhttp3.ResponseBody body = response.body();
      if (response.isSuccessful()) {
        res = body.string();
      }
    } catch (IOException e) {
      log.error("okHttp3请求异常", e);
      throw new LsException("okHttp3请求异常", e);
    }
    return res;
  }

  /**
   * 设置头部信息
   *
   * @param map
   * @return
   */
  public static Headers handleHeaders(Map<String, Object> map) {
    Headers.Builder headers = new Headers.Builder();
    if (Objects.nonNull(map)) {
      for (Map.Entry entry : map.entrySet()) {
        String key = entry.getKey().toString();
        if (key.startsWith(HEADER_PREFIX)) {
          headers.add(key.replace(HEADER_PREFIX, ""), entry.getValue().toString());
        }
      }
    }
    return headers.build();
  }


  /**
   * @param saveDir
   * @return
   * @throws IOException 判断下载目录是否存在
   */
  private static String isExistDir(String saveDir) throws IOException {
    // 下载位置
    File downloadFile = new File(getServicePath(), saveDir);
    if (!downloadFile.mkdirs()) {
      downloadFile.createNewFile();
    }
    String savePath = downloadFile.getAbsolutePath();
    return savePath;
  }

  /**
   * 获取服务路径
   */
  public static String getServicePath() {
    String servicePath = null;
    try {
      File pathFile = new File(ResourceUtils.getURL("").getPath());
      servicePath = pathFile.getAbsolutePath();
    } catch (FileNotFoundException e) {
      log.error("获取服务路径错误");
    }
    return servicePath;
  }

  /**
   * @param url
   * @return 从下载连接中解析出文件名
   */
  private static String getNameFromUrl(String url) {
    return url.substring(url.lastIndexOf("/") + 1);
  }

  public interface FileDownloadListener {
    /**
     * 下载开始
     *
     * @param fileName
     * @param length
     * @return
     */
    String start(String fileName, long length);

    /**
     * 下载失败
     *
     * @param e
     */
    void fail(Exception e);

    /**
     * 下载中
     *
     * @param progress
     */
    void downloading(int progress);

    /**
     * 下载成功
     *
     * @param savePath
     * @param fileName
     */
    void success(String savePath, String fileName);

  }

}
