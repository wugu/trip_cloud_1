package com.pzhu.article.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.domain.Destination;
import com.pzhu.domain.Region;
import com.pzhu.article.service.DestinationService;
import com.pzhu.article.service.RegionService;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {


    private final RegionService regionService;
    private final DestinationService destinationService;

    public RegionController(RegionService regionService,DestinationService destinationService) {
        this.regionService = regionService;
        this.destinationService = destinationService;
    }

    @GetMapping
    public R<Page<Region>> pageList(Page<Region> page){
        return R.success(regionService.page(page));
    }

    @GetMapping("/detail")
    public R<Region> getById(Long id){
        return R.success(regionService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(Region region){
        return R.success(regionService.save(region));
    }

    @PostMapping("/update")
    public R<?> updateById(Region region){
        return R.success(regionService.updateById(region));
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        return R.success(regionService.removeById(id));
    }

    /**
     * 根据区域id查询吗目的地
     * @param id
     * @return
     */
    @PostMapping("/{id}/destination")
    public R<List<Destination>> getDestinationByRegionId(@PathVariable Long id){
        List<Destination> destinations = destinationService.getDestinationByRegionId(id);
        return R.success(destinations);
    }
}
