package com.pzhu.article.timer;

import jdk.nashorn.internal.ir.Block;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskTest {


    @Test
    public void test() {

        // 对象锁
        final Object lock = new Object();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("定时任务：+" + LocalDateTime.now());

                synchronized (TimerTaskTest.class){
                    lock.notify();
                }
            }
        }, 3000);

        System.out.println("锁之前时间："+ LocalDateTime.now());
        synchronized (TimerTaskTest.class){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
