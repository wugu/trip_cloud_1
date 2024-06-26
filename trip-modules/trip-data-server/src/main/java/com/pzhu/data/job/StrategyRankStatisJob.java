package com.pzhu.data.job;


import com.pzhu.data.mapper.StrategyMapper;
import com.pzhu.data.mapper.StrategyRankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 攻略排行统计数据
 */
@Component
public class StrategyRankStatisJob {

    private final StrategyMapper strategyMapper;
    private final StrategyRankMapper strategyRankMapper;

    public StrategyRankStatisJob(StrategyMapper strategyMapper, StrategyRankMapper strategyRankMapper) {
        this.strategyMapper = strategyMapper;
        this.strategyRankMapper = strategyRankMapper;
    }
    @Scheduled(cron = "0 */10 * * * *") // 每10分钟执行一次
    public void statisRank(){

    }
}
