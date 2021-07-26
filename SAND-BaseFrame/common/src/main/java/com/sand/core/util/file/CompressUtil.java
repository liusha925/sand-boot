package com.sand.core.util.file;

import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 功能说明：压缩解压工具 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2021/7/26 8:52 <br>
 * 功能描述：
 * ZIP文件格式[1]ZIP，是一个计算机文件的压缩的算法，原名Deflate（真空），发明者为菲尔·卡茨（Phil
 * Katz)），他于1989年1月公布了该格式的资料。ZIP通常使用后缀名“.zip”，它的MIME格式为 application/zip
 * 。目前，ZIP格式属于几种主流的压缩格式之一，其竞争者包括RAR格式以及开放源码的7-Zip格式。从性能上比较，RAR格式较ZIP格式压缩率较高，
 * 而7-Zip由于提供了免费的压缩工具而逐渐在更多的领域得到应用。 WinMount可以把ZIP挂载到虚拟盘，无需解压，随即读取，这又是另一大创新。
 * 编辑本段ZIP文件详细信息前身 1985年一家名为SEA（System Enhancement
 * Associates，系统增强伙伴）的小公司开发了一个在MS-DOS平台下的商业压缩软件，名为ARC。当时的软体发行方式与现在略有不同，
 * 用户购买了软体，除了得到软体的可执行文件还包括一份C语言的源代码。当时的卡茨与很多用计算机的平民一样，缺乏资金购买大量的商业软件，
 * 当时卡茨从网上下载了一份ARC的C语言源代码，并用汇编语言将其全新编写并编译出来。卡茨将这个软体名为：PKARC（Phillip Katz'
 * ARC）。卡茨制作的新软体PKARC因为是使用汇编语言重新编写，因为是参照源代码编写的，所以完全兼容ARC以及性能上比ARC高。
 * 卡茨当时将这个新软件上传到网络上面。显然，卡茨此举造成对SEA公司的侵权。SEA最初希望通过联络卡茨使PKARC成为SEA公司旗下的一款产品，
 * 后来卡茨拒绝了。最终，双方对簿公堂，结果是卡茨败诉，卡茨被判以对SEA公司的赔款以及停止发放PKARC。后来，
 * 卡茨在研发过程中的PKARC续作PKPRC也被迫重新改写所有代码，PKPRC其实就是下文提及的PKZIP的前身。 诞生
 * 这场官司过后几周，卡茨就制作出新压缩软件PKZIP(Phillip Katz'
 * ZIP)，这款全新的软体比ARC的压缩率，压缩性能以及功能都要高许多。此后，卡茨还将ZIP的所有技术参数公诸于众。这一名称zip（含义“速度”）
 * 是由卡茨的朋友罗伯特·马宏利（Robert
 * Mahoney）建议的。他们想暗示，他们的产品比ARC在一定时间内更快速。这个名字往往被写成大写字母，因为在DOS系统内，
 * 通常都是使用大写字母作为后缀名的。（由于MS-DOS运行在FAT文件系统上） Winzip的面世 Windows 3
 * 面世之前，有两种格式与ZIP同样流行，一个是LHA（LHArc），另一个是ARJ（Archiver Robert
 * Jung），直至到1995年，这3种压缩格式都是PC应用的主流。之后，1995年，微软发布了Windows95，
 * 当时从DOS转移到Windows的用户们，极度渴求对图形界面下的优良软件，Winzip以其优良的性能以及不太羞涩的图形用户界面吸引了用户的目光，
 * 在当时占有大量的市场占有量，其实，当时的Winzip仅仅是一个调用DOS资源的GUI外壳，但是其从Windows
 * 3就开始的制作经验使其GUI性能和外观都比当时的流行软件要好。很快的，Winzip就成为了当时一个很流行的软件。同时也带动了ZIP的传播，
 * 由于在前期太受欢迎，使到后期很多用户都以为，是WinZip创造了ZIP，其实这是一个误解，关于Winzip更详细的资讯，参见Winzip。 发展
 * 因为格式开放而且免费。越来越多的软件内嵌支持打开Zip文件。这时，Zip文件越来越象一个经过压缩的透明文件夹。 自Windows
 * Me以来，Windows内嵌支持打开以及压缩Zip文件。 一些下载软体的工具，支持部分下载Zip文件然后进行恢复。
 * 越来越多的软件内嵌支持打开Zip文件。 几乎所有的压缩软体都支持打开及制作Zip文件。 危机
 * 基本上，Zip文件的发展都是由PKware公司与Winzip所推动。然而，其两家公司就某些问题上互相猜疑，导致发展缓慢。人们目前最想在
 * Zip文件实现的目标，就是加强目前Zip文件的加密能力。就目前而言，Zip的文件加密能力弱得可怜，仅凭单单一个口令保护，根本满足不了安全需求。
 * 虽然卡兹在生时公开了格式，但是当时制作的时候留有为日后升级的空间。而Winzip仅仅是一个使用者，根本无法发布新标准，其标准的制订权依然保留在
 * PKware手中。2002年时，PKware开发了支持256位AES加密的PKZIP 5.0，但是Winzip在2003年发布的Winzip
 * 9却被证明了无法与之相容。双方都互相指责对方违背了Zip的自由开放精神。这是Zip自诞生日起，第一个对于它来说最严峻的挑战。 特色
 * 使用任何一种文本编辑器打开Zip文件，都会看到头2字母为：PK 技术
 * ZIP是一种相当简单的分别压缩每个文件的存档格式。分别压缩文件允许不必读取另外的数据而检索独立的文件；理论上，
 * 这种格式允许对不同的文件使用不同的算法。不管用何种方法，对这种格式的一个告诫是对于包含很多小文件的时候，存档会明显的比压缩成一个独立的文件（
 * 在类Unix系统中一个经典的例子是普通的tar.gz存档是由一个使用gzip压缩的TAR存档组成）要大。
 * ZIP的规则指出文件可以不经压缩或者使用不同的压缩算法来存储。然而，在实际上，ZIP几乎差不多总是在使用卡茨（Katz）的DEFLATE算法。
 * ZIP支持基于对称加密系统的一个简单的密码，现在已知有严重的缺陷，已知明文攻击，字典攻击和暴力攻击。ZIP也支持分卷压缩。
 * 在近来一段时间，ZIP加入了包括新的压缩和加密方法的新特征，不过这些新特征并没有被许多工具所支持并且没有得到广泛应用。 压缩方法
 * 用来对比压缩大小使用的是[1]的内容和最大压缩比。 Shrinking（方法1）
 * 收缩（Shrinking）是LZW的微小调整的一个异体，同样也受到LZW专利问题的影响。从来没有明确的是这项专利是否涵盖反收缩，
 * 不过一些开放源码的项目（例如Info-ZIP）决定谨慎行事，在默认的构造里不包含反收缩的支持。 Reducing（方法2-5）
 * 缩小（Reducing）包括压缩重复字节序列的组合，然后应用一个基于概率的编码得到结果。 Imploding（方法6）
 * 爆聚（Imploding）包括使用一个滑动窗口压缩重复字节序列，然后使用多重Shannon-Fano树压缩得到结果。
 * Tokenizing（方法7） 令牌化（Tokenizing）的方法数是保留的。PKWARE规约没有为其定义一个算法。
 * Deflate和增强的Deflate（方法8和9）
 * 这些方法使用众所周知的Deflate算法。Deflate允许最大32K的窗口。增强的Deflate允许最大64K的窗口。增强版完成任务稍稍成功一些
 * ，但是并没有被广泛的支持。 Deflate比较尺寸是52.1MiB（使用pkzip for Windows，版本8.00.0038测试）
 * 增强的Deflate比较尺寸是52.8MiB（使用pkzip for Windows，版本8.00.0038测试） PKWARE Data
 * Compression Library Imploding（方法10） PKWARE数据压缩库爆聚（PKWARE Data Compression
 * Library Imploding），官方ZIP格式规约就此没有给出更多的信息。 比较尺寸是61.6MiB（使用pkzip for
 * Windows，版本8.00.0038测试，选择二进制模式） 方法11 此方法被PKWARE保留。 Bzip2（方法12）
 * 此方法使用众所周知的bzip2算法。此算法比deflate高效但是并没有被（基于Windows平台的）工具所支持。
 * 比较尺寸是50.6MiB（使用pkzip for Windows，版本8.00.0038测试）
 * 编辑本段与rar文件的区别将网络号映射到区域名的AppleTalk会话层协议。ZIP是被NBP用来决定哪些网络包含属于某一个区域的节点。
 * ZIP的历史比RAR长久.但是压缩方面比较小. ZIP,一个强大并且易用的压缩格式, 支持 ZIP、CAB、TAR、GZIP、MIME,
 * 以及更多格式的压缩文件. 其特点是紧密地与 Windows 资源管理器拖放集成, 不用离开资源管理器而进行压缩 / 解压缩. 包括 WinZip
 * 向导 和 WinZip 自解压缩器个人版本. 区别一、zip的安装比较大，并仅仅有英文版+汉化包 rar有官方的简体中文版，并且安装很小，不足一兆
 * 区别二、zip的压缩速度比rar要快4倍以上，而压缩率仅差1%
 * 区别三、国外很多都采用zip，因为它是免费的，rar不是免费的，在国内很流行是由于有盗版的存在；
 * <br>
 */
public class CompressUtil {
    /**
     * <p>
     * 功能描述：采用zip压缩算法对目录下面的文件进行压缩
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 8:53
     * 修改记录：新建
     *
     * @param file 文件
     * @param dist dist
     */
    public static void zipCompress(File file, File dist) throws IOException, ArchiveException {
        compress(file, dist, ArchiveStreamFactory.ZIP);
    }

    /**
     * <p>
     * 功能描述：采用zip压缩算法对目录下面的文件进行压缩
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 8:54
     * 修改记录：新建
     *
     * @param file 文件或者文件的路径
     */
    public static void zipCompress(File file) throws IOException, ArchiveException {
        compress(file, null, ArchiveStreamFactory.ZIP);
    }

    /***
     * 采用zip 压缩算法进行压缩文件或者文件目录名字加上后缀zip
     *
     * @param file 文件，后者文件目录
     * @throws IOException IOException
     * @throws ArchiveException ArchiveException
     */
    public static void zipCompress(String file) throws IOException, ArchiveException {
        zipCompress(file, null);
    }

    /***
     * 在用zip 压缩算发进行压缩，如存储文件为空则，采用目录名称或者文件名称加上zip后缀进行命名
     *
     * @param file
     *            文件，目录
     * @param dist
     *            存在目标文件
     * @throws IOException IOException
     * @throws ArchiveException ArchiveException
     */
    public static void zipCompress(String file, String dist) throws IOException, ArchiveException {
        if (file == null || file.trim().length() == 0) {
            throw new NullPointerException("源文件或者路径不能为空");
        }

        if (dist == null || dist.trim().length() == 0) {
            zipCompress(new File(file));
        } else {
            zipCompress(new File(file), new File(dist));
        }

    }


    /**
     * <p>
     * 功能描述：解压压缩文件
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 8:58
     * 修改记录：新建
     *
     * @param zipFile zipFile
     */
    public static void unZip(String zipFile) throws Exception {
        unZip(new File(zipFile), null);
    }


    /**
     * <p>
     * 功能描述：解压压缩文件
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 8:58
     * 修改记录：新建
     *
     * @param zipFile zipFile
     */
    public static void unZip(File zipFile) throws Exception {
        unZip(zipFile, null);
    }

    /**
     * <p>
     * 功能描述：解压文件至指定目录
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 8:59
     * 修改记录：新建
     *
     * @param zipfile zipfile
     * @param destDir destDir
     */
    public static void unZip(File zipfile, String destDir) throws Exception {

        if (StringUtils.isBlank(destDir)) {
            destDir = zipfile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        ZipFile zipFile = new ZipFile(zipfile);
        zipFile.setFileNameCharset("GBK");
        zipFile.extractAll(destDir);
    }

    /**
     * <p>
     * 功能描述：采用zip压缩算法对目录下面的文件进行压缩
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 8:59
     * 修改记录：新建
     *
     * @param file file
     * @param dist dist
     */
    public static void tarCompress(File file, File dist) throws IOException, ArchiveException {
        compress(file, dist, ArchiveStreamFactory.TAR);
    }

    /**
     * <p>
     * 功能描述：采用zip压缩算法对目录下面的文件进行压缩
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 8:59
     * 修改记录：新建
     *
     * @param file file
     */
    public static void tarCompress(File file) throws IOException, ArchiveException {
        compress(file, null, ArchiveStreamFactory.TAR);
    }

    /**
     * <p>
     * 功能描述：采用zip 压缩算法进行压缩 ,文件，后者文件目录名字加上后缀zip
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 9:00
     * 修改记录：新建
     *
     * @param file file
     */
    public static void tarCompress(String file) throws IOException, ArchiveException {
        tarCompress(file, null);
    }

    /**
     * <p>
     * 功能描述：在用zip 压缩算发进行压缩，如存储文件为空则，采用目录名称或者文件名称加上zip后缀进行命名
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 9:00
     * 修改记录：新建
     *
     * @param file file
     * @param dist dist
     */
    public static void tarCompress(String file, String dist) throws IOException, ArchiveException {
        if (file == null || file.trim().length() == 0) {
            throw new NullPointerException("源文件或者路径不能为空");
        }

        if (dist == null || dist.trim().length() == 0) {
            tarCompress(new File(file));
        } else {
            tarCompress(new File(file), new File(dist));
        }

    }

    /**
     * <p>
     * 功能描述：采用一定的压缩算法进行压缩
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 9:00
     * 修改记录：新建
     *
     * @param file file
     * @param dist dist
     * @param type type
     */
    private static void compress(File file, File dist, String type)
            throws IOException, ArchiveException {
        if (dist == null) {
            dist = new File(file.getParent(), file.getName() + "." + type);
        }
        if (!dist.exists()) {
            dist.createNewFile();
        }
        OutputStream out = new FileOutputStream(dist);
        try {
            ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(type, out);
            if (os instanceof ZipArchiveOutputStream) {
                ((ZipArchiveOutputStream) os).setEncoding("GBK");
            }
            if (os instanceof TarArchiveOutputStream) {
                ((TarArchiveOutputStream) os).setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
                ((TarArchiveOutputStream) os).setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_POSIX);
            }

            createArchiveEntry(file, null, os, out);
            os.finish();
        } finally {
            out.close();
        }
    }


    /**
     * <p>
     * 功能描述：采用一定的压缩算法进行压缩
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/26 9:00
     * 修改记录：新建
     *
     * @param file file
     * @param pre pre
     * @param os os
     * @param out out
     */
    private static void createArchiveEntry(File file, String pre, ArchiveOutputStream os, OutputStream out)
            throws IOException {
        String oripre = pre;
        if (file.isFile()) {
            ArchiveEntry entry = os.createArchiveEntry(file, pre + file.getName());
            os.putArchiveEntry(entry);
            try (FileInputStream input = new FileInputStream(file)) {
                IOUtils.copy(input, os);
            }
            os.closeArchiveEntry();
        } else {

            if (pre == null) {
                pre = "";
            } else {
                pre = pre + file.getName() + "\\";
            }
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    createArchiveEntry(f, pre, os, out);
                }
            } else {
                if (oripre == null) {
                    oripre = "";
                }
                ArchiveEntry entry = os.createArchiveEntry(file, oripre + file.getName());
                os.putArchiveEntry(entry);
                os.closeArchiveEntry();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        CompressUtil.unZip("D:\\temp\\新建文件夹.zip");
    }
}
