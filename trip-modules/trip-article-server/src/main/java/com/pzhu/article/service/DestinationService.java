package com.pzhu.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.Destination;
import com.pzhu.Region;

import java.util.List;

public interface DestinationService extends IService<Destination> {

    List<Destination> getDestinationByRegionId(Long id);
}
