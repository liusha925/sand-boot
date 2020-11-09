/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/22    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.checkstyle;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import java.util.EnumSet;

import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.tools.Diagnostic.Kind.WARNING;

/**
 * 功能说明：程序名称规范“检查器”
 * 开发人员：@author liusha
 * 开发日期：2019/11/22 15:42
 * 功能描述：如果程序命名不合规范，将会输出一个编译器的WARNING信息，来源于《深入理解Java虚拟机》——周志明第10.4.2节
 */
public class NameChecker {
  private final Messager messager;
  NameCheckScanner nameCheckScanner = new NameCheckScanner();

  NameChecker(ProcessingEnvironment processingEnv) {
    this.messager = processingEnv.getMessager();
  }

  /**
   * 对java程序命名进行检查，根据《Java语言规范（第3版）》第6.8节的要求，java程序命名应当符合下列格式：
   *
   * <ul>
   * <li>类或接口：符合驼式命名法，首字母大写。
   * <li>方法：符合驼式命名法，首字母小写。
   * <li>字段：
   * <ul>
   * <li>类、实例变量：符合驼式命名法，首字母小写。
   * <li>常量：要求全部大写。
   * </ul>
   * </ul>
   *
   * @param element 元素
   */
  public void checkNames(Element element) {
    nameCheckScanner.scan(element);
  }

  /**
   * 名称检查器实现类，继承JDK1.8中新提供的ElementScanner8，实质是ElementScanner6<br>
   * 将会以Visitor模式访问抽象语法树中的元素
   */
  private class NameCheckScanner extends ElementScanner8<Void, Void> {
    /**
     * 检查java类命名是否合法
     *
     * @param e Element
     * @param p 返回类型
     * @return Void
     */
    @Override
    public Void visitType(TypeElement e, Void p) {
      super.scan(e.getTypeParameters(), p);
      checkCamelCase(e, true);
      super.visitType(e, p);
      return null;
    }

    /**
     * 检查方法命名是否合法
     *
     * @param e Element
     * @param p Void
     * @return Void
     */
    @Override
    public Void visitExecutable(ExecutableElement e, Void p) {
      if (e.getKind() == METHOD) {
        Name name = e.getSimpleName();
        if (name.contentEquals(e.getEnclosingElement().getSimpleName())) {
          messager.printMessage(WARNING, "一个普通方法" + name + "不应当与类名重复，避免与构造函数产生混淆", e);
          checkCamelCase(e, false);
        }
      }
      super.visitExecutable(e, p);
      return null;
    }

    /**
     * 检查变量命名是否合法
     *
     * @param e Element
     * @param p Void
     * @return Void
     */
    @Override
    public Void visitVariable(VariableElement e, Void p) {
      // 如果这个Variable是枚举或常量，则按大写命名检查，否则按照驼式命名法规则检查
      if (e.getKind() == ENUM_CONSTANT || e.getConstantValue() != null) {
        checkAllCaps(e);
      } else {
        checkCamelCase(e, false);
      }
      return null;
    }

    /**
     * 判断一个变量是否是常量
     *
     * @param e Element
     * @return true-是，false-不是
     */
    private boolean heuristicallyConstant(VariableElement e) {
      if (e.getEnclosingElement().getKind() == INTERFACE
          || (e.getKind() == FIELD && e.getModifiers().containsAll(EnumSet.of(PUBLIC, STATIC, FINAL)))) {
        return true;
      }  else {
        return false;
      }
    }

    /**
     * 检查传入的Element是否符合驼式命名法，如果不符合，则输出警告信息
     *
     * @param e           Element
     * @param initialCaps 字母开头是否大写 true-大写开头 false-小写开头
     */
    private void checkCamelCase(Element e, boolean initialCaps) {
      String name = e.getSimpleName().toString();
      boolean previousUpper = false;
      // 是否符合驼式命名法
      boolean conventional = true;
      int firstCodePoint = name.codePointAt(0);
      if (Character.isUpperCase(firstCodePoint)) {
        previousUpper = true;
        if (!initialCaps) {
          messager.printMessage(WARNING, "名称" + name + "应当以小写字母开头", e);
          return;
        }
      } else if (Character.isLowerCase(firstCodePoint)) {
        if (initialCaps) {
          messager.printMessage(WARNING, "名称" + name + "应当以大写字母开头", e);
          return;
        }
      } else {
        conventional = false;
      }
      if (conventional) {
        int cp = firstCodePoint;
        for (int i = Character.charCount(cp); i < name.length(); i += Character.charCount(cp)) {
          cp = name.codePointAt(i);
          if (Character.isUpperCase(cp)) {
            if (previousUpper) {
              conventional = false;
              break;
            }
            previousUpper = true;
          } else {
            previousUpper = false;
          }
        }
      }
      if (!conventional) {
        messager.printMessage(WARNING, "名称" + name + "应当符合驼式命名法（Camel Case Names）", e);
      }
    }

    /**
     * 大写命名检查，要求第一个字母必须是大写的英文字母，其余部分可以是下划线或大写字母
     *
     * @param e Element
     */
    private void checkAllCaps(Element e) {
      String name = e.getSimpleName().toString();
      // 是否符合驼式命名法
      boolean conventional = true;
      int firstCodePoint = name.codePointAt(0);
      if (!Character.isUpperCase(firstCodePoint)) {
        conventional = false;
      } else {
        boolean previousUnderscore = false;
        int cp = firstCodePoint;
        for (int i = Character.charCount(cp); i < name.length(); i += Character.charCount(cp)) {
          cp = name.codePointAt(i);
          if (cp == (int) '_') {
            if (previousUnderscore) {
              conventional = false;
              break;
            }
            previousUnderscore = true;
          } else {
            previousUnderscore = false;
            if (!Character.isUpperCase(cp) && !Character.isDigit(cp)) {
              conventional = false;
              break;
            }
          }
        }
      }
      if (!conventional) {
        messager.printMessage(WARNING, "常量" + name + "应当全部以大写字母或下划线命名，并且以字母开头", e);
      }
    }
  }
}
