package com.pzhu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient//启用服务注册，没写也生效
@SpringBootApplication
public class TripGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripGatewayApplication.class, args);
    }
}
