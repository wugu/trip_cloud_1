package com.pzhu.data.job;

import com.pzhu.article.domain.Strategy;
import com.pzhu.article.redis.key.StrategyRedisKeyPrefix;
import com.pzhu.data.mapper.StrategyMapper;
import com.pzhu.data.service.StrategyService;
import com.pzhu.redis.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@Component
public class StrategyStatDataPersistenceJob {

    private final RedisCache redisCache;
    private final StrategyService strategyService;

    public StrategyStatDataPersistenceJob(RedisCache redisCache, StrategyService strategyService) {
        this.redisCache = redisCache;
        this.strategyService = strategyService;
    }

    @Scheduled(cron = "0 0/10 * * * *")// 10分钟执行一次
    public void task(){
        log.info("[攻略数据持久化] ----持久化开始----");
        // 根据范围分数获取指定的成员
        Set<Integer> list = redisCache.zsetRevrange(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 0, Integer.MAX_VALUE);
        if (list != null && list.size() > 0){
            // 根据成员 id 拼接 key 取出统计数据
            List<Strategy> updateList = new ArrayList<>();
            for (Integer id : list) {
                Map<String, Object> map = redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
                // 将数据封装为攻略对象，将对象存入待更新的集合
                Strategy strategy = new Strategy();
                strategy.setViewnum((Integer) map.get("viewnum"));
                strategy.setSharenum((Integer) map.get("sharenum"));
                strategy.setReplynum((Integer) map.get("replynum"));
                strategy.setFavornum((Integer) map.get("favornum"));
                strategy.setThumbsupnum((Integer) map.get("thumbsupnum"));

                strategy.setId(id.longValue());

                updateList.add(strategy);
            }

            // 批量更新到数据库
            strategyService.updateBatchById(updateList, 100);

            // 删除已经更新过的成员
            redisCache.zsetRemoveRange(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 0, Integer.MAX_VALUE);
        }
        log.info("[攻略数据持久化] ----持久化结束----");
    }
}
