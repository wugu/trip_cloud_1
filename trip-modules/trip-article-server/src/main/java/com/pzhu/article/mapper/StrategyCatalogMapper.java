package com.pzhu.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.article.domain.StrategyCatalog;
import com.pzhu.article.vo.StrategyCatalogGroup;

import java.util.List;

public interface StrategyCatalogMapper extends BaseMapper<StrategyCatalog> {

    List<StrategyCatalogGroup> selectGroupList();
}
