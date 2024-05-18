package com.pzhu.redis.key;

import lombok.Setter;
import java.util.concurrent.TimeUnit;

/**
 * 基础redis key 前缀类
 */
@Setter
public class BaseKeyPrefix implements KeyPrefix{

    private String prefix;
    private Integer timeout;
    private TimeUnit unit;

    public BaseKeyPrefix(String prefix){
        this(prefix,-1,null);
    }

    public BaseKeyPrefix(String prefix,Integer timeout,TimeUnit unit){
        this.prefix = prefix;
        this.timeout = timeout;
        this.unit = unit;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public Long getTimeout() {
        return Long.valueOf(timeout);
    }

    @Override
    public TimeUnit getUnit() {
        return unit;
    }
}
