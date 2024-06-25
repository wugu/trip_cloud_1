package com.pzhu.article.timer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskTest {


    @Test
    public void test() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("定时任务：+" + LocalDateTime.now());
            }
        }, 3000);
    }
}
