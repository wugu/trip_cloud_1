package com.pzhu.article.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.article.domain.Destination;
import com.pzhu.article.service.DestinationService;
import com.pzhu.core.utils.R;
import com.pzhu.article.qo.DestinationQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {


    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    /**
     * 分层分页，默认目的地没有上一级，点击可查看下一级目的地
     * @param query
     * @return
     */
    @GetMapping
    public R<Page<Destination>> pageList(DestinationQuery query){
        return R.success(destinationService.pageList(query));
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

    /**
     * 目的地吐司查询
     * @param destId 中国(最上级) id
     * @return
     */
    @GetMapping("/toasts")
    public R<List<Destination>> toasts(Long destId){
        return R.success(destinationService.findToasts(destId));
    }

    /**
     * 热门目的地查询
     * @param rid
     * @return
     */
    @GetMapping("/hotList")
    public R<List<Destination>> hotList(Long rid){
        return R.success(destinationService.findDestsByRid(rid));
    }
}
