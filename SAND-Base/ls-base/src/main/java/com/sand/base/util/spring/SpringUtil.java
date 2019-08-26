/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 功能说明：spring工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 13:59
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
@Component
public class SpringUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;
  private static DefaultListableBeanFactory beanFactory;

  public SpringUtil() {
  }

  /**
   * 获取applicationContext
   *
   * @return
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (SpringUtil.applicationContext == null) {
      SpringUtil.applicationContext = applicationContext;
      beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }
  }

  /**
   * 注册bean到Spring容器中
   *
   * @param obj
   */
  public static void register(Object obj) {
    beanFactory.registerSingleton(obj.getClass().getName(), obj);
  }

  /**
   * 注册bean到Spring容器中（提前删除）
   *
   * @param obj
   */
  public static void register(Object obj, String beanName, boolean remove) {
    if (remove) {
      beanFactory.destroySingleton(beanName);
    }
    beanFactory.registerSingleton(beanName, obj);
  }

  /**
   * 注册bean到Spring容器中
   *
   * @param obj
   */
  public static void register(Object obj, String beanName) {
    beanFactory.registerSingleton(beanName, obj);
  }

  /**
   * 通过name获取 Bean.
   *
   * @param name
   * @return
   */
  public static Object getBean(String name) {
    return getApplicationContext().getBean(name);
  }

  /**
   * 通过class获取Bean.
   *
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(Class<T> clazz) {
    return getApplicationContext().getBean(clazz);
  }

  /**
   * 通过name,以及Clazz返回指定的Bean
   *
   * @param name
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }

  /**
   * 获取某类型的实例
   *
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> Map<String, T> getBeans(Class<T> clazz) {
    return getApplicationContext().getBeansOfType(clazz);
  }

}
