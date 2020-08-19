/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/19    liusha   新增
 * =========  ===========  =====================
 */
package com.sand;

import static java.lang.invoke.MethodHandles.lookup;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

/**
 * 功能说明：MethodHandle分派测试
 * 开发人员：@author liusha
 * 开发日期：2019/11/19 14:26
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
public class MethodHandleTest {

  class GrandFather {
    void thinking() {
      System.out.println("I am GrandFather");
    }
  }

  class Father extends GrandFather {
    void thinking() {
      System.out.println("I am Father");
    }
  }

  class Son extends Father {
    void thinking() {
      try {
        MethodType mt = MethodType.methodType(void.class);
        MethodHandle mh = lookup().findSpecial(GrandFather.class, "thinking", mt, getClass());
        mh.invoke(this);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    (new MethodHandleTest().new Son()).thinking();
  }
}
