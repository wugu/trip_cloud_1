package com.pzhu.article.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.article.domain.Travel;
import com.pzhu.article.qo.TravelQuery;
import com.pzhu.article.service.TravelService;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/travels")
public class TravelController {


    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping("/query")
    public R<Page<Travel>> pageList(TravelQuery query, HttpServletRequest req){
        return R.success(travelService.pageList(query));
    }

    @GetMapping("/detail")
    public R<Travel> getById(Long id){
        return R.success(travelService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(Travel travel){
        return R.success(travelService.save(travel));
    }

    @PostMapping("/update")
    public R<?> updateById(Travel travel){
        return R.success(travelService.updateById(travel));
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        return R.success(travelService.removeById(id));
    }


}
