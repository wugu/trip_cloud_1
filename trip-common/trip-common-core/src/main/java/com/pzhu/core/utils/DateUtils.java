package com.pzhu.core.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateUtils {

    // java 8 新的日期 api
    // 获取今天的最后一秒
    public static long getLastMillisSeconds(){
        // 开始时间
        LocalDateTime now = LocalDateTime.now();
        // 结束时间
        LocalDateTime lastSeconds = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
        return Duration.between(now, lastSeconds).toMillis(); // 间隔时间
    }


    /*public static long getLastMillisSeconds(){
        // 传统版本
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY,23);
        instance.set(Calendar.MINUTE,59);
        instance.set(Calendar.SECOND,59);
        long lastMillis = instance.getTimeInMillis();

        return lastMillis - System.currentTimeMillis();
    }*/
}
