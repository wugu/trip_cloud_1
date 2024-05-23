package com.pzhu.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
@Configuration
public class JwtProperties {

    private Integer expireTime;//过期时间
    private String secret;//秘钥
}

