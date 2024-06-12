package com.pzhu.article.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.article.domain.Destination;
import com.pzhu.article.domain.StrategyTheme;
import com.pzhu.article.service.DestinationService;
import com.pzhu.article.service.StrategyThemeService;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/straregies/themes")
public class StrategyThemeController {


    private final StrategyThemeService StrategyThemeService;

    public StrategyThemeController(StrategyThemeService StrategyThemeService) {
        this.StrategyThemeService = StrategyThemeService;
    }

    @GetMapping("query")
    public R<Page<StrategyTheme>> pageList(Page<StrategyTheme> page){
        return R.success(StrategyThemeService.page(page));
    }

    @GetMapping("/detail")
    public R<StrategyTheme> getById(Long id){
        return R.success(StrategyThemeService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(StrategyTheme StrategyTheme){
        return R.success(StrategyThemeService.save(StrategyTheme));
    }

    @PostMapping("/update")
    public R<?> updateById(StrategyTheme StrategyTheme){
        return R.success(StrategyThemeService.updateById(StrategyTheme));
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        return R.success(StrategyThemeService.removeById(id));
    }


}
