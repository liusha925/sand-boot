/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/10   liusha      新增
 * =========  ===========  =====================
 */
package com.sand.core.util.file.concrete;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 功能说明：文件压缩执行类
 * 开发人员：@author liusha
 * 开发日期：2019/8/10 22:33
 * 功能描述：文件压缩执行类
 */
public class ZipExecutor extends SimpleFileVisitor<Path> {
  private ZipOutputStream zos;

  private Path srcPath;

  public ZipExecutor(Path srcPath, ZipOutputStream zos) {
    this.srcPath = srcPath;
    this.zos = zos;
  }

  @Override
  public FileVisitResult visitFile(Path filePath, BasicFileAttributes attributes) throws IOException {
    try {
      Path targetFile = srcPath.relativize(filePath);
      zos.putNextEntry(new ZipEntry(targetFile.toString()));
      byte[] bytes = Files.readAllBytes(filePath);
      zos.write(bytes, 0, bytes.length);
      zos.closeEntry();
    } catch (IOException ex) {
      System.err.println(ex);
    }

    return FileVisitResult.CONTINUE;
  }
}
