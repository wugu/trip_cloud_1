package com.pzhu.article.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.Destination;
import com.pzhu.Region;
import com.pzhu.article.service.DestinationService;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {


    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public R<Page<Destination>> pageList(Page<Destination> page){
        return R.success(destinationService.page(page));
    }

    @GetMapping("/list")
    public R<List<Destination>> listAll(){
        return R.success(destinationService.list());
    }

    @GetMapping("/detail")
    public R<Destination> getById(Long id){
        return R.success(destinationService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(Destination destination){
        return R.success(destinationService.save(destination));
    }

    @PostMapping("/update")
    public R<?> updateById(Destination destination){
        return R.success(destinationService.updateById(destination));
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        return R.success(destinationService.removeById(id));
    }
}
