package com.pzhu.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@MapperScan("com.pzhu.user.mapper")
@ComponentScan(basePackages = "com.pzhu.core")
@SpringBootApplication
public class TripUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripUserApplication.class, args);
    }
}
