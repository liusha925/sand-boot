package com.sand.core.util.idworker;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * 功能说明：雪花算法 <br>
 * 开发人员：@author shaohua.huang <br>
 * 开发时间：2022/3/25 16:37 <br>
 * 功能描述：Twitter_Snowflake
 * SnowFlake的结构如下(每部分用-分开):
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 * 加起来刚好64位，为一个Long型。
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。 <br>
 */
public class SnowflakeIdWorker {
    /**
     * 开始时间截 (建议用服务第一次上线的时间，到毫秒级的时间戳)
     */
    private final long twepoch = 1648444544000L;
    /**
     * 工作机器id所占的位数
     */
    private final long workerIdBits = 5L;
    /**
     * 数据中心id所占的位数
     */
    private final long dataCenterIdBits = 5L;
    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);
    /**
     * 支持的最大数据中心id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxDataCenterId = ~(-1L << dataCenterIdBits);
    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;
    /**
     * 工作机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;
    /**
     * 数据中心id向左移17位(12+5)
     */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;
    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = ~(-1L << sequenceBits);
    /**
     * 工作机器ID(0~31)
     */
    private long workerId;
    /**
     * 数据中心ID(0~31)
     */
    private long dataCenterId;
    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;
    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;
    /**
     * 使用单例
     */
    private static SnowflakeIdWorker idWorker;

    static {
        idWorker = new SnowflakeIdWorker(getWorkId(), getDataCenterId());
    }

    /**
     * <p>
     * 功能描述：获取工作机器id
     * 理论上每台机器的计算出的是不一样的，但也可能出现重复的现象
     * </p>
     * 开发人员：@author shaohua.huang
     * 开发时间：2022/3/28 13:18
     * 修改记录：新建
     *
     * @return java.lang.Long
     */
    public static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums += b;
            }
            return (long) (sums % 32);
        } catch (UnknownHostException e) {
            // 如果获取失败，则使用随机数备用
            return RandomUtils.nextLong(0, 31);
        }
    }

    /**
     * <p>
     * 功能描述：获取数据中心Id
     * 理论上每台机器的计算出的是不一样的，但也可能出现重复的现象
     * </p>
     * 开发人员：@author shaohua.huang
     * 开发时间：2022/3/28 13:21
     * 修改记录：新建
     *
     * @return java.lang.Long
     */
    public static Long getDataCenterId() {
        try {
            int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
            int sums = 0;
            for (int i : ints) {
                sums += i;
            }
            return (long) (sums % 32);
        } catch (Exception e) {
            return RandomUtils.nextLong(0, 31);
        }
    }

    public SnowflakeIdWorker() {
        this.workerId = getWorkId();
        this.dataCenterId = getDataCenterId();
    }

    /**
     * <p>
     * 功能描述：构造函数
     * </p>
     * 开发人员：@author shaohua.huang
     * 开发时间：2022/3/28 13:32
     * 修改记录：新建
     *
     * @param workerId     工作机器ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    public SnowflakeIdWorker(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * <p>
     * 功能描述：获得下一个ID (该方法是线程安全的)
     * </p>
     * 开发人员：@author shaohua.huang
     * 开发时间：2022/3/28 13:33
     * 修改记录：新建
     *
     * @return long 生成的唯一id
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }
        // 上次生成ID的时间截
        lastTimestamp = timestamp;
        // 返回结果：移位并通过或运算拼到一起组成64位的ID
        // (timestamp - twepoch) << timestampLeftShift) 表示将时间戳减去初始时间戳，再左移相应位数
        // (datacenterId << datacenterIdShift) 表示将数据id左移相应位数
        // (workerId << workerIdShift) 表示将工作id左移相应位数
        // | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
        // 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * <p>
     * 功能描述：阻塞到下一个毫秒，直到获得新的时间戳
     * </p>
     * 开发人员：@author shaohua.huang
     * 开发时间：2022/3/28 13:34
     * 修改记录：新建
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return long 当前时间(毫秒)
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * <p>
     * 功能描述：返回以毫秒为单位的当前时间
     * </p>
     * 开发人员：@author shaohua.huang
     * 开发时间：2022/3/28 13:34
     * 修改记录：新建
     *
     * @return long 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * <p>
     * 功能描述：唯一id生成入口
     * 只能通过 SnowflakeIdWorker.generateId() 访问生成
     * </p>
     * 开发人员：@author shaohua.huang
     * 开发时间：2022/3/28 13:35
     * 修改记录：新建
     *
     * @return java.lang.Long 唯一id
     */
    public static synchronized Long generateId() {
        return idWorker.nextId();
    }

    /**
     * 验证
     */
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    System.out.println(Thread.currentThread().getName() + "：" + SnowflakeIdWorker.generateId());
                }
            }).start();
        }
    }

}