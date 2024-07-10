package com.pzhu.article.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.Banner;
import com.pzhu.article.mapper.BannerMapper;
import com.pzhu.article.service.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {


    @Override
    public List<Banner> findByType(Integer type) {

        return list(new QueryWrapper<Banner>()
                .eq("type", type)
                .eq("state", Banner.STATE_NORMAL)
                .orderByAsc("seq")
        );
    }
}
