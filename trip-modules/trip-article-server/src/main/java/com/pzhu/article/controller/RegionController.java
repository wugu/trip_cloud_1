package com.pzhu.article.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.Region;
import com.pzhu.article.service.RegionService;
import com.pzhu.core.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
public class RegionController {


    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public R<Page<Region>> pageList(Page<Region> page){
        return R.success(regionService.page(page));
    }
}
