package com.sand.core.util.idworker;

import org.springframework.beans.factory.annotation.Value;

/**
 * 功能说明：ID生成器 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/3/27 12:06 <br>
 * 功能描述：生成全局唯一ID <br>
 */
public class IdWorker {
    /**
     * 工作机器ID(0~31)
     */
    private static long workerId;
    /**
     * 数据中心ID(0~31)
     */
    private static long datacenterId;

    @Value("${snowflake.workerId:22}")
    public void setWorkerId(long workerId) {
        IdWorker.workerId = workerId;
    }

    @Value("${snowflake.datacenterId:33}")
    public void setDatacenterId(long datacenterId) {
        IdWorker.datacenterId = datacenterId;
    }

    /**
     * 通过雪花算法获取唯一ID
     *
     * @return ID
     */
    public static long getIdBySnowflake() {
        return SnowflakeIdWorker.getInstance(workerId, datacenterId).nextId();
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    System.out.println(Thread.currentThread().getName() + "：" + getIdBySnowflake());
                }
            }).start();
        }
    }
}
