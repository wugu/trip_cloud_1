package com.pzhu.auth.config;

import com.pzhu.auth.inceptor.LoginInterceptor;
import com.pzhu.redis.utils.RedisCache;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WebConfig.class)
@EnableConfigurationProperties(JwtProperties.class)
public class tripJwtAutoConfiguration {

    @Bean
    public LoginInterceptor loginInterceptor(RedisCache redisCache, JwtProperties jwtProperties){
        return new LoginInterceptor(redisCache, jwtProperties);
    }
}
