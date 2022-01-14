package com.sand.fastdfs.utils;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * 功能说明： <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/1/14 11:23 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class FastDFSClientTest {

    @Test
    public void uploadFile() {
        File file = new File("C:/Work/tmp/images/upload.jpg");
        try (InputStream is = new FileInputStream(file)) {
            String fileName = UUID.randomUUID() + ".jpg";
            String[] result = FastDFSClient.uploadFile(is, fileName);
            System.out.println(Arrays.toString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadFile() {
        try (
                InputStream is = FastDFSClient.downloadFile("group1",
                        "M00/00/00/rB0oy2HhMTWAb5LrAAAuiPTgr4Y422.jpg");
                OutputStream os = new FileOutputStream("C:/Work/tmp/images/download.jpg");
        ) {
            int index = 0;
            while ((index = is.read()) != -1) {
                os.write(index);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
