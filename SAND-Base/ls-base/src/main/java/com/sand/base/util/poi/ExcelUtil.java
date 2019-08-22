/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/19   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.poi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.sand.base.enums.DateEnum;
import com.sand.base.enums.ResultEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.common.DateUtil;
import com.sand.base.util.common.StringUtil;
import com.sand.base.util.http.OkHttp3Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 功能说明：excel工具类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/19 14:22
 * 功能描述：使用开源的easypoi，参考http://easypoi.mydoc.io
 */
@Slf4j
public class ExcelUtil {
  /**
   * 以.xls结尾的excel文件
   */
  public static final String XLS = ".xls";
  /**
   * 以.xlsx结尾的excel文件
   */
  public static final String XLSX = ".xlsx";

  public ExcelUtil() {
  }

  /**
   * excel导出
   *
   * @param pojoClass Excel对象Class
   * @param dataSet   Excel对象数据List
   * @param maxNum    单sheet最大值
   */
  public static String exportExcel(Class<?> pojoClass, Collection<?> dataSet, int maxNum) {
    FileOutputStream fos;
    String saveFolder = OkHttp3Util.getServicePath() + File.separator + "excel";
    String savePath = saveFolder + File.separator + System.currentTimeMillis() + XLS;
    try {
      File createFolder = new File(saveFolder);
      if (!createFolder.exists()) {
        createFolder.mkdirs();
      }
      ExportParams exportParams = new ExportParams();
      exportParams.setMaxNum(maxNum == 0 ? 10000 : maxNum);
      exportParams.setStyle(ExcelExportStyler.class);
      long start = System.currentTimeMillis();
      Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, dataSet);
      log.info("excel导出耗时 = {}", (System.currentTimeMillis() - start) + "毫秒");
      fos = new FileOutputStream(savePath);
      workbook.write(fos);
      fos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return savePath;
  }

  /**
   * 通过excel模板导出
   *
   * @param templateFilePath 模板位置
   * @param pojoClass        Excel对象Class
   * @param dataSet          Excel对象数据List
   * @param otherData        对象外的数据
   * @return
   */
  public static String exportExcelByTemplate(String templateFilePath, Class<?> pojoClass, Collection<?> dataSet, Map<String, Object> otherData) {
    FileOutputStream fos;
    String saveFolder = OkHttp3Util.getServicePath() + File.separator + "excel";
    String savePath = saveFolder + File.separator + System.currentTimeMillis() + XLS;
    try {
      File createFolder = new File(saveFolder);
      if (!createFolder.exists()) {
        createFolder.mkdirs();
      }
      File templateFile = new File(templateFilePath);
      // 模板不存在用普通导出
      if (!templateFile.exists()) {
        return exportExcel(pojoClass, dataSet, 65536);
      }
      TemplateExportParams params = new TemplateExportParams(templateFilePath);
      otherData.put("entityList", dataSet);
      long start = System.currentTimeMillis();
      Workbook workbook = ExcelExportUtil.exportExcel(params, otherData);
      log.info("excel导出耗时 = {}", (System.currentTimeMillis() - start) + "毫秒");
      fos = new FileOutputStream(savePath);
      workbook.write(fos);
      fos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return savePath;
  }

  /**
   * excel导入
   *
   * @param file      需要导入的文件
   * @param pojoClass Excel对象Class
   * @param <T>       Excel对象Class
   * @return
   */
  public static <T> List<T> importExcel(File file, Class<?> pojoClass) {
    ImportParams importParams = new ImportParams();
    importParams.setHeadRows(1);
    importParams.setTitleRows(1);
    long start = System.currentTimeMillis();
    List<T> list = ExcelImportUtil.importExcel(file, pojoClass, importParams);
    log.info("excel导入耗时 = {}", (System.currentTimeMillis() - start) + "毫秒");
    return list;
  }

  /**
   * 读取导入excel
   *
   * @param file
   * @param saveFileFlag
   * @return
   */
  public static Map<String, Object> importExcel(MultipartFile file, boolean saveFileFlag) {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      long start = System.currentTimeMillis();
      List<String[]> result = new ArrayList<>();
      String fileName = file.getOriginalFilename();
      Workbook workbook;
      if (fileName.endsWith(XLS)) {
        workbook = new HSSFWorkbook(file.getInputStream());
      } else if (fileName.endsWith(XLSX)) {
        workbook = new XSSFWorkbook(file.getInputStream());
      } else {
        throw new LsException(ResultEnum.ERROR, "读取文件类型异常");
      }
      // 保存文件
      if (saveFileFlag) {
        String saveFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-" + StringUtil.getUniqueSerialNo();
        if (fileName.endsWith(XLS)) {
          saveFileName = saveFileName + XLS;
        } else if (fileName.endsWith(XLSX)) {
          saveFileName = saveFileName + XLSX;
        } else {
          throw new LsException(ResultEnum.ERROR, "读取文件类型异常");
        }
        File saveDir = new File(OkHttp3Util.getServicePath() + File.separator + "temp");
        if (!saveDir.exists()) {
          saveDir.mkdirs();
        }
        // 保存文件
        file.transferTo(new File(saveFileName));
        resultMap.put("filePath", saveFileName);
      }
      //获取标题列数
      Sheet sheet1 = workbook.getSheetAt(0);
      int sheet1Rows = sheet1.getRow(0).getPhysicalNumberOfCells();
      // 得到最后一行行号
      int rowNum = sheet1.getLastRowNum();
      //行数据
      String[] rowValue;
      //行数据
      Row row;
      for (int i = 1; i <= rowNum; i++) {
        rowValue = new String[sheet1Rows];
        row = sheet1.getRow(i);
        int j = 0;
        while (j < sheet1Rows && Objects.nonNull(row)) {
          if (Objects.nonNull(row.getCell(j))) {
            CellType cellType = row.getCell(j).getCellTypeEnum();
            switch (cellType) {
              case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(row.getCell(j))) {
                  rowValue[j] = validCellContent(row.getCell(j));
                } else {
                  DecimalFormat df = new DecimalFormat("#.########");
                  rowValue[j] = df.format(row.getCell(j).getNumericCellValue());
                }
                break;
              case STRING:
                if (Objects.nonNull(row.getCell(j))) {
                  rowValue[j] = validCellContent(row.getCell(j));
                }
                break;
              default:
                if (Objects.nonNull(row.getCell(j))) {
                  rowValue[j] = row.getCell(j).getStringCellValue();
                }
                break;
            }
          }
          j++;
        }
        result.add(rowValue);
      }
      log.info("excel导入耗时 = {}", (System.currentTimeMillis() - start) + "毫秒");
      resultMap.put("result", result);
      return resultMap;
    } catch (Exception e) {
      e.printStackTrace();
      log.info("导入异常");
      return resultMap;
    }
  }


  private static String validCellContent(Cell cell) {
    String value = null;
    // 如果数据为数字格式
    if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
      // 如果是日期格式，直接以日期格式数据提取出来
      Date d = cell.getDateCellValue();
      DateFormat format = new SimpleDateFormat(DateEnum.F1_YYYY_MM_DD.getPattern());
      // 再判断它是不是日期格式的，DateUtil的该方法只能判断出常规日期格式，包含汉字的会返回false
      if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
        // 并进行格式化
        value = format.format(d);
        // 数据为数字格式，且是非常规日期格式
      } else {
        // 进行特殊格式判断
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if ("yyyy/mm;@".equals(dataFormatString) || "m/d/yy".equals(dataFormatString)
            || "yy/m/d".equals(dataFormatString) || "mm/dd/yy".equals(dataFormatString)
            || "dd-mmm-yy".equals(dataFormatString) || "yyyy/m/d".equals(dataFormatString)
            || "m\"月\"d\"日\";@".equals(dataFormatString) || "yyyy\"年\"m\"月\"d\"日\";@".equals(dataFormatString)
            || "yyyy\"年\"m\"月\";@".equals(dataFormatString)
        ) {
          String guarantee_date = format.format(d);
          value = guarantee_date;
          // 仅仅为数字格式，不是日期，就把数据作为数字存储
        } else {
          value = cell.getNumericCellValue() + "";
        }
      }
      // 如果数据为String类型
    } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
      // 判断它是不是文本格式的日期
      if (DateUtil.isValidDate(cell.getStringCellValue(), DateEnum.F4_YYYY_MM_DD)
          || DateUtil.isValidDate(cell.getStringCellValue(), DateEnum.F6_YYYY_MM)
          || DateUtil.isValidDate(cell.getStringCellValue(), DateEnum.F6_YYYY_MM_DD)) {
        // 如果是特殊格式的日期，则进行相关处理转换，格式化为yyyy-MM-dd的格式
        if (cell.getStringCellValue().contains("/")) {
          value = cell.getStringCellValue().replaceAll("/", "-");
        }
        if (cell.getStringCellValue().contains("年") && cell.getStringCellValue().contains("月")) {
          value = cell.getStringCellValue()
              .replace("年", "-")
              .replace("月", "-")
              .replace("日", "");
        }
        // 仅仅是普通文本数据，不是日期
      } else {
        value = cell.getStringCellValue();
      }
    } else {
      value = cell.getStringCellValue();
    }
    return value;
  }

}
