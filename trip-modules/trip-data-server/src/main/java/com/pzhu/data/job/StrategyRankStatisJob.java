package com.pzhu.data.job;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pzhu.article.domain.Strategy;
import com.pzhu.article.domain.StrategyRank;
import com.pzhu.data.mapper.StrategyMapper;
import com.pzhu.data.mapper.StrategyRankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 */10 * * * *") // 每10分钟执行一次
    public void statisRank() {
        Date now = new Date();
        // 统计国内攻略
        List<StrategyRank> strategyRanks = strategyMapper.selectStrategyRankByAbroad(Strategy.ABROAD_NO);
        for (StrategyRank rank : strategyRanks) {
            rank.setType(StrategyRank.TYPE_CHINA);
            rank.setStatisTime(now);
        }
        // 保存到排名表中
        strategyRankMapper.batchInsert(strategyRanks);

        // 统计国外攻略
        strategyRanks = strategyMapper.selectStrategyRankByAbroad(Strategy.ABROAD_YES);
        for (StrategyRank rank : strategyRanks) {
            rank.setType(StrategyRank.TYPE_ABROAD);
            rank.setStatisTime(now);
        }
        // 保存到排名表中
        strategyRankMapper.batchInsert(strategyRanks);

        // 统计热门攻略
        strategyRanks = strategyMapper.selectStrategyRankHotList();
        for (StrategyRank rank : strategyRanks) {
            rank.setType(StrategyRank.TYPE_HOT);
            rank.setStatisTime(now);
        }
        // 保存到排名表中
        strategyRankMapper.batchInsert(strategyRanks);

        // 删除这次之前的所有数据
        strategyRankMapper.delete(new QueryWrapper<StrategyRank>().lt("statis_time", now));
    }
}
