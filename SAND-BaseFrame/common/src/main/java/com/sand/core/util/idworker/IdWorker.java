package com.sand.core.util.idworker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 功能说明：ID生成器 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2022/3/27 12:06 <br>
 * 功能描述：生成全局唯一ID <br>
 */
@Component
public class IdWorker {
    /**
     * 工作机器ID(0~31)
     */
    @Value("${snowflake.workerId:22}")
    private long workerId;
    /**
     * 数据中心ID(0~31)
     */
    @Value("${snowflake.datacenterId:23}")
    private long datacenterId;

    /**
     * 通过[雪花算法]获取唯一ID
     *
     * @return ID
     */
    public long getIdBySnowflake() {
        return SnowflakeIdWorker.getInstance(workerId, datacenterId).nextId();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    IdWorker idWorker = new IdWorker();
                    System.out.println(Thread.currentThread().getName() + "：" + idWorker.getIdBySnowflake());
                }
            }).start();
        }
    }
}
