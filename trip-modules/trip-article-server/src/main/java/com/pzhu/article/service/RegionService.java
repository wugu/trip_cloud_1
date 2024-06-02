package com.pzhu.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.domain.Region;

import java.util.List;


public interface RegionService extends IService<Region> {

    List<Region> findHotList();
}
