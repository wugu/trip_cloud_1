package com.pzhu.article.listener;

import com.pzhu.article.domain.Strategy;
import com.pzhu.article.redis.key.StrategyRedisKeyPrefix;
import com.pzhu.article.service.StrategyService;
import com.pzhu.redis.utils.RedisCache;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RedisStatDataInitListener implements ApplicationListener<ContextRefreshedEvent> {// redis 统计数据监听器

    private final StrategyService strategyService;
    private final RedisCache redisCache;

    public RedisStatDataInitListener( StrategyService strategyService, RedisCache redisCache) {
         this.strategyService = strategyService;
        this.redisCache = redisCache;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        if (AnnotationConfigServletWebServerApplicationContext.class == ctx.getClass()){
            System.out.println("------容器启动完成执行初始化数据------");
            // 数据初始化
            // 查询所有攻略数据
            // TODO 不能一次加载表中所有数据，得分批加载，因为数据量大了，性能很慢，内存可能溢出
            List<Strategy> strategies = strategyService.list();
            System.out.println("redis攻略统计数初始化");
            System.out.println("统计数"+ strategies.size());
            int count = 0;
            // 遍历攻略列表，判断当前对象在 redis 中是否已经存在，不存在，将数据存入 redis
            for (Strategy strategy : strategies) {
                String fullKey = StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(strategy.getId() + "");
                Boolean exists = redisCache.hasKey(fullKey);
                if (!exists) {
                    // 不存在，将数据存入 redis
                    Map<String, Object> map = new HashMap<>();
                    map.put("viewnum",strategy.getViewnum());
                    map.put("replynum",strategy.getReplynum());
                    map.put("favornum",strategy.getFavornum());
                    map.put("sharenum",strategy.getSharenum());
                    map.put("thumbsupnum",strategy.getThumbsupnum());
                    redisCache.setCacheMap(fullKey, map);
                    count++;
                }
            }
            System.out.println("初始化："+count);
            System.out.println("------初始化数据完成------");
        }
    }
}
