package com.sand.core.delayed.timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 功能说明：Timer延时处理 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/8/20 17:45 <br>
 * 功能描述：1、Timer在执行所有定时任务时只会创建一个线程。如果某个任务的执行时间长度大于其周期时间长度，那么就会导致这一次的任务还在执行，
 * 而下一个周期的任务已经需要开始执行了，当然在一个线程内这两个任务只能顺序执行，有两种情况：对于之前需要执行但还没有执行的任务，
 * 一是当前任务执行完马上执行那些任务（按顺序来），二是干脆把那些任务丢掉，不去执行它们。至于具体采取哪种做法，需要看是调用schedule还是scheduleAtFixedRate
 * <p>
 * 2、Timer线程是不会捕获异常的，如果TimerTask抛出的了未检查异常则会导致Timer线程终止，同时Timer也不会重新恢复线程的执行，他会错误的认为整个Timer线程都会取消。
 * 同时，已经被安排单尚未执行的TimerTask也不会再执行了，新的任务也不能被调度。故如果TimerTask抛出未检查的异常，Timer将会产生无法预料的行为 <br>
 */
public class TimerDelayed {

    private void testTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("业务延时处理");
            }
        }, (3 * 1000));
    }

    public static void main(String[] args) throws InterruptedException {
        new TimerDelayed().testTimer();
    }
}
