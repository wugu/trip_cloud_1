package com.pzhu.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.article.domain.Banner;

import java.util.List;


public interface BannerService extends IService<Banner> {
    List<Banner> findByType(Integer type);
}
