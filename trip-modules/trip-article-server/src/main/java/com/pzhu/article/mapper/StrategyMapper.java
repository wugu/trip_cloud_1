package com.pzhu.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.article.domain.Strategy;
import com.pzhu.article.domain.StrategyCatalog;

import java.util.List;

public interface StrategyMapper extends BaseMapper<Strategy> {

    List<StrategyCatalog> selectGroupByDestId(Long destId);
}
