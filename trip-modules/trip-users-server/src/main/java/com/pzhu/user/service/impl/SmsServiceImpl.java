package com.pzhu.user.service.impl;

import com.pzhu.redis.utils.RedisCache;
import com.pzhu.user.redis.key.UserRedisKeyPrefix;
import com.pzhu.user.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    private final RedisCache redisCache;

    public SmsServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 发送手机短信-验证码
     * @param phone
     */
    public void SmsSend(String phone) {
        //TODO 验证手机合法性

        // 生成验证码（纯数字， 字母+数字
        String code = this.generateVerifyCode("LETTER", 6);
        // 将验证码保存起来，半小时内有效
        UserRedisKeyPrefix keyPrefix = UserRedisKeyPrefix.USER_REGISTER_VERIFY_CODE_STRING;
        redisCache.setCacheObject(keyPrefix.fullKey(phone), code, keyPrefix.getTimeout(),keyPrefix.getUnit());
        // 调用第三方接口

    }

    private String generateVerifyCode(String type, int len) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");//uuid格式是xxx-xx-xxx，需要把横换为空
        String code = uuid.substring(0, len);//截取指定长度的子串作为最终的随机字符串
        log.info("[验证码] 生成验证码===》 type= {}， len= {}, code = {}",type,len,code);
        return code;
    }
}
