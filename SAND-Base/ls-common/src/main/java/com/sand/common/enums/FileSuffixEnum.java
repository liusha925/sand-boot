/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/1    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能说明：文件类型枚举
 * 开发人员：@author liusha
 * 开发日期：2019/9/1 8:16
 * 功能描述：文件类型枚举
 */
@Getter
@AllArgsConstructor
public enum FileSuffixEnum {
  // 文件后缀
  TXT(".txt"),
  HTML(".html"),
  ZIP(".zip"),
  EXE(".exe"),
  PDF(".pdf"),
  RM(".rm"),
  AVI(".avi"),
  TMP(".tmp"),
  MDF(".mdf"),
  XLS(".xls"),
  XLSX(".xlsx"),
  DOC(".doc"),
  DOCX(".docx");
  private final String suffix;
}
