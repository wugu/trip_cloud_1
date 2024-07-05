package com.pzhu.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.article.domain.Region;
import com.pzhu.article.domain.StrategyRank;

import java.util.List;


public interface StrategyRankService extends IService<StrategyRank> {

    List<StrategyRank> selectLastRanksByType(int type);
}
