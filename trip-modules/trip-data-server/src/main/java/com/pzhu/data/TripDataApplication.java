package com.pzhu.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启用 spring 任务调度
 */
@EnableScheduling
@MapperScan("com.pzhu.data.mapper")
@SpringBootApplication
public class TripDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripDataApplication.class, args);
    }
}
