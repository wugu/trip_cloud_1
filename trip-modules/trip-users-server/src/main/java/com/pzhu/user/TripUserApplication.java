package com.pzhu.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.pzhu.user.mapper")
@SpringBootApplication
public class TripUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripUserApplication.class, args);
    }
}
