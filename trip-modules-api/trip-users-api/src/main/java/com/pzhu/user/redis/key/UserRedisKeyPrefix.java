package com.pzhu.user.redis.key;

import com.pzhu.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class UserRedisKeyPrefix extends BaseKeyPrefix {

    public static final UserRedisKeyPrefix STRATEGIES_STAT_DATA_MAP =
            new UserRedisKeyPrefix("STRATEGIES:STAT:DATA"); // 攻略 key 的前缀

    public static final UserRedisKeyPrefix USER_REGISTER_VERIFY_CODE_STRING =
            new UserRedisKeyPrefix("USER:REGISTER_VERIFY_CODE_STRING",30,TimeUnit.MINUTES);

    public static final UserRedisKeyPrefix USER_LOGIN_INFO_STRING =
            new UserRedisKeyPrefix("USER:LOGIN:INFO");

    public UserRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public UserRedisKeyPrefix(String prefix, Integer timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }

}
