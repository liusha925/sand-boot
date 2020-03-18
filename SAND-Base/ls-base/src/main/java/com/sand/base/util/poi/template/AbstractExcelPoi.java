/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/29    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.poi.template;

import com.sand.base.annotation.ExcelAnnotation;
import com.sand.base.web.entity.ResultEntity;
import com.sand.base.util.text.LsCharset;
import com.sand.base.util.text.LsConvert;
import com.sand.base.enums.DateEnum;
import com.sand.base.enums.FileSuffixEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.CloseableUtil;
import com.sand.base.util.ReflectUtil;
import com.sand.base.util.ResultUtil;
import com.sand.base.util.ServletUtil;
import com.sand.base.util.lang3.DateUtil;
import com.sand.base.util.lang3.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能说明：excel工具类属性
 * 开发人员：@author liusha
 * 开发日期：2019/9/29 11:32
 * 功能描述：自定义excel工具类属性
 */
@Slf4j
public abstract class AbstractExcelPoi<T> {
  /**
   * 工作表名称
   */
  protected String sheetName;
  /**
   * 操作类型
   */
  protected ExcelAnnotation.Type type;
  /**
   * 数据列表
   */
  protected List<T> entityList;
  /**
   * 含@ExcelAnnotation注解的成员属性
   */
  protected List<Field> fields;
  /**
   * 工作簿对象
   */
  protected Workbook workbook;
  /**
   * 工作表对象
   */
  protected Sheet sheet;
  /**
   * 实体对象
   */
  protected Class<T> entity;

  public AbstractExcelPoi(Class<T> entity) {
    this.entity = entity;
  }

  /**
   * 初始化excel表格数据
   *
   * @param sheetName 工作表名称
   * @param type      操作类型
   */
  protected void init(String sheetName, ExcelAnnotation.Type type) {
    init(sheetName, type, null);
  }

  /**
   * 初始化excel表格数据
   *
   * @param sheetName  工作表名称
   * @param type       操作类型
   * @param entityList 数据列表
   */
  protected void init(String sheetName, ExcelAnnotation.Type type, List<T> entityList) {
    this.entityList = CollectionUtils.isEmpty(entityList) ? new ArrayList<>() : entityList;
    this.sheetName = sheetName;
    this.type = type;
    createTableHeader();
    createWorkBook();
  }

  /**
   * 创建表头：从含有@ExcelAnnotation注解的成员属性获取
   */
  protected void createTableHeader() {
    fields = new ArrayList<>();
    List<Field> tempFields = ReflectUtil.getOrderDeclaredFields(entity, ExcelAnnotation.class);
    // 筛选含有@ExcelAnnotation注解的成员属性
    tempFields.forEach(field -> {
      ExcelAnnotation excelAnnotation = field.getAnnotation(ExcelAnnotation.class);
      if (Objects.nonNull(excelAnnotation)) {
        if (Objects.equals(excelAnnotation.type(), ExcelAnnotation.Type.ALL) || Objects.equals(excelAnnotation.type(), type)) {
          fields.add(field);
        }
      }
    });
  }

  /**
   * 创建工作簿
   */
  protected void createWorkBook() {
    workbook = new SXSSFWorkbook(500);
  }

  /**
   * 将excel中的数据转换成list
   *
   * @param sheetName sheet名称
   * @param is        输入流
   * @return 转换后的list集合
   */
  protected List<T> imported(String sheetName, InputStream is) throws Exception {
    type = ExcelAnnotation.Type.IMPORT;
    workbook = WorkbookFactory.create(is);
    List<T> entityList = new ArrayList<>();
    // 如果传入的sheet名称不存在则默认指定第一个sheet，否则取指定sheet中的内容
    Sheet sheet = StringUtil.isBlank(sheetName) ? workbook.getSheetAt(0) : workbook.getSheet(sheetName);
    if (Objects.isNull(sheet)) {
      throw new LsException("文件中的sheet页不存在！");
    }
    // 行数rows，返回行数为除去表头(rows-1)
    int rows = sheet.getLastRowNum();
    if (rows > 1000) {
      throw new LsException("导入数据超过规定限制条数" + "1000" + "条");
    }
    if (rows > 0) {
      // 默认序号
      AtomicInteger serialNum = new AtomicInteger(0);
      // 获取所有的成员属性
      List<Field> fields = ReflectUtil.getOrderDeclaredFields(entity, ExcelAnnotation.class);
      // 存放序号和成员属性
      Map<Integer, Field> fieldsMap = new HashMap<>();
      fields.forEach(field -> {
        ExcelAnnotation excelAnnotation = field.getAnnotation(ExcelAnnotation.class);
        if (Objects.nonNull(excelAnnotation)) {
          if (Objects.equals(excelAnnotation.type(), ExcelAnnotation.Type.ALL) || Objects.equals(excelAnnotation.type(), type)) {
            // 设置类的私有属性可访问
            field.setAccessible(true);
            fieldsMap.put(serialNum.incrementAndGet(), field);
          }
        }
      });
      // 第一行为表头，所以从第二行开始取数据
      for (int i = 1; i <= rows; i++) {
        Row row = sheet.getRow(i);
        int cellNum = serialNum.get();
        T entity = null;
        for (int column = 0; column < cellNum; column++) {
          entity = Objects.isNull(entity) ? this.entity.newInstance() : entity;
          Object value = getCellValue(row, column);
          // 从map中获取对应的成员属性
          Field field = fieldsMap.get(column + 1);
          // 根据对象类型设置值
          Class<?> fileType = field.getType();
          if (Objects.equals(String.class, fileType)) {
            String s = LsConvert.obj2Str(value);
            if (StringUtil.endsWith(s, ".0")) {
              value = StringUtil.substringBefore(s, ".0");
            } else {
              value = LsConvert.obj2Str(value);
            }
          } else if (Objects.equals(Date.class, fileType)) {
            if (value instanceof String) {
              value = DateUtil.parseDate(LsConvert.obj2Str(value));
            } else if (value instanceof Double) {
              value = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Double) value);
            }
          } else if (Objects.equals(BigDecimal.class, fileType)) {
            value = LsConvert.obj2BigDecimal(value);
          } else if (Objects.equals(Long.class, fileType) || Objects.equals(Long.TYPE, fileType)) {
            value = LsConvert.obj2Long(value);
          } else if (Objects.equals(Integer.class, fileType) || Objects.equals(Integer.TYPE, fileType)) {
            value = LsConvert.obj2Int(value);
          } else if (Objects.equals(Double.class, fileType) || Objects.equals(Double.TYPE, fileType)) {
            value = LsConvert.obj2Double(value);
          } else if (Objects.equals(Float.class, fileType) || Objects.equals(Float.TYPE, fileType)) {
            value = LsConvert.obj2Float(value);
          }
          if (Objects.nonNull(fileType)) {
            ExcelAnnotation excelAnnotation = field.getAnnotation(ExcelAnnotation.class);
            String propertyName = field.getName();
            if (StringUtil.isNotBlank(excelAnnotation.targetAnnotation())) {
              propertyName = propertyName + "." + excelAnnotation.targetAnnotation();
            }
            if (StringUtil.isNotBlank(excelAnnotation.readConvertExp())) {
              value = reverseByExp(LsConvert.obj2Str(value), excelAnnotation.readConvertExp());
            }
            ReflectUtil.invokeSetter(entity, value, propertyName);
          }
        }
        entityList.add(entity);
      }
    }
    return entityList;
  }

  protected ResultEntity export() {
    OutputStream out = null;
    try {
      // 计算一共有几个sheet页
      double sheetNum = Math.ceil(entityList.size() / ExcelStyler.SHEET_MAX_NUM);
      for (int index = 0; index <= sheetNum; index++) {
        createSheet(index);
        // 产生单元格
        Cell cell = null;
        // 产生第一行
        Row row = sheet.createRow(0);
        // 写入各个成员属性的表头名称
        for (int i = 0; i < fields.size(); i++) {
          Field field = fields.get(i);
          ExcelAnnotation excelAnnotation = field.getAnnotation(ExcelAnnotation.class);
          // 创建列
          cell = row.createCell(i);
          cell.setCellType(CellType.STRING);
          CellStyle cellStyle = ExcelStyler.initHeaderStyle(workbook);
          if (excelAnnotation.name().indexOf("注：") >= 0) {
            Font font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            cellStyle.setFont(font);
            cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
            sheet.setColumnWidth(i, 6000);
          } else {
            Font font = workbook.createFont();
            font.setBold(true);
            cellStyle.setFont(font);
            cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
            sheet.setColumnWidth(i, (int) ((excelAnnotation.width() + 0.72) * StringUtil.STRING_BUILDER_SIZE));
            row.setHeight((short) (excelAnnotation.height() * 20));
          }
          cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
          cellStyle.setWrapText(true);
          cell.setCellStyle(cellStyle);
          // 写入列名
          cell.setCellValue(excelAnnotation.name());
          // 设置只能选择不能输入的列内容
          if (excelAnnotation.combo().length > 0) {
            // 设置2-101列只能选择不能输入
            setHSSFValidation(sheet, excelAnnotation.combo(), i, i);
          }
        }
        // 导出时将数据写入到excel中
        if (Objects.equals(type, ExcelAnnotation.Type.EXPORT)) {
          fillExcelData(index, row, cell);
        }
      }
      HttpServletResponse response = ServletUtil.getResponse();
      String fileName = ServletUtil.encodingFileName(sheetName + FileSuffixEnum.XLSX.getSuffix());
      response.setCharacterEncoding(LsCharset.UTF_8);
      response.setContentType("multipart/form-data");
      response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
      out = response.getOutputStream();
      workbook.write(out);
      return ResultUtil.ok(fileName);
    } catch (Exception e) {
      log.error("导出excel异常：{}", e.getMessage());
      throw new LsException("导出excel异常，请联系管理人员");
    } finally {
      CloseableUtil.close(workbook, out);
    }
  }

  /**
   * 获取单元格的值
   *
   * @param row    获取的行
   * @param column 获取单元格列号
   * @return 单元格值
   */
  private Object getCellValue(Row row, int column) {
    if (Objects.nonNull(row)) {
      Object value = StringUtil.EMPTY;
      try {
        Cell cell = row.getCell(column);
        if (Objects.nonNull(cell)) {
          if (Objects.equals(cell.getCellTypeEnum(), CellType.NUMERIC)) {
            value = cell.getNumericCellValue();
            // POI Excel日期格式转换
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
              value = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Double) value);
            } else {
              if ((Double) value % 1 > 0) {
                value = new DecimalFormat("0.00").format(value);
              } else {
                value = new DecimalFormat("0").format(value);
              }
            }
          } else if (Objects.equals(cell.getCellTypeEnum(), CellType.STRING)) {
            value = cell.getStringCellValue();
          } else if (Objects.equals(cell.getCellTypeEnum(), CellType.BOOLEAN)) {
            value = cell.getBooleanCellValue();
          } else if (Objects.equals(cell.getCellTypeEnum(), CellType.ERROR)) {
            value = cell.getErrorCellValue();
          }
        }
        return value;
      } catch (Exception e) {
        return value;
      }
    }
    return null;
  }

  /**
   * 枚举类字段反向解析，例：女=0,男=1,未知=2
   *
   * @param propertyValue  待反向解析的值
   * @param readConvertExp 解析表达式
   * @return
   */
  protected String reverseByExp(String propertyValue, String readConvertExp) {
    try {
      String[] sourceArray = readConvertExp.split(",");
      for (String item : sourceArray) {
        String[] itemArray = item.split("=");
        if (itemArray[1].equals(propertyValue)) {
          return itemArray[0];
        }
      }
    } catch (Exception e) {
      throw new LsException("枚举类数值[" + propertyValue + "]反向解析失败！");
    }
    return propertyValue;
  }

  /**
   * 创建sheet信息
   *
   * @param index 序号
   */
  protected void createSheet(int index) {
    sheet = workbook.createSheet();
    workbook.setSheetName(index, sheetName + "-" + (index + 1));
  }

  /**
   * 设置只能选择不能输入的列内容,设置2-101列只能选择不能输入
   *
   * @param sheet        要设置的sheet
   * @param explicitList 下拉框显示内容
   * @param startCol     开始列
   * @param endCol       结束列
   */
  protected void setHSSFValidation(Sheet sheet, String[] explicitList, int startCol, int endCol) {
    setHSSFValidation(sheet, explicitList, 1, 100, startCol, endCol);
  }

  /**
   * 设置只能选择不能输入的列内容
   *
   * @param sheet        要设置的sheet
   * @param explicitList 下拉框显示内容
   * @param startRow     开始行
   * @param endRow       结束行
   * @param startCol     开始列
   * @param endCol       结束列
   */
  protected void setHSSFValidation(Sheet sheet, String[] explicitList, int startRow, int endRow, int startCol, int endCol) {
    DataValidationHelper helper = sheet.getDataValidationHelper();
    // 加载下拉列表内容
    DataValidationConstraint constraint = helper.createExplicitListConstraint(explicitList);
    CellRangeAddressList regions = new CellRangeAddressList(startRow, endRow, startCol, endCol);
    // 数据有效性验证
    DataValidation dataValidation = helper.createValidation(constraint, regions);
    // 处理excel兼容性问题
    if (dataValidation instanceof XSSFDataValidation) {
      dataValidation.setSuppressDropDownArrow(true);
      dataValidation.setShowErrorBox(true);
    } else {
      dataValidation.setSuppressDropDownArrow(false);
    }
    sheet.addValidationData(dataValidation);
  }

  /**
   * 填充excel数据
   *
   * @param index 序号
   * @param row   单元格行
   * @param cell  类型单元格
   */
  protected void fillExcelData(int index, Row row, Cell cell) {
    int startNo = index * ExcelStyler.SHEET_MAX_NUM;
    int endNo = Math.min(startNo + ExcelStyler.SHEET_MAX_NUM, entityList.size());
    // 写入各条记录，每条记录对应excel中的一行
    CellStyle cellStyle = ExcelStyler.initDataStyle(workbook);
    try {
      for (int i = startNo; i < endNo; i++) {
        row = sheet.createRow(i + 1 - startNo);
        // 获取导出对象
        T entity = entityList.get(i);
        for (int j = 0; j < fields.size(); j++) {
          Field field = fields.get(j);
          // 设置实体类私有属性可访问
          field.setAccessible(true);
          ExcelAnnotation excelAnnotation = field.getAnnotation(ExcelAnnotation.class);
          row.setHeight((short) (excelAnnotation.height() * 20));
          if (excelAnnotation.isExport()) {
            // 创建cell
            cell = row.createCell(j);
            cell.setCellStyle(cellStyle);
            // 数据存在就填入，不存在填空格
            if (Objects.isNull(entity)) {
              cell.setCellValue(StringUtil.EMPTY);
              continue;
            }
            // 获取对象中的属性
            Object value = getTargetValue(entity, field, excelAnnotation);
            String dateFormat = excelAnnotation.dateFormat();
            String readConvertExp = excelAnnotation.readConvertExp();
            if (StringUtil.isNotBlank(dateFormat)) {
              cell.setCellValue(DateUtil.formatDate((Date) value, DateEnum.getDateEnum(dateFormat)));
            } else if (StringUtil.isNotBlank(readConvertExp)) {
              cell.setCellValue(convertByExp(LsConvert.obj2Str(value), readConvertExp));
            } else {
              cell.setCellType(CellType.STRING);
              cell.setCellValue(Objects.isNull(value) ? excelAnnotation.defaultValue() : value + excelAnnotation.suffix());
            }
          }
        }
      }
    } catch (Exception e) {
      log.error("excel导出数据填充失败：{}", e.getMessage());
    }
  }

  /**
   * 获取对象中的属性
   *
   * @param entity          实体对象
   * @param field           成员属性
   * @param excelAnnotation 注解信息
   * @return
   */
  protected Object getTargetValue(T entity, Field field, ExcelAnnotation excelAnnotation) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    Object obj = field.get(entity);
    String targetAnnotation = excelAnnotation.targetAnnotation();
    if (StringUtil.isNotBlank(targetAnnotation)) {
      if (targetAnnotation.indexOf(".") > -1) {
        String[] targetAnnotations = targetAnnotation.split("[.]");
        for (String annotation : targetAnnotations) {
          obj = getValue(obj, annotation);
        }
      } else {
        obj = getValue(obj, targetAnnotation);
      }
    }

    return obj;
  }

  /**
   * 获取类成员属性的值
   *
   * @param obj  对象类
   * @param name 成员属性名称
   * @return
   */
  protected Object getValue(Object obj, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    if (StringUtil.isNotBlank(name)) {
      Class<?> clz = obj.getClass();
      String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
      Method method = clz.getMethod(methodName);
      obj = method.invoke(obj);
    }
    return obj;
  }

  /**
   * 读取内容转表达式，如：0=男,1=女,2=未知
   *
   * @param propertyValue  参数值
   * @param readConvertExp 待翻译的注解
   * @return 解析后的值
   */
  protected String convertByExp(String propertyValue, String readConvertExp) {
    try {
      String[] convertSource = readConvertExp.split(",");
      for (String exp : convertSource) {
        String[] expArray = exp.split("=");
        if (expArray[0].equals(propertyValue)) {
          return expArray[1];
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return propertyValue;
  }
}
