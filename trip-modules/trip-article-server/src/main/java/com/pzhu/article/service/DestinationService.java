package com.pzhu.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.article.domain.Destination;
import com.pzhu.article.qo.DestinationQuery;

import java.util.List;

public interface DestinationService extends IService<Destination> {

    List<Destination> getDestinationByRegionId(Long id);

    Page<Destination> pageList(DestinationQuery query);

    List<Destination> findToasts(Long destId);


    List<Destination> findDestsByRid(Long rid);

}
