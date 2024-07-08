package com.pzhu.comment.controller;

import com.pzhu.auth.anno.RequireLogin;
import com.pzhu.comment.domain.StrategyComment;
import com.pzhu.comment.service.StrategyCommentService;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strategies/comments")
public class StrategyCommentController {

    private final StrategyCommentService strategyCommentService;


    public StrategyCommentController(StrategyCommentService strategyCommentService) {
        this.strategyCommentService = strategyCommentService;
    }

    @RequireLogin
    @PostMapping("/save")
    public R<?> save(StrategyComment comment){ // 尽量不要用对象整个，可能会暴露表结构
        strategyCommentService.save(comment);
        return R.success();
    }
}
