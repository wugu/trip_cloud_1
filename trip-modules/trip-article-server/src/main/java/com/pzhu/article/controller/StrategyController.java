package com.pzhu.article.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.article.domain.Strategy;
import com.pzhu.article.service.StrategyService;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/straregies")
public class StrategyController {


    private final StrategyService StrategyService;

    public StrategyController(StrategyService StrategyService) {
        this.StrategyService = StrategyService;
    }

    @GetMapping("query")
    public R<Page<Strategy>> pageList(Page<Strategy> page){
        return R.success(StrategyService.page(page));
    }

    @GetMapping("/detail")
    public R<Strategy> getById(Long id){
        return R.success(StrategyService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(Strategy Strategy){
        return R.success(StrategyService.save(Strategy));
    }

    @PostMapping("/update")
    public R<?> updateById(Strategy Strategy){
        return R.success(StrategyService.updateById(Strategy));
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        return R.success(StrategyService.removeById(id));
    }


}
