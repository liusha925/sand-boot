/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.core.util.editor;

import com.sand.core.util.lang3.DateUtil;

import java.beans.PropertyEditorSupport;

/**
 * 功能说明：日期编辑器
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 22:29
 * 功能描述：格式化日期类型,无需配置，spring容器会自动加载PropertyEditor
 */
public class DateEditor extends PropertyEditorSupport {

  @Override
  public void setAsText(String text) {
    setValue(DateUtil.parseDate(text));
  }
}
