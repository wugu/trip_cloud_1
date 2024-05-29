package com.pzhu.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.domain.Destination;
import com.pzhu.qo.DestinationQuery;

import java.util.List;

public interface DestinationService extends IService<Destination> {

    List<Destination> getDestinationByRegionId(Long id);

    Page<Destination> pageList(DestinationQuery query);
}