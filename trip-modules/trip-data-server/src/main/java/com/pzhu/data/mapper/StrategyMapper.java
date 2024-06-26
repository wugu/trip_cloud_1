package com.pzhu.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.article.domain.Strategy;
import com.pzhu.article.domain.StrategyRank;

import java.util.List;


public interface StrategyMapper extends BaseMapper<Strategy> {

    List<StrategyRank> selectStrategyRankByAbroad(Integer abroad);

    List<StrategyRank> selectStrategyRankHotList();
}
