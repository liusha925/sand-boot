/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/25    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.util.checkstyle;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * 功能说明：程序名称规范“处理器”
 * 开发人员：@author liusha
 * 开发日期：2019/11/25 10:43
 * 功能描述：来源于《深入理解Java虚拟机》——周志明第10.4.2节
 */

/**
 * 可以用"*"表示支持所有的Annotations
 */
@SupportedAnnotationTypes("*")
/**
 * 只支持JDK1.8的Java代码
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class NameCheckerProcessor extends AbstractProcessor {
  private NameChecker nameChecker;

  /**
   * 初始化名称“检查器”
   *
   * @param processingEnv
   */
  @Override
  public void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    nameChecker = new NameChecker(processingEnv);
  }

  /**
   * 对输入的语法树的各个节点进行名称检查
   *
   * @param annotations
   * @param roundEnv
   * @return
   */
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (!roundEnv.processingOver()) {
      roundEnv.getRootElements().forEach(e -> nameChecker.checkNames(e));
    }
    return false;
  }
}
