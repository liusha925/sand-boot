/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/13    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.global;

import com.sand.core.util.convert.SandCharset;
import com.sand.core.util.CloseableUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

/**
 * 功能说明：加载配置文件工具
 * 开发人员：@author liusha
 * 开发日期：2020/3/13 9:36
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
@NoArgsConstructor
public class PropUtil {

  private Properties properties;

  public PropUtil(String fileName) {
    this(fileName, SandCharset.UTF_8);
  }

  public PropUtil(String fileName, String encoding) {
    InputStream input = null;
    try {
      log.info("loading file：{}", fileName);
      input = PathUtil.getClassLoader().getResourceAsStream(fileName);
      if (Objects.isNull(input)) {
        throw new IllegalArgumentException("Properties file not found in classpath，fileName：" + fileName);
      }
      properties = new Properties();
      properties.load(new InputStreamReader(input, encoding));
    } catch (Exception e) {
      log.info("load file error：{}", fileName);
      ClassPathResource resource = new ClassPathResource(fileName);
      try {
        input = resource.getInputStream();
        properties.load(input);
      } catch (Exception e1) {
        throw new RuntimeException("loading file error，", e1);
      }
    } finally {
      CloseableUtil.close(input);
    }
  }

  public Properties getProperties() {
    return this.properties;
  }

}
