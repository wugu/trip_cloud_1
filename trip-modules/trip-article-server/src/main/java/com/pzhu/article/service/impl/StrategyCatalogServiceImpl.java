package com.pzhu.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.StrategyCatalog;
import com.pzhu.article.mapper.StrategyCatalogMapper;
import com.pzhu.article.service.StrategyCatalogService;
import com.pzhu.article.vo.StrategyCatalogGroup;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StrategyCatalogServiceImpl extends ServiceImpl<StrategyCatalogMapper, StrategyCatalog> implements StrategyCatalogService {


    @Override
    public List<StrategyCatalogGroup> findGroupList() {
        return getBaseMapper().selectGroupList();
    }
}
