package com.pzhu.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.Destination;
import com.pzhu.article.mapper.DestinationMapper;
import com.pzhu.article.service.DestinationService;
import org.springframework.stereotype.Service;

@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {
}
