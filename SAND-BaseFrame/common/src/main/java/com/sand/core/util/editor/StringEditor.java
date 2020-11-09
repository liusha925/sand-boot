/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.editor;

import com.sand.core.util.Utils;
import com.sand.core.util.lang3.StringUtil;

import java.beans.PropertyEditorSupport;

/**
 * 功能说明：字符串编辑器
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 22:38
 * 功能描述：null转字符串处理,无需配置，spring容器会自动加载PropertyEditor
 */
public class StringEditor extends PropertyEditorSupport {
  @Override
  public String getAsText() {
    Object value = getValue();
    return value != null ? value.toString() : StringUtil.EMPTY;
  }

  @Override
  public void setAsText(String text) {
    setValue(text == null ? null : Utils.replaceHtml(text));
  }
}
