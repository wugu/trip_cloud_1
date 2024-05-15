package com.pzhu.redis.config;

import com.pzhu.redis.utils.RedisCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 如果spring容器中找不到RedisConnectionFactory类型都bean对象
 * 那么这个整个配置对象都不会生效
 */
@ConditionalOnMissingBean(RedisConnectionFactory.class)
@Configuration
public class Wolf2wRedisAutoConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置redis key 的序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        //设置hash key 的序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        //设置value的序列化方式
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }

    @Bean
    public RedisCache redisCache(RedisConnectionFactory redisConnectionFactory){
        return new RedisCache(redisTemplate(redisConnectionFactory));
    }
}
