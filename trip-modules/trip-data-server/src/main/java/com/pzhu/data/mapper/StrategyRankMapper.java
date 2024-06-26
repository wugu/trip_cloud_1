package com.pzhu.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.article.domain.Strategy;
import com.pzhu.article.domain.StrategyRank;

import java.util.List;


public interface StrategyRankMapper extends BaseMapper<StrategyRank> {


    void batchInsert(List<StrategyRank> strategyRanks);
}
