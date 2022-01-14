package com.sand.fastdfs.utils;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 功能说明：fastDFS工具类 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/1/14 10:56 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class FastDFSClient {
    private static final String CONF_FILENAME = Objects.requireNonNull(Thread.currentThread()
            .getContextClassLoader()
            .getResource(""))
            .getPath() + "fastdfs-client.conf";
    private static StorageClient storageClient = null;

    // 只加载一次
    static {
        try {
            ClientGlobal.init(CONF_FILENAME);
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            storageClient = new StorageClient(trackerServer, storageServer);
        } catch (Exception e) {
            System.out.println("初始化 fastDFS 失败，请检查！");
            e.printStackTrace();
        }
    }

    private FastDFSClient() {
    }

    /**
     * @param inputStream 上传的文件输入流
     * @param fileName    上传的文件原始名
     * @return 文件存储路径
     */
    public static String[] uploadFile(InputStream inputStream, String fileName) {
        try {
            // 文件的元数据
            NameValuePair[] metaList = new NameValuePair[2];
            // 第一组元数据，文件的原始名称
            metaList[0] = new NameValuePair("file name", fileName);
            // 第二组元数据
            metaList[1] = new NameValuePair("file length", inputStream.available() + "");
            // 准备字节数组
            byte[] fileBuff = null;
            if (inputStream != null) {
                // 查看文件的长度
                int len = inputStream.available();
                // 创建对应长度的字节数组
                fileBuff = new byte[len];
                // 将输入流中的字节内容，读到字节数组中。
                inputStream.read(fileBuff);
            }
            // 上传文件。参数含义：要上传的文件的内容（使用字节数组传递），上传的文件的类型（扩展名），元数据
            return storageClient.upload_file(fileBuff, getFileExt(fileName), metaList);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @param file     文件
     * @param fileName 文件名
     * @return 返回Null则为失败
     */
    public static String[] uploadFile(File file, String fileName) {
        // 初始化new NameValuePair[0]
        NameValuePair[] metaList = null;
        try (
                FileInputStream fis = new FileInputStream(file)
        ) {
            byte[] fileBuff = null;
            if (fis != null) {
                int len = fis.available();
                fileBuff = new byte[len];
                fis.read(fileBuff);
            }
            return storageClient.upload_file(fileBuff, getFileExt(fileName), metaList);
        } catch (Exception e) {
            return new String[0];
        }
    }

    /**
     * 根据组名和远程文件名来删除一个文件
     *
     * @param groupName      例如 "group1" 如果不指定该值，默认为group1
     * @param remoteFileName 例如"M00/00/00/wKgxgk5HbLvfP86RAAAAChd9X1Y736.jpg"
     * @return 0为成功，非0为失败，具体为错误代码
     */
    public static int deleteFile(String groupName, String remoteFileName) {
        try {
            return storageClient.delete_file(groupName == null ? "group1" : groupName, remoteFileName);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 修改一个已经存在的文件
     *
     * @param oldGroupName 旧的组名
     * @param oldFileName  旧的文件名
     * @param file         新文件
     * @param fileName     新文件名
     * @return 返回空则为失败
     */
    public static String[] modifyFile(String oldGroupName, String oldFileName, File file, String fileName) {
        String[] fileids = null;
        try {
            // 先上传
            fileids = uploadFile(file, fileName);
            if (fileids == null) {
                return new String[0];
            }
            // 再删除
            int delResult = deleteFile(oldGroupName, oldFileName);
            if (delResult != 0) {
                return new String[0];
            }
        } catch (Exception ex) {
            return new String[0];
        }
        return fileids;
    }

    /**
     * 文件下载
     *
     * @param groupName      卷名
     * @param remoteFileName 文件名
     * @return 返回一个流
     */
    public static InputStream downloadFile(String groupName, String remoteFileName) {
        try {
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            return new ByteArrayInputStream(bytes);
        } catch (Exception ex) {
            return null;
        }
    }

    public static NameValuePair[] getMetaDate(String groupName, String remoteFileName) {
        try {
            return storageClient.get_metadata(groupName, remoteFileName);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new NameValuePair[0];
        }
    }

    /**
     * 获取文件后缀名（不带点）.
     *
     * @return 如："jpg" or "".
     */
    private static String getFileExt(String fileName) {
        if (StringUtils.isBlank(fileName) || !fileName.contains(".")) {
            return "";
        } else {
            // 不带最后的点
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
    }
}
