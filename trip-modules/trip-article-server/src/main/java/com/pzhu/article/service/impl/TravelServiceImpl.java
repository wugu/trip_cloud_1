package com.pzhu.article.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.*;
import com.pzhu.article.feign.UserInfoFeignService;
import com.pzhu.article.mapper.TravelContentMapper;
import com.pzhu.article.mapper.TravelMapper;
import com.pzhu.article.qo.TravelQuery;
import com.pzhu.article.service.*;
import com.pzhu.article.vo.TravelRange;
import com.pzhu.core.utils.R;
import com.pzhu.user.dto.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements TravelService {

    private final TravelContentMapper travelContentMapper;
    private final ThreadPoolExecutor businessThreadPoolExecutor;
    private final UserInfoFeignService userInfoFeignService;

    public TravelServiceImpl(UserInfoFeignService userInfoFeignService, ThreadPoolExecutor businessThreadPoolExecutor, TravelContentMapper travelContentMapper) {
        this.userInfoFeignService = userInfoFeignService;
        this.businessThreadPoolExecutor = businessThreadPoolExecutor;
        this.travelContentMapper = travelContentMapper;
    }

    /**
     * 游记范围条件查询
     * @param query
     * @return
     */
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

        // 创建计数器，等待子线程都执行完成
        CountDownLatch latch = new CountDownLatch(records.size());

        for (Travel travel : records) {
            businessThreadPoolExecutor.execute( ()->{
                try { // 为什么使用 try-catch ，因为接口404会抛异常，会导致阻塞死，为了保证，所以加上 try-catch
                    // 查找游记作者
                    R<UserInfoDTO> result = userInfoFeignService.getById(travel.getAuthorId());
                    if (result.getCode() != R.CODE_SUCCESS){
                        log.warn("[游记服务] 查询用户作者失败，返回数据：{}", JSON.toJSONString(result));
                        // 数量-1
                        latch.countDown(); // 防止死锁，让查不到数据时候也数量-1
                        return;
                    }

                    travel.setAuthor(result.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 数量-1
                    latch.countDown();
                }

            });
        }
        // 返回前等待计数器值减到0,也表示所有子线程都执行结束
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return page;
    }

    /**
     * 游记详情查询
     * @param id
     * @return
     */
    @Override
    public Travel getById(Serializable id) {
        Travel travel = super.getById(id);
        if (travel == null){
            return null;
        }
        TravelContent travelContent = travelContentMapper.selectById(id);
        travel.setContent(travelContent);
        R<UserInfoDTO> result = userInfoFeignService.getById(travel.getAuthorId());
        UserInfoDTO dto = result.getAndCheck();
        travel.setAuthor(dto);
        return travel;
    }
}
