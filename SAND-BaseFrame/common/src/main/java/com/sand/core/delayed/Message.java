package com.sand.core.delayed;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 功能说明：消息体定义 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/8/20 17:28 <br>
 * 功能描述：实现Delayed接口就是实现两个方法即compareTo 和 getDelay最重要的就是getDelay方法，这个方法用来判断是否到期 <br>
 */
public class Message implements Delayed {
    private int id;
    /**
     * 消息内容
     */
    private String body;
    /**
     * 延迟时长，这个是必须的属性因为要按照这个判断延时时长
     */
    private long executeTime;

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public Message(int id, String body, long delayTime) {
        this.id = id;
        this.body = body;
        this.executeTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    /**
     * <p>
     * 功能描述：自定义实现比较方法返回 1 0 -1三个参数
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/20 17:30
     * 修改记录：新建
     *
     * @param delayed delayed
     * @return int
     */
    @Override
    public int compareTo(Delayed delayed) {
        Message msg = (Message) delayed;
        return Integer.compare(this.id, msg.id);
    }

    /**
     * <p>
     * 功能描述：延迟任务是否到时就是按照这个方法判断如果返回的是负数则说明到期否则还没到期
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/20 17:31
     * 修改记录：新建
     *
     * @param unit unit
     * @return long
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.executeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }
}
