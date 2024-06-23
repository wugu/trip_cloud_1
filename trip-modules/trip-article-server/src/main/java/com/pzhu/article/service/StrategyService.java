package com.pzhu.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.article.domain.Strategy;
import com.pzhu.article.domain.StrategyCatalog;
import com.pzhu.article.domain.StrategyContent;

import java.util.List;


public interface StrategyService extends IService<Strategy> {


    List<StrategyCatalog> findGroupByDestId(Long destId);

    StrategyContent getContentById(Long id);

    List<Strategy> findViewnumTop3ByDestId(Long destId);
}
