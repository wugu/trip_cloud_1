package com.pzhu.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.Destination;
import com.pzhu.article.domain.Region;
import com.pzhu.article.mapper.DestinationMapper;
import com.pzhu.article.service.DestinationService;
import com.pzhu.article.service.RegionService;
import com.pzhu.article.qo.DestinationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {

    private final RegionService regionService;
    private final ThreadPoolExecutor businessThreadPoolExecutor;


    public DestinationServiceImpl(RegionService regionService, ThreadPoolExecutor businessThreadPoolExecutor) {
        this.regionService = regionService;
        this.businessThreadPoolExecutor = businessThreadPoolExecutor;
    }

    /**
     * 根据区域id查询目的地
     */
    public List<Destination> getDestinationByRegionId(Long regionId) {
        // 基于区域 id 查询区域对象
        Region region = regionService.getById(regionId);
        if (region == null){
            return Collections.emptyList();
        }
        // 基于区域对象，得到目的地 id 集合
        List<Long> ids = region.parseRefIds();
        if (ids.size() == 0){
            return Collections.emptyList();
        }
        // 基于目的地 id 集合，查询目的地集合并返回
        return super.listByIds(ids);
    }

    /**
     * 目的地分层查询
     * @param query
     * @return
     */
    public Page<Destination> pageList(DestinationQuery query) {
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();// 构建一个条件
        // 如果 parentId 为 null 就查询所有 parentId is null 得数据
        wrapper.isNull(query.getParentId() == null, "parent_id");
        // 如果 parentId 不为 null ，就根据 parentId 进行查询
        wrapper.eq(query.getParentId() != null, "parent_id", query.getParentId());// 传参
        // 关键字查询 关键字不为空进行模糊查询
        wrapper.like(!StringUtils.isEmpty(query.getKeyword()),"name", query.getKeyword());
        return super.page(
                new Page<>(query.getCurrent(), query.getSize()), wrapper
        );
    }

    /**
     * 目的地吐司查询
     * @param destId
     * @return
     */
    public List<Destination> findToasts(Long destId) {

        List<Destination> destinations = new ArrayList<>();
        while (destId != null){
            Destination dest = super.getById(destId);
            if (dest == null){
                break;
            }
            destinations.add(dest);
            destId = dest.getParentId();
        }
        Collections.reverse(destinations);// 将 list 集合翻转
        return destinations;
    }

    /**
     * 查询热门目的地
     * @param rid
     * @return
     */
    public List<Destination> findDestsByRid(Long rid) {
        List<Destination> destinations;
        // 查询国内的数据
        if (rid < 0){
            destinations = this.getBaseMapper().selectHotListByRid(rid, Collections.emptyList());
        }else {
            // 查询其他区域的数据
            Region region = regionService.getById(rid);
            if (region == null) {
                return Collections.emptyList();
            }
             destinations = this.getBaseMapper().selectHotListByRid(rid, region.parseRefIds());
        }
        // 对每一个子目的地集合进行剪裁， 只保留10条数据
        for (Destination destination : destinations) {
            List<Destination> children = destination.getChildren();
            if (children != null && children.size() > 10) {
                destination.setChildren(children.subList(0, 10));
            }
        }

        return destinations;
        /*
        // 查询所有目的地的下一级目的地
        // 如何等待所有异步线程都执行完成后，主线程再继续执行
        // 1. 创建一个倒计时对象 CountDownLatch 设置倒计时的值为目的地的数量
        CountDownLatch latch = new CountDownLatch(destinations.size());
        for (Destination destination : destinations) {
            // 运用线程提高sql查询效率，但一般不这样做，因为循环产生的sql太多，查询效率会变低
            // 采用未注释的方法，运用动态sql，进行连表查询
            businessThreadPoolExecutor.execute(() -> {// 异步
                // 清除之前的条件
                wrapper.clear();
                // 限制只查 10 条， lastSql 指在 sql 后面加上这一句
                List<Destination> children = list(wrapper.eq("parent_id", destination.getId()).last("limit 10"));
                destination.setChildren(children);

                // 2. 倒计时数量-1
                latch.countDown();
            });
        }

        try {
            // 3， 最终返回结果前，阻塞等待所有任务完成
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return destinations;*/
    }
}
