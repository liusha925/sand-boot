/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/10   liusha      新增
 * =========  ===========  =====================
 */
package com.sand.common.util.file.concrete;

import com.sand.common.constant.Constant;
import com.sand.common.util.convert.SandCharset;
import com.sand.common.exception.BusinessException;
import com.sand.common.util.file.AbstractFileExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 功能说明：非阻塞IO
 * 开发人员：@author liusha
 * 开发日期：2019/8/10 22:13
 * 功能描述：非阻塞IO
 */
@Component
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
  public void subDownLoadFile(String filePath, String fileName) throws IOException {
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletResponse response = servletRequestAttributes.getResponse();
    HttpServletRequest request = servletRequestAttributes.getRequest();
    File file = new File(filePath);
    if (file.exists()) {
      String showName;
      // 浏览器兼容
      String userAgent = request.getHeader("user-agent").toLowerCase();
      if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
        // IE
        showName = URLEncoder.encode(fileName, SandCharset.UTF_8);
      } else {
        // 非IE
        showName = new String(fileName.getBytes(SandCharset.UTF_8), SandCharset.ISO_8859_1);
      }
      // 设置Content-Type为文件的MimeType
      response.setContentType("application/octet-stream");
      // 设置文件名
      response.addHeader("Content-Disposition", "attachment;filename=" + showName);
      response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
      int bufferSize = Constant.BUFF_SIZE;
      // 6x128 KB = 768KB byte buffer
      ByteBuffer buff = ByteBuffer.allocateDirect(786432);
      try (
          FileInputStream fileInputStream = new FileInputStream(file);
          FileChannel fileChannel = fileInputStream.getChannel();
          ServletOutputStream fileOutputStream = response.getOutputStream()
      ) {
        response.setContentLengthLong(fileChannel.size());
        byte[] byteArr = new byte[bufferSize];
        int nRead, nGet;
        while ((nRead = fileChannel.read(buff)) != -1) {
          if (nRead == 0) {
            continue;
          }
          buff.position(0);
          buff.limit(nRead);
          while (buff.hasRemaining()) {
            nGet = Math.min(buff.remaining(), bufferSize);
            buff.get(byteArr, 0, nGet);
            fileOutputStream.write(byteArr, 0, nGet);
          }
          buff.clear();
        }
      } catch (IOException e) {
        throw new BusinessException("文件下载失败");
      }
    } else {
      throw new BusinessException("文件不存在");
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
