/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/10   nevercoming      新增
 * =========  ===========  =====================
 */
package com.sand.base.util.file.concrete;

import com.sand.base.util.file.AbstractFileExecutor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 功能说明：非阻塞IO
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/10 22:13
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
public class NIOFileExecutor extends AbstractFileExecutor {

  @Override
  public void subCopyStream(InputStream is, OutputStream os, int buffSize) throws IOException {
    try (
        FileChannel inFileChannel = ((FileInputStream) is).getChannel();
        FileChannel outFileChannel = ((FileOutputStream) os).getChannel()
    ) {
      while (inFileChannel.position() != inFileChannel.size()) {
        if ((inFileChannel.size() - inFileChannel.position()) < buffSize) {
          // 读取剩下的
          buffSize = (int) (inFileChannel.size() - inFileChannel.position());
        }
        inFileChannel.transferTo(inFileChannel.position(), buffSize, outFileChannel);
        // 内存映像复制
        MappedByteBuffer inBuffer = inFileChannel.map(FileChannel.MapMode.READ_ONLY, inFileChannel.position(), buffSize);
        outFileChannel.write(inBuffer);

        inFileChannel.position(inFileChannel.position() + buffSize);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean subDeleteFile(String filePathName) {
    Path path = Paths.get(filePathName);
    try {
      return Files.deleteIfExists(path);
    } catch (IOException e) {
      return false;
    }
  }
}
