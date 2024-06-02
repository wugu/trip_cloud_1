package com.pzhu.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.domain.Region;
import com.pzhu.article.mapper.RegionMapper;
import com.pzhu.article.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    /**
     * 热门区域查询
     * @return
     */
    public List<Region> findHotList() {

        // 查询所有热门区域，并且进行排序
        QueryWrapper queryWrapper = new QueryWrapper<Region>();
        queryWrapper.eq("ishot", Region.STATE_HOT);
        queryWrapper.orderByAsc("seq");
        return super.list(queryWrapper);
    }
}
