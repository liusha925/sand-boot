/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/6   liusha      新增
 * =========  ===========  =====================
 */
package com.sand.base.util;

import com.alibaba.fastjson.JSONObject;
import com.sand.base.constant.Constant;
import com.sand.base.util.text.LsCharset;
import com.sand.base.util.lang3.StringUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明：servlet工具类
 * 开发人员：@author liusha <br>
 * 开发时间：2019/8/6 21:52 <br>
 * 功能描述：servlet工具类 <br>
 */
@Slf4j
public class ServletUtil {
  /**
   * 操作系统
   **/
  public static final String OS = "os";
  /**
   * 浏览器
   **/
  public static final String BROWSER = "browser";

  public ServletUtil() {
  }

  /**
   * 获取request
   *
   * @return
   */
  public static HttpServletRequest getRequest() {
    return getRequestAttributes().getRequest();
  }

  /**
   * 获取response
   *
   * @return
   */
  public static HttpServletResponse getResponse() {
    return getRequestAttributes().getResponse();
  }

  /**
   * 获取session
   *
   * @return
   */
  public static HttpSession getSession() {
    return getRequest().getSession();
  }

  public static ServletRequestAttributes getRequestAttributes() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    return (ServletRequestAttributes) attributes;
  }

  /**
   * 获取请求参数
   *
   * @return
   */
  public static String getRequestParams() {
    return getRequestParams(getRequest());
  }

  /**
   * 获取String参数
   *
   * @param name
   * @return
   */
  public static String getParameter(String name) {
    return getRequest().getParameter(name);
  }

  /**
   * 获取所有的请求参数
   *
   * @param request
   * @return
   */
  public static String getRequestParams(HttpServletRequest request) {
    String paraName;
    Enumeration enu = request.getParameterNames();
    JSONObject object = new JSONObject();
    while (enu.hasMoreElements()) {
      paraName = enu.nextElement().toString();
      object.put(paraName, request.getParameter(paraName));
    }
    return object.toJSONString();
  }

  /**
   * 获取真实用户IP
   *
   * @return
   */
  public static String getIp() {
    return getIp(getRequest());
  }

  /**
   * 获取真实用户IP
   *
   * @return
   */
  public static String getIp(HttpServletRequest request) {
    String addIp = null;
    // X-Forwarded-For：Squid 服务代理
    String ipAddresses = request.getHeader("X-Forwarded-For");
    if (ipAddresses == null || ipAddresses.length() == 0 || ipAddresses.equalsIgnoreCase(Constant.UNKNOWN)) {
      // Proxy-Client-IP：apache 服务代理
      ipAddresses = request.getHeader("Proxy-Client-IP");
    }
    if (ipAddresses == null || ipAddresses.length() == 0 || ipAddresses.equalsIgnoreCase(Constant.UNKNOWN)) {
      // WL-Proxy-Client-IP：webLogic 服务代理
      ipAddresses = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ipAddresses == null || ipAddresses.length() == 0 || ipAddresses.equalsIgnoreCase(Constant.UNKNOWN)) {
      // HTTP_CLIENT_IP：有些代理服务器
      ipAddresses = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ipAddresses == null || ipAddresses.length() == 0 || ipAddresses.equalsIgnoreCase(Constant.UNKNOWN)) {
      // X-Real-IP：nginx服务代理
      ipAddresses = request.getHeader("X-Real-IP");
    }
    // 有些网络通过多层代理，就会获取到多个IP，一般逗号（,）分割并且第一个IP为客户端的真实IP
    if (ipAddresses != null && ipAddresses.length() != 0) {
      addIp = ipAddresses.split(",")[0];
    }
    // 以上都获取不到就从"Remote Address中"获取
    if (addIp == null || addIp.length() == 0 || ipAddresses.equalsIgnoreCase(Constant.UNKNOWN)) {
      addIp = request.getRemoteAddr();
    }
    if (log.isDebugEnabled()) {
      Enumeration<String> headerNames = request.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        log.debug("header【{}】:{}", headerName, request.getHeader(headerName));
      }
    }

    return addIp;
  }

  /**
   * 返回系统和浏览器信息
   *
   * @return
   */
  public static Map<String, Object> getOSAndBrowser() {
    return getOSAndBrowser(getRequest());
  }

  /**
   * 返回系统和浏览器信息
   *
   * @param request
   * @return
   */
  public static Map<String, Object> getOSAndBrowser(HttpServletRequest request) {
    // 获取User-Agent字符串
    String agent = request.getHeader("User-Agent");
    // 解析User-Agent字符串
    UserAgent userAgent = UserAgent.parseUserAgentString(agent);
    Map<String, Object> agentMap = new HashMap<>();
    StringBuffer browserInfo = new StringBuffer();
    String operatingSystemName = Constant.UNKNOWN;
    if (null != userAgent) {
      // 获取浏览器对象
      Browser browser = userAgent.getBrowser();
      if (null != browser) {
        // 浏览器名
        browserInfo.append(browser.getName()).append(":");
        // 浏览器类型
        browserInfo.append(browser.getBrowserType()).append(":");
        // 浏览器家族
        browserInfo.append(browser.getGroup()).append(":");
        // 浏览器生产厂商
        browserInfo.append(browser.getManufacturer()).append(":");
        // 浏览器使用的渲染引擎
        browserInfo.append(browser.getRenderingEngine()).append(":");
      }
      // 获取操作系统对象
      OperatingSystem operatingSystem = userAgent.getOperatingSystem();
      if (null != operatingSystem) {
        operatingSystemName = operatingSystem.getName();
        // 操作系统名
        // 访问设备类型
        browserInfo.append(operatingSystem.getName()).append(":");
        browserInfo.append(operatingSystem.getDeviceType()).append(":");
        // 操作系统家族
        browserInfo.append(operatingSystem.getGroup()).append(":");
        // 操作系统生产厂商
        browserInfo.append(operatingSystem.getGroup()).append(":");
      }
      // 浏览器版本
      browserInfo.append(userAgent.getBrowserVersion());
    }
    agentMap.put(OS, operatingSystemName);
    agentMap.put(BROWSER, browserInfo);

    return agentMap;
  }

  /**
   * 设置文件名称编码格式
   *
   * @param fileName 文件名称
   * @return
   */
  public static String encodingFileName(String fileName) throws UnsupportedEncodingException {
    return encodingFileName(getRequest(), fileName);
  }

  /**
   * 设置文件名称编码格式
   *
   * @param request  servlet请求
   * @param fileName 文件名称
   * @return
   */
  public static String encodingFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
    String agent = request.getHeader("User-Agent");
    fileName = URLEncoder.encode(fileName, LsCharset.UTF_8);
    if (agent.contains("MSIE")) {
      // IE浏览器
      fileName = fileName.replace("+", StringUtil.SPACE);
    }
    if (agent.contains("Firefox")) {
      // 火狐浏览器
      fileName = new String(fileName.getBytes(), "ISO8859-1");
    }
    return fileName;
  }

}
