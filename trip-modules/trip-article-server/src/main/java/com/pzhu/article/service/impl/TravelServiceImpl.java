package com.pzhu.article.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.*;
import com.pzhu.article.feign.UserInfoFeignService;
import com.pzhu.article.mapper.TravelMapper;
import com.pzhu.article.qo.TravelQuery;
import com.pzhu.article.service.*;
import com.pzhu.article.vo.TravelRange;
import com.pzhu.core.utils.R;
import com.pzhu.user.dto.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements TravelService {

    private final UserInfoFeignService userInfoFeignService;

    public TravelServiceImpl(UserInfoFeignService userInfoFeignService) {
        this.userInfoFeignService = userInfoFeignService;
    }

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
        Page<Travel> page = super.page(new Page<>(query.getCurrent(), query.getSize()), wrapper);
        List<Travel> records = page.getRecords(); // 得到分页查询的记录条数
        for (Travel travel : records) {
            // 查找游记作者
            R<UserInfoDTO> result = userInfoFeignService.getById(travel.getAuthorId());
            if (result.getCode() != R.CODE_SUCCESS){
                log.warn("[游记服务] 查询用户作者失败，返回数据：{}", JSON.toJSONString(result));
                continue;
            }

            travel.setAuthor(result.getData());
        }
        return page;
    }
}
