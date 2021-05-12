package com.sand.core.util.file;

/**
 * 功能说明：文件处理 <br>
 * 开发人员：hsh <br>
 * 开发时间：2021/5/12 17:39 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class FileUtil {

    /**
     * <p>
     * 功能描述：获取文件大小
     * </p>
     * 开发人员：hsh
     * 开发时间：2021/5/12 17:41
     * 修改记录：新建
     *
     * @param size size
     * @return java.lang.String
     */
    public static String getFileSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return size + "B";
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位，因为还没有到达要使用另一个单位的时候，接下去以此类推
        if (size < 1024) {
            return size + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，因此，把此数乘以100之后再取余
            size = size * 100;
            return size / 100 + "." + size % 100 + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return size / 100 + "." + size % 100 + "GB";
        }
    }
}