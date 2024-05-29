package com.pzhu.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.Destination;
import com.pzhu.Region;
import com.pzhu.article.mapper.DestinationMapper;
import com.pzhu.article.service.DestinationService;
import com.pzhu.article.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {

    private final RegionService regionService;

    public DestinationServiceImpl(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * 根据区域id查询目的地
     */
    public List<Destination> getDestinationByRegionId(Long regionId) {
        // 基于区域 id 查询区域对象
        Region region = regionService.getById(regionId);
        if (region == null){
            return Collections.emptyList();
        }
        // 基于区域对象，得到目的地 id 集合
        List<Long> ids = region.parseRefIds();
        if (ids.size() == 0){
            return Collections.emptyList();
        }
        // 基于目的地 id 集合，查询目的地集合并返回
        return super.listByIds(ids);
    }
}
