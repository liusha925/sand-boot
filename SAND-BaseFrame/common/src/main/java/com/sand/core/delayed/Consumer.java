package com.sand.core.delayed;

import java.util.concurrent.DelayQueue;

/**
 * 功能说明：消费者 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/8/20 17:31 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class Consumer implements Runnable {
    /**
     * 延时队列 ,消费者从其中获取消息进行消费
     */
    private DelayQueue<Message> queue;

    public Consumer(DelayQueue<Message> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message take = queue.take();
                System.out.println("消费消息id：" + take.getId() + " 消息体：" + take.getBody());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
