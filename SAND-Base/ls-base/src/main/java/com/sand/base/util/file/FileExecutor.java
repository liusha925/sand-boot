/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/10   liusha      新增
 * =========  ===========  =====================
 */
package com.sand.base.util.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 功能说明：文件处理执行接口
 * 开发人员：@author liusha
 * 开发日期：2019/8/10 20:15
 * 功能描述：文件处理执行接口
 */
public interface FileExecutor {
  /**
   * 复制文件，可以复制单个文件或文件夹
   *
   * @param srcFilePathName 源文件
   * @param tarFilePathName 目标文件
   * @return 如果复制成功 ，则返回true，否是返回false
   * @throws IOException the io exception
   */
  boolean copy(String srcFilePathName, String tarFilePathName) throws IOException;

  /**
   * 复制单个文件
   *
   * @param srcFilePathName 源文件
   * @param tarFilePathName 目标文件
   * @param isCover         是否允许原（目标）文件覆盖
   * @return 如果复制成功 ，则返回true，否则返回false
   * @throws IOException the io exception
   */
  boolean copyFile(String srcFilePathName, String tarFilePathName, boolean isCover) throws IOException;

  /**
   * 复制整个目录的内容，如果目标目录存在，则不覆盖
   *
   * @param srcDirPathName 源目录
   * @param tarDirPathName 目标目录
   * @param isCover        是否允许原（目标）目录覆盖
   * @return 如果复制成功 ，则返回true，否则返回false
   * @throws IOException the io exception
   */
  boolean copyDirectory(String srcDirPathName, String tarDirPathName, boolean isCover) throws IOException;

  /**
   * 复制整个目录的内容
   *
   * @param folder     源目录
   * @param tarDirName 目的地址
   * @return 如果复制成功 ，则返回true，否则返回false
   * @throws IOException the io exception
   */
  boolean copyFolder(File folder, String tarDirName) throws IOException;

  /**
   * 文件最终都是通过流操作的
   *
   * @param is InputStream 输入流
   * @param os OutputStream 输出流
   * @throws IOException the io exception
   */
  void copyStream(InputStream is, OutputStream os) throws IOException;

  /**
   * 删除文件，可以删除单个文件或文件夹
   *
   * @param filePathName 被删除的文件
   * @return 如果删除成功 ，则返回true，否是返回false
   */
  boolean deleteFile(String filePathName);

  /**
   * 删除目录及目录下的文件
   *
   * @param dirPathName 被删除的目录所在的文件路径
   * @return 如果目录删除成功 ，则返回true，否则返回false
   */
  boolean deleteDirectory(String dirPathName);

  /**
   * 清空目录
   *
   * @param folder 目标目录
   * @return 是否清除成功
   */
  boolean clearFolder(File folder);

  /**
   * 压缩文件
   *
   * @param srcFilePathName 压缩前的文件路径名称
   * @param tarFilePathName 压缩后的文件路径名称
   * @throws IOException the io exception
   */
  void zipFile(String srcFilePathName, String tarFilePathName) throws IOException;

  /**
   * 文件最终都是通过流操作的
   *
   * @param is       输入流
   * @param os       输出流
   * @param buffSize
   * @throws IOException the io exception
   */
  void subCopyStream(InputStream is, OutputStream os, int buffSize) throws IOException;

  /**
   * 文件下载
   *
   * @param filePath 文件路径
   * @param fileName 文件名称
   * @throws IOException the io exception
   */
  void subDownLoadFile(String filePath, String fileName) throws IOException;

  /**
   * 删除单个文件
   *
   * @param filePathName 被删除的文件名
   * @return 如果删除成功 ，则返回true，否则返回false
   */
  boolean subDeleteFile(String filePathName);
}
