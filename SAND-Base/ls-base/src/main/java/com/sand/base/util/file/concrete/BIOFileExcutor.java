/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/19   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.file.concrete;

import com.sand.base.util.file.AbstractFileExecutor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * 功能说明：阻塞IO
 * 开发人员：@author liusha
 * 开发日期：2019/8/19 17:53
 * 功能描述：阻塞IO
 */
@Slf4j
public class BIOFileExcutor extends AbstractFileExecutor {
  @Override
  public void subCopyStream(InputStream is, OutputStream os, int buffSize) throws IOException {
    byte[] buf = new byte[buffSize];
    int c;
    try {
      while ((c = is.read(buf)) != -1) {
        os.write(buf, 0, c);
      }
    } finally {
      if (Objects.nonNull(is)) {
        is.close();
      }
    }
  }

  @Override
  public void subDownLoadFile(String filePath, String fileName) throws IOException {

  }

  @Override
  public boolean subDeleteFile(String filePathName) {
    File file = new File(filePathName);
    if (file.exists() && file.isFile()) {
      if (file.delete()) {
        log.debug("删除文件 {} 成功!", filePathName);
        return true;
      } else {
        log.debug("删除文件 {} 失败!", filePathName);
        return false;
      }
    } else {
      log.debug(String.format("%s 文件不存在!", filePathName));
      return true;
    }
  }
}
