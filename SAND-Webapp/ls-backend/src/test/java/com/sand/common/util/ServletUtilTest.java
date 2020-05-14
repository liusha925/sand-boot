/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/5/14    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util;

import com.sand.JunitBootStrap;
import com.sand.common.util.convert.SandCharset;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 功能说明：ServletUtil测试类
 * 开发人员：@author liusha
 * 开发日期：2020/5/14 15:03
 * 功能描述：ServletUtil测试类
 */
public class ServletUtilTest extends JunitBootStrap {

  @Test
  public void getRemoteAddress() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setCharacterEncoding(SandCharset.UTF_8);
    RequestAttributes attributes = new ServletRequestAttributes(request);
    RequestContextHolder.setRequestAttributes(attributes);
    retData = ServletUtil.getRemoteAddress();
  }

  @Test
  public void getHostAddress() throws Exception {
    retData = ServletUtil.getHostAddress();
  }
}
