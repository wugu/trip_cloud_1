package com.pzhu.comment.controller;

import com.pzhu.comment.domain.TravelComment;
import com.pzhu.comment.service.TravelCommentService;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/travels/comments")
public class TravelCommentController {
    
    private final TravelCommentService travelCommentService;


    public TravelCommentController(TravelCommentService travelsCommentService) {
        this.travelCommentService = travelsCommentService;
    }

    @PostMapping("/save")
    public R<?> save(TravelComment comment){
        travelCommentService.save(comment);
        return R.success();
    }
}
