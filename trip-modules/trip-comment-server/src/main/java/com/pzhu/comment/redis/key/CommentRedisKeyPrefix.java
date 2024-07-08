package com.pzhu.comment.redis.key;

import com.pzhu.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class CommentRedisKeyPrefix extends BaseKeyPrefix {

    public static final CommentRedisKeyPrefix STRATEGIES_STAT_DATA_MAP =
            new CommentRedisKeyPrefix("STRATEGIES:STAT:DATA"); // key 的前缀

    public CommentRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public CommentRedisKeyPrefix(String prefix, Integer timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
