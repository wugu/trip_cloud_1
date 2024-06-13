package com.pzhu.article.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.article.domain.StrategyCatalog;
import com.pzhu.article.service.StrategyCatalogService;
import com.pzhu.article.vo.StrategyCatalogGroup;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/straregies/catalog")
public class StrategyCatalogController {


    private final StrategyCatalogService StrategyCatalogService;

    public StrategyCatalogController(StrategyCatalogService StrategyCatalogService) {
        this.StrategyCatalogService = StrategyCatalogService;
    }

    @GetMapping("/query")
    public R<Page<StrategyCatalog>> pageList(Page<StrategyCatalog> page){
        return R.success(StrategyCatalogService.page(page));
    }

    @GetMapping("/groups")
    public R<List<StrategyCatalogGroup>> groupList(){
        return R.success(StrategyCatalogService.findGroupList());
    }

    @GetMapping("/detail")
    public R<StrategyCatalog> getById(Long id){
        return R.success(StrategyCatalogService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(StrategyCatalog StrategyCatalog){
        return R.success(StrategyCatalogService.save(StrategyCatalog));
    }

    @PostMapping("/update")
    public R<?> updateById(StrategyCatalog StrategyCatalog){
        return R.success(StrategyCatalogService.updateById(StrategyCatalog));
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        return R.success(StrategyCatalogService.removeById(id));
    }
    

}
