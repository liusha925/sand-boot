/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/6   liusha      新增
 * =========  ===========  =====================
 */
package com.sand.common.util;

import com.alibaba.fastjson.JSONObject;
import com.sand.common.util.convert.SandCharset;
import com.sand.common.util.lang3.StringUtil;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
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
   * @return HttpServletRequest
   */
  public static HttpServletRequest getRequest() {
    return getRequestAttributes().getRequest();
  }

  /**
   * 获取response
   *
   * @return HttpServletResponse
   */
  public static HttpServletResponse getResponse() {
    return getRequestAttributes().getResponse();
  }

  /**
   * 获取session
   *
   * @return HttpSession
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
   * @param name 参数名
   * @return 参数值
   */
  public static String getParameter(String name) {
    return getRequest().getParameter(name);
  }

  /**
   * 获取所有的请求参数
   *
   * @return 参数值
   */
  public static String getParameters() {
    return getParameters(getRequest());
  }

  /**
   * 获取所有的请求参数
   *
   * @param request request
   * @return 所有的请求参数
   */
  public static String getParameters(HttpServletRequest request) {
    String parameterName;
    JSONObject object = new JSONObject();
    Enumeration parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      parameterName = parameterNames.nextElement().toString();
      object.put(parameterName, request.getParameter(parameterName));
    }
    return object.toJSONString();
  }

  /**
   * 获取本机域名地址
   * <pre>
   *   需将域名地址与本地IP一一对应配置至hosts文件中
   *   System.out.println(ServletUtil.getHostName()); = "www.sand.com"
   * </pre>
   *
   * @return 域名
   */
  public static String getHostName() throws Exception {
    String hostName = InetAddress.getLocalHost().getCanonicalHostName();
    if (StringUtil.isBlank(hostName)) {
      hostName = InetAddress.getLocalHost().getHostName();
    }
    return hostName;
  }

  /**
   * 获取本机IP地址
   * <pre>
   *   联网的情况下System.out.println(ServletUtil.getHostAddress()); = "对应的ip地址"
   *   不联网的情况下System.out.println(ServletUtil.getHostAddress()); = "127.0.0.1"
   * </pre>
   *
   * @return ip地址
   */
  public static String getHostAddress() throws Exception {
    String hostAddress = null;
    Enumeration<?> networkInterfaces = NetworkInterface.getNetworkInterfaces();
    while (networkInterfaces.hasMoreElements()) {
      NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
      Enumeration<?> addresses = networkInterface.getInetAddresses();
      while (addresses.hasMoreElements()) {
        InetAddress ip = (InetAddress) addresses.nextElement();
        if (ip instanceof Inet4Address) {
          hostAddress = ip.getHostAddress();
        }
      }
    }
    if (StringUtil.isNotBlank(hostAddress)) {
      return hostAddress;
    } else {
      return InetAddress.getLocalHost().getHostAddress();
    }
  }

  @Getter
  @AllArgsConstructor
  public enum ProxyType {
    // 代理类型
    X_FORWARDED_FOR("X-Forwarded-For", "Squid 服务代理"),
    PROXY_CLIENT_IP("Proxy-Client-IP", "apache 服务代理"),
    WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP", "webLogic 服务代理"),
    HTTP_CLIENT_IP("HTTP_CLIENT_IP", "其它 服务代理"),
    X_REAL_IP("X-Real-IP", "nginx 服务代理"),
    ;
    private String value;
    private String vName;
  }

  /**
   * 获取远程调用IP地址
   *
   * @return ip地址
   */
  public static String getRemoteAddress() {
    return getRemoteAddress(getRequest());
  }

  /**
   * 获取远程调用IP地址
   *
   * @return ip地址
   */
  public static String getRemoteAddress(HttpServletRequest request) {
    String remoteAddress = null;
    String serverType = "unknown";
    String remoteAddresses = request.getHeader(ProxyType.X_FORWARDED_FOR.getValue());
    if (StringUtil.isBlank(remoteAddresses) || remoteAddresses.equalsIgnoreCase(serverType)) {
      remoteAddresses = request.getHeader(ProxyType.PROXY_CLIENT_IP.getValue());
    }
    if (StringUtil.isBlank(remoteAddresses) || remoteAddresses.equalsIgnoreCase(serverType)) {
      remoteAddresses = request.getHeader(ProxyType.WL_PROXY_CLIENT_IP.getValue());
    }
    if (StringUtil.isBlank(remoteAddresses) || remoteAddresses.equalsIgnoreCase(serverType)) {
      remoteAddresses = request.getHeader(ProxyType.HTTP_CLIENT_IP.getValue());
    }
    if (StringUtil.isBlank(remoteAddresses) || remoteAddresses.equalsIgnoreCase(serverType)) {
      remoteAddresses = request.getHeader(ProxyType.X_REAL_IP.getValue());
    }
    // 有些网络通过多层代理，就会获取到多个IP，一般逗号（,）分割并且第一个IP为客户端的真实IP
    if (StringUtil.isNotBlank(remoteAddresses)) {
      remoteAddress = remoteAddresses.split(",")[0];
    }
    // 以上都获取不到就从"Remote Address"中获取
    if (StringUtil.isBlank(remoteAddress) || remoteAddresses.equalsIgnoreCase(serverType)) {
      remoteAddress = request.getRemoteAddr();
    }
    if (log.isDebugEnabled()) {
      Enumeration<String> headerNames = request.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        log.debug("header【{}】:{}", headerName, request.getHeader(headerName));
      }
    }

    return remoteAddress;
  }

  /**
   * 返回系统和浏览器信息
   *
   * @return 系统和浏览器信息
   */
  public static Map<String, Object> getOSAndBrowser() {
    return getOSAndBrowser(getRequest());
  }

  /**
   * 返回系统和浏览器信息
   *
   * @param request request
   * @return 系统和浏览器信息
   */
  public static Map<String, Object> getOSAndBrowser(HttpServletRequest request) {
    // 获取User-Agent字符串
    String agent = request.getHeader("User-Agent");
    // 解析User-Agent字符串
    UserAgent userAgent = UserAgent.parseUserAgentString(agent);
    Map<String, Object> agentMap = new HashMap<>();
    StringBuffer browserInfo = new StringBuffer();
    String operatingSystemName = "unknown";
    if (null != userAgent) {
      // 获取浏览器对象
      eu.bitwalker.useragentutils.Browser browser = userAgent.getBrowser();
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
   * @return 文件名称编码格式
   */
  public static String encodingFileName(String fileName) throws UnsupportedEncodingException {
    return encodingFileName(getRequest(), fileName);
  }

  @Getter
  @AllArgsConstructor
  public enum Browser {
    // 浏览器类型
    MSIE("MSIE", "IE浏览器"),
    FIREFOX("Firefox", "火狐浏览器"),
    ;
    private String value;
    private String vName;
  }

  /**
   * 设置文件名称编码格式
   *
   * @param request  servlet请求
   * @param fileName 文件名称
   * @return 文件名称编码格式
   */
  public static String encodingFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
    String agent = request.getHeader("User-Agent");
    fileName = URLEncoder.encode(fileName, SandCharset.UTF_8);
    if (agent.contains(Browser.MSIE.getValue())) {
      fileName = fileName.replace("+", StringUtil.SPACE);
    }
    if (agent.contains(Browser.FIREFOX.getValue())) {
      fileName = new String(fileName.getBytes(), SandCharset.ISO_8859_1);
    }
    return fileName;
  }

}
