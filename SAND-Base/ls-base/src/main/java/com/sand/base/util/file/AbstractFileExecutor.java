/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/10   nevercoming      新增
 * =========  ===========  =====================
 */
package com.sand.base.util.file;

import com.sand.base.constant.Constant;
import com.sand.base.util.file.concrete.ZipExecutor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 功能说明：定义文件执行抽象类（模板方法）
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/10 20:33
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
public abstract class AbstractFileExecutor implements FileExecutor {

  /**
   * 模板方法：第一步，预定义操作（文件拷贝或者文件夹拷贝）
   *
   * @param srcFilePathName 源文件
   * @param tarFilePathName 目标文件
   * @return
   * @throws IOException
   */
  @Override
  public boolean copy(String srcFilePathName, String tarFilePathName) throws IOException {
    File file = new File(srcFilePathName);
    if (!file.exists()) {
      log.debug(String.format("%s 文件不存在!", srcFilePathName));
      return false;
    } else {
      if (file.isFile()) {
        return !copyFile(srcFilePathName, tarFilePathName, false);
      } else {
        return !copyDirectory(srcFilePathName, tarFilePathName, false);
      }
    }
  }

  /**
   * 模板方法：第二步，文件拷贝，是否允许目标文件覆盖
   *
   * @param srcFilePathName 源文件
   * @param tarFilePathName 目标文件
   * @param isCover         是否允许原（目标）文件覆盖
   * @return
   * @throws IOException
   */
  @Override
  public boolean copyFile(String srcFilePathName, String tarFilePathName, boolean isCover) throws IOException {
    File srcFile = new File(srcFilePathName);
    // 判断源文件是否存在
    if (!srcFile.exists()) {
      throw new FileNotFoundException(String.format("复制文件失败，源文件 %s 不存在!", srcFilePathName));
      // 判断源文件是否是合法的文件
    } else if (!srcFile.isFile()) {
      throw new FileNotFoundException(String.format("复制文件失败，%s 不是一个文件!", srcFilePathName));
    }
    File tarFile = new File(tarFilePathName);
    // 判断目标文件是否存在
    if (tarFile.exists()) {
      // 是否允许覆盖
      if (isCover) {
        if (deleteFile(tarFilePathName)) {
          throw new IOException(String.format("删除目标文件 %s 失败!", tarFilePathName));
        }
      } else {
        throw new FileAlreadyExistsException(String.format("复制文件失败，目标文件 %s 已存在!", tarFilePathName));
      }
    } else {
      // 目标文件不存在就去创建
      if (!tarFile.getParentFile().exists() && !tarFile.getParentFile().mkdirs()) {
        throw new IOException("创建目标文件所在的目录失败!");
      }
    }
    // 准备复制文件
    try (
        InputStream is = new FileInputStream(srcFile);
        OutputStream os = new FileOutputStream(tarFile)
    ) {
      copyStream(is, os);
      return true;
    } catch (Exception e) {
      log.warn("复制文件失败：", e);
      return false;
    }
  }

  /**
   * 模板方法：第二步，文件夹（目录）拷贝，是否允许目标文件夹覆盖
   *
   * @param srcDirPathName 源目录
   * @param tarDirPathName 目标目录
   * @param isCover        是否允许原（目标）目录覆盖
   * @return
   * @throws IOException
   */
  @Override
  public boolean copyDirectory(String srcDirPathName, String tarDirPathName, boolean isCover) throws IOException {
    File srcDir = new File(srcDirPathName);
    // 判断源目录是否存在
    if (!srcDir.exists()) {
      throw new FileNotFoundException(String.format("复制目录失败，源目录 %s 不存在!", srcDirPathName));
      // 判断源目录是否是目录
    } else if (!srcDir.isDirectory()) {
      throw new FileNotFoundException(String.format("复制目录失败，%s 不是一个目录!", srcDirPathName));
    }
    // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
    String tarDirPathNames = tarDirPathName;
    if (!tarDirPathNames.endsWith(File.separator)) {
      tarDirPathNames = tarDirPathNames + File.separator;
    }
    File tarDir = new File(tarDirPathNames);
    // 如果目标文件夹存在
    if (tarDir.exists()) {
      if (isCover) {
        // 允许覆盖目标目录
        if (deleteFile(tarDirPathNames)) {
          throw new IOException(String.format("删除目录 %s 失败!", tarDirPathNames));
        }
      } else {
        throw new FileAlreadyExistsException(String.format("目标目录复制失败，目标目录 %s 已存在!", tarDirPathNames));
      }
    } else {
      if (!tarDir.mkdirs()) {
        throw new IOException("创建目标目录失败!");
      }
    }
    return copyFolder(srcDir, tarDirPathName);
  }

  /**
   * 模板方法：第三步，对流操作（具体实现将由子类去完成）
   *
   * @param is 输入流
   * @param os 输出流
   * @throws IOException
   */
  @Override
  public void copyStream(InputStream is, OutputStream os) throws IOException {
    // 具体实现由子类完成
    subCopyStream(is, os, Constant.BUFF_SIZE);
  }

  /**
   * 模板方法：第三步，对文件夹操作
   *
   * @param folder     源目录
   * @param tarDirName 目的地址
   * @return
   * @throws IOException
   */
  @Override
  public boolean copyFolder(File folder, String tarDirName) throws IOException {
    File[] files = folder.listFiles();
    if (files != null) {
      for (File file : files) {
        // 如果是一个文件，则复制文件
        if (file.isFile() && !copyFile(file.getAbsolutePath(), tarDirName + file.getName(), false)) {
          return false;
        }
        // 如果是一个文件夹，则复制文件夹
        if (file.isDirectory() && !copyDirectory(file.getAbsolutePath(), tarDirName + file.getName(), false)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean deleteFile(String filePathName) {
    File file = new File(filePathName);
    if (!file.exists()) {
      log.debug(String.format("%s 文件不存在!", filePathName));
      return false;
    } else {
      if (file.isFile()) {
        // 具体实现由子类完成
        return subDeleteFile(filePathName);
      } else {
        return deleteDirectory(filePathName);
      }
    }
  }

  @Override
  public boolean deleteDirectory(String dirPathName) {
    String dirPathNames = dirPathName;
    if (!dirPathNames.endsWith(File.separator)) {
      dirPathNames = dirPathNames + File.separator;
    }
    File dirFile = new File(dirPathNames);
    if (!dirFile.exists() || !dirFile.isDirectory()) {
      log.debug("{} 目录不存在!", dirPathNames);
      return true;
    }
    if (clearFolder(dirFile) && dirFile.delete()) {
      log.debug("删除目录 {} 成功!", dirPathName);
      return true;
    } else {
      log.debug("删除目录 {} 失败!", dirPathName);
      return false;
    }
  }

  @Override
  public boolean clearFolder(File folder) {
    File[] files = folder.listFiles();
    if (files != null) {
      for (File file : files) {
        // 如果是一个文件，则删除文件
        if (file.isFile() && !deleteFile(file.getAbsolutePath())) {
          return false;
        }
        // 如果是一个文件，则删除文件夹
        if (file.isDirectory() && !deleteDirectory(file.getAbsolutePath())) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public void zipFile(String srcFilePathName, String tarFilePathName) throws IOException {
    File file = new File(srcFilePathName);
    if (!file.exists()) {
      log.debug(String.format("%s 文件不存在!", srcFilePathName));
    } else {
      if (file.isFile()) {
        FileOutputStream fos = new FileOutputStream(tarFilePathName);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zos.putNextEntry(new ZipEntry(file.getName()));
        byte[] bytes = Files.readAllBytes(Paths.get(srcFilePathName));
        zos.write(bytes, 0, bytes.length);
        zos.closeEntry();
        zos.close();
      } else {
        File tarFile = new File(tarFilePathName);
        Path srcPath = Paths.get(srcFilePathName);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tarFile));
        ZipExecutor zipExecutor = new ZipExecutor(srcPath, zos);
        Files.walkFileTree(srcPath, zipExecutor);
        zos.close();
      }
    }
  }

}
