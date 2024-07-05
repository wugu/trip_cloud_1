package com.pzhu.auth.config;

import com.pzhu.auth.inceptor.LoginInterceptor;
import com.pzhu.auth.util.SpringContextUtil;
import com.pzhu.redis.utils.RedisCache;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Import 注解等同于以前 xml 文件中的 \<import resource="applicationContext.xml"/>
 */
@Configuration
@Import(WebConfig.class)
@EnableConfigurationProperties(JwtProperties.class)
public class tripJwtAutoConfiguration {

    @Bean
    public LoginInterceptor loginInterceptor(RedisCache redisCache, JwtProperties jwtProperties){
        return new LoginInterceptor(redisCache, jwtProperties);
    }

    @Bean
    public SpringContextUtil springContextUtil(){
        return new SpringContextUtil();
    }
}
