package com.pzhu.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients // 开启 openfeign
@ComponentScan(basePackages = "com.pzhu.core")
public class TripArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripArticleApplication.class, args);
    }
}
