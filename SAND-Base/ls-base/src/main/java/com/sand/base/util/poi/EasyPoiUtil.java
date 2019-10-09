/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/19   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.poi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.core.text.LsCharset;
import com.sand.base.exception.LsException;
import com.sand.base.util.CloseableUtil;
import com.sand.base.util.ResultUtil;
import com.sand.base.util.ServletUtil;
import com.sand.base.util.lang3.StringUtil;
import com.sand.base.util.poi.template.ExcelStyler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：excel工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/19 14:22
 * 功能描述：使用开源的easypoi实现excel导入导出操作，参考http://easypoi.mydoc.io
 */
@Slf4j
public class EasyPoiUtil {

  public EasyPoiUtil() {
  }

  /**
   * 下载模板文件
   *
   * @param sheetName 工作表名称
   * @param pojoClass 实体对象
   */
  public static ResultEntity downTemplateExcel(String sheetName, Class<?> pojoClass) {
    return exportExcel(sheetName, pojoClass, new ArrayList<>());
  }

  /**
   * excel导入
   *
   * @param file      导入文件
   * @param pojoClass 实体对象
   * @return
   */
  public static <T> List<T> importExcel(File file, Class<?> pojoClass) {
    ImportParams importParams = new ImportParams();
    importParams.setHeadRows(1);
    List<T> list = ExcelImportUtil.importExcel(file, pojoClass, importParams);
    return list;
  }

  /**
   * excel导出
   *
   * @param sheetName 工作表名称
   * @param pojoClass 实体对象
   * @param dataSet   数据集合
   * @return
   */
  public static ResultEntity exportExcel(String sheetName, Class<?> pojoClass, Collection<?> dataSet) {
    return exportExcel(sheetName, pojoClass, dataSet, ExcelStyler.SHEET_MAX_NUM, StringUtil.EMPTY);
  }

  /**
   * excel导出，使用指定模板
   *
   * @param sheetName   工作表名称
   * @param pojoClass   实体对象
   * @param dataSet     数据集合
   * @param templateUrl 模板路径
   * @return
   */
  public static ResultEntity exportExcel(String sheetName, Class<?> pojoClass, Collection<?> dataSet, String templateUrl) {
    return exportExcel(sheetName, pojoClass, dataSet, ExcelStyler.SHEET_MAX_NUM, templateUrl);
  }

  /**
   * excel导出
   *
   * @param sheetName   工作表名称
   * @param pojoClass   实体对象
   * @param dataSet     数据集合
   * @param maxNum      单sheet最大值
   * @param templateUrl 模板文件路径
   * @return
   */
  public static ResultEntity exportExcel(String sheetName, Class<?> pojoClass, Collection<?> dataSet, int maxNum, String templateUrl) {
    return exportExcel(sheetName, pojoClass, dataSet, ExcelStyler.class, maxNum, templateUrl);
  }

  /**
   * excel导出
   *
   * @param sheetName   工作表名称
   * @param pojoClass   实体对象
   * @param dataSet     数据集合
   * @param style       样式选择器
   * @param maxNum      单sheet最大值
   * @param templateUrl 模模板文件路径
   * @return
   */
  public static ResultEntity exportExcel(String sheetName, Class<?> pojoClass, Collection<?> dataSet, Class<?> style, int maxNum, String templateUrl) {
    OutputStream out = null;
    Workbook workbook = null;
    try {
      long start = System.currentTimeMillis();
      File templateFile = new File(templateUrl);
      // 模板不存在用普通导出
      if (!templateFile.exists()) {
        ExportParams exportParams = new ExportParams();
        exportParams.setMaxNum(maxNum);
        exportParams.setStyle(style);
        workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, dataSet);
      } else {
        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("entityList", dataSet);
        TemplateExportParams templateParams = new TemplateExportParams();
        templateParams.setTemplateUrl(templateUrl);
        templateParams.setStyle(style);
        workbook = ExcelExportUtil.exportExcel(templateParams, templateMap);
      }
      log.info("excel导出耗时 = {}", (System.currentTimeMillis() - start) + "毫秒");
      HttpServletResponse response = ServletUtil.getResponse();
      String fileName = ServletUtil.encodingFileName(sheetName);
      response.setCharacterEncoding(LsCharset.UTF_8);
      response.setContentType("multipart/form-data");
      response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
      out = response.getOutputStream();
      workbook.write(out);
      return ResultUtil.ok(fileName);
    } catch (Exception e) {
      throw new LsException("导出excel异常，请联系管理人员");
    } finally {
      CloseableUtil.close(workbook, out);
    }
  }

}
