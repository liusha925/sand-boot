/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/27    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.poi;

import com.sand.base.annotation.ExcelAnnotation;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.util.lang3.StringUtil;
import com.sand.base.util.poi.template.AbstractExcelPoi;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;

/**
 * 功能说明：excel工具类
 * 开发人员：@author liusha
 * 开发日期：2019/9/27 8:24
 * 功能描述：使用自定义的excel注解@ExcelAnnotation实现excel导入导出操作
 */
@Slf4j
public class ExcelUtil<T> extends AbstractExcelPoi<T> {

  public ExcelUtil(Class<T> entity) {
    super(entity);
  }

  /**
   * 下载模板文件
   *
   * @param sheetName 模板文件名称
   * @return
   */
  public ResultEntity downTemplate(String sheetName) {
    super.init(sheetName, ExcelAnnotation.Type.DOWN_TEMPLATE);
    return super.export();
  }

  /**
   * 将excel中的数据转换成list
   *
   * @param is 输入流
   * @return
   */
  public List<T> imported(InputStream is) throws Exception {
    return super.imported(StringUtil.EMPTY, is);
  }

  /**
   * 将list数据导入到excel中
   *
   * @param sheetName  文件名称
   * @param entityList list列表数据
   * @return
   */
  public ResultEntity export(String sheetName, List<T> entityList) {
    super.init(sheetName, ExcelAnnotation.Type.EXPORT, entityList);
    return super.export();
  }

}
