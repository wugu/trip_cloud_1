package com.pzhu.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.article.domain.StrategyCatalog;
import com.pzhu.article.vo.StrategyCatalogGroup;

import java.util.List;


public interface StrategyCatalogService extends IService<StrategyCatalog> {

    List<StrategyCatalogGroup> findGroupList();

}
