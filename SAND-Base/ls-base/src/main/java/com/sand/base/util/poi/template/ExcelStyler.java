/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/19   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.poi.template;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 功能说明：excel导出样式设置
 * 开发人员：@author liusha
 * 开发日期：2019/8/19 16:06
 * 功能描述：excel导出样式设置
 */
public class ExcelStyler extends AbstractExcelExportStyler {
  /**
   * 单sheet最大值
   */
  public static final int SHEET_MAX_NUM = 65536;
  private static final short FONT_SIZE_TEN = 10;
  private static final short FONT_SIZE_ELEVEN = 11;
  private static final short FONT_SIZE_TWELVE = 12;
  private static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");
  /**
   * 标题样式
   */
  private CellStyle titleStyle;
  /**
   * 表头样式
   */
  private CellStyle headerStyle;
  /**
   * 数据样式
   */
  private CellStyle dataStyle;

  public ExcelStyler() {
    this.init(workbook);
  }

  /**
   * 初始化样式
   *
   * @param workbook
   */
  private void init(Workbook workbook) {
    this.titleStyle = initTitleStyle(workbook);
    this.headerStyle = initHeaderStyle(workbook);
    this.dataStyle = initDataStyle(workbook);
  }

  /**
   * 标题样式
   *
   * @param color
   * @return
   */
  @Override
  public CellStyle getTitleStyle(short color) {
    return titleStyle;
  }

  /**
   * 表头样式
   *
   * @param color
   * @return
   */
  @Override
  public CellStyle getHeaderStyle(short color) {
    return headerStyle;
  }

  /**
   * 数据样式
   *
   * @param parity 奇偶行
   * @param entity 数据内容
   * @return 样式
   */
  @Override
  public CellStyle getStyles(boolean parity, ExcelExportEntity entity) {
    return dataStyle;
  }

  /**
   * 获取样式
   *
   * @param dataRow 数据行
   * @param obj     对象
   * @param data    数据
   */
  @Override
  public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
    return getStyles(true, entity);
  }

  /**
   * 模板样式设置
   */
  @Override
  public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
    return null;
  }

  /**
   * 初始化--标题样式
   *
   * @param workbook
   * @return
   */
  public static CellStyle initTitleStyle(Workbook workbook) {
    CellStyle cellStyle = getBaseCellStyle(workbook);
    // 字体大小
    cellStyle.setFont(getFont(workbook, FONT_SIZE_TWELVE, true));

    return cellStyle;
  }

  /**
   * 初始化--表头样式
   *
   * @param workbook
   * @return
   */
  public static CellStyle initHeaderStyle(Workbook workbook) {
    CellStyle cellStyle = getBaseCellStyle(workbook);
    // 字体大小
    cellStyle.setFont(getFont(workbook, FONT_SIZE_ELEVEN, false));
    // 背景颜色
    cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return cellStyle;
  }

  /**
   * 初始化--数据样式
   *
   * @param workbook
   * @return
   */
  public static CellStyle initDataStyle(Workbook workbook) {
    CellStyle cellStyle = getBaseCellStyle(workbook);
    // 字体大小
    cellStyle.setFont(getFont(workbook, FONT_SIZE_TEN, false));
    // 单元格格式
    cellStyle.setDataFormat(STRING_FORMAT);
    return cellStyle;
  }

  /**
   * 基础样式
   *
   * @return
   */
  public static CellStyle getBaseCellStyle(Workbook workbook) {
    CellStyle cellStyle = workbook.createCellStyle();
    // 上边框
    cellStyle.setBorderTop(BorderStyle.THIN);
    // 下边框
    cellStyle.setBorderBottom(BorderStyle.THIN);
    // 左边框
    cellStyle.setBorderLeft(BorderStyle.THIN);
    // 右边框
    cellStyle.setBorderRight(BorderStyle.THIN);
    // 水平居中
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    // 垂直居中
    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    // 自动换行
    cellStyle.setWrapText(false);
    return cellStyle;
  }

  /**
   * 字体样式
   *
   * @param size   字体大小
   * @param isBold 是否加粗
   * @return
   */
  public static Font getFont(Workbook workbook, short size, boolean isBold) {
    Font font = workbook.createFont();
    // 字体样式
    font.setFontName("宋体");
    // 是否加粗
    font.setBold(isBold);
    // 字体大小
    font.setFontHeightInPoints(size);
    return font;
  }
}
