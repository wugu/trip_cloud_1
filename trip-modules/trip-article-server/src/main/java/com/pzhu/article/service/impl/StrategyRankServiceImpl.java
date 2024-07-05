package com.pzhu.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.Region;
import com.pzhu.article.domain.StrategyRank;
import com.pzhu.article.mapper.RegionMapper;
import com.pzhu.article.mapper.StrategyRankMapper;
import com.pzhu.article.service.RegionService;
import com.pzhu.article.service.StrategyRankService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyRankServiceImpl extends ServiceImpl<StrategyRankMapper, StrategyRank> implements StrategyRankService {

    @Override
    public List<StrategyRank> selectLastRanksByType(int type) {
        QueryWrapper<StrategyRank> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type)
                .orderByDesc("statis_time", "statisnum")
                .last("limit 10");
        return list(wrapper);

    }
}
