package com.pzhu.comment.controller;

import com.pzhu.comment.service.TravelCommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/travels/comments")
public class TravelCommentController {
    
    private final TravelCommentService travelCommentService;


    public TravelCommentController(TravelCommentService travelsCommentService) {
        this.travelCommentService = travelsCommentService;
    }
}
