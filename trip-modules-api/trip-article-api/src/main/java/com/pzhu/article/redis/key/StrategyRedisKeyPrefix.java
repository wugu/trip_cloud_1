package com.pzhu.article.redis.key;

import com.pzhu.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class StrategyRedisKeyPrefix extends BaseKeyPrefix {

    public static final StrategyRedisKeyPrefix STRATEGIES_TOP_MAP =
            new StrategyRedisKeyPrefix("STRATEGIES:TOP"); // 置顶数 key 的前缀

    public static final StrategyRedisKeyPrefix STRATEGIES_STAT_DATA_MAP =
            new StrategyRedisKeyPrefix("STRATEGIES:STAT:DATA"); // 攻略 key 的前缀

    public static final StrategyRedisKeyPrefix STRATEGIES_STAT_COUNT_RANK_ZSET =
            new StrategyRedisKeyPrefix("STRATEGIES:STAT:COUNT:RANK"); // 统计数据 key 的前缀

    public StrategyRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public StrategyRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
