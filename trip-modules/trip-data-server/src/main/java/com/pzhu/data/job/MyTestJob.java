package com.pzhu.data.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyTestJob {

    @Scheduled(fixedDelay = 2000) // 2秒
    public void printTime(){
        System.out.println("当前时间"+ LocalDateTime.now());
    }
}
