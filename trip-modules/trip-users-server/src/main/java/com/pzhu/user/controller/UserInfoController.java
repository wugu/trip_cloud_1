package com.pzhu.user.controller;

import com.pzhu.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserInfoController {
    /*@Autowired
    private UserInfoService userInfoService;*/

//  这种方法是构造器注入，好处是可以有爆红，在有空指针异常时
    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService){
        this.userInfoService = userInfoService;
    }


}
