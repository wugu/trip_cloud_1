package com.pzhu.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.article.domain.Region;
import com.pzhu.article.domain.Travel;
import com.pzhu.article.qo.TravelQuery;

import java.util.List;


public interface TravelService extends IService<Travel> {

    Page<Travel> pageList(TravelQuery query);
}
