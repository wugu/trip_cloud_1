package com.pzhu.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.Strategy;
import com.pzhu.data.mapper.StrategyMapper;
import com.pzhu.data.service.StrategyService;
import org.springframework.stereotype.Service;


@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {
}
