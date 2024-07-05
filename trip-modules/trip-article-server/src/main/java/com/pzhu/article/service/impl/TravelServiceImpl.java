package com.pzhu.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.*;
import com.pzhu.article.mapper.TravelMapper;
import com.pzhu.article.qo.TravelQuery;
import com.pzhu.article.service.*;
import com.pzhu.article.vo.TravelRange;
import org.springframework.stereotype.Service;



@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements TravelService {

    @Override
    public Page<Travel> pageList(TravelQuery query) {
        QueryWrapper<Travel> wrapper = Wrappers.<Travel>query()
                .eq(query.getDestId() != null, "dest_id", query.getDestId());

        if (query.getTravelTimeRange() != null){
            TravelRange range = query.getTravelTimeRange();
            wrapper.between("MONTH(travel_time)", range.getMin(), range.getMax()); // 得到当前月份，在范围之内,得到出发月份
        }

        if (query.getCostRange() != null) {
            TravelRange range = query.getCostRange();
            wrapper.between("avg_consume", range.getMin(), range.getMax()); // 人均花费在哪个范围内
        }

        if (query.getDayRange() != null) {
            TravelRange range = query.getDayRange();
            wrapper.between("day", range.getMin(), range.getMax()); // 旅游天数在哪个范围内
        }

        // 排序
        wrapper.orderByDesc(query.getOrderBy());

        return super.page(
                new Page<>(query.getCurrent(), query.getSize()), wrapper
        );
    }
}
