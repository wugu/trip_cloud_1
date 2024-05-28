package com.pzhu.article.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.Destination;
import com.pzhu.Region;
import com.pzhu.article.service.RegionService;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/regions")
public class RegionController {


    private final RegionService regionService;
    private final Destination destinationService;

    public RegionController(RegionService regionService, Destination destinationService) {
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
}
