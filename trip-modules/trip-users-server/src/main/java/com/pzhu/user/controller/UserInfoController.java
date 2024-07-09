package com.pzhu.user.controller;

import com.pzhu.auth.anno.RequireLogin;
import com.pzhu.core.utils.R;
import com.pzhu.user.domain.UserInfo;
import com.pzhu.user.dto.UserInfoDTO;
import com.pzhu.user.service.UserInfoService;
import com.pzhu.user.vo.RegisterRequest;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping
    public List<UserInfo> test(){
        List<UserInfo> userInfos = userInfoService.list();
        return userInfos;
    }

    @GetMapping("/phone/exists")
    public R<Boolean> checkPhoneExists(String phone){//返回只需要存在或者不存在手机号，true/false
        return R.success(userInfoService.findByPhone(phone) != null);
    }

    @PostMapping("/login")
    public R<Map<String, Object>> login(String username, String password){//给前端返回一个map，里面有Token和用户
        Map<String, Object> map = userInfoService.login(username, password);
        return R.success(map);
    }

    @PostMapping("/register")
    public R<?> register(RegisterRequest req){//泛型问号表示返回数据无所谓
        userInfoService.register(req);
        return R.success();
    }

    /**
     * 得到 dto 对象，提供接口
     * @param id
     * @return
     */
    @GetMapping("/getById")
    public R<UserInfoDTO> getById(Long id){
        return R.success(userInfoService.getDtoById(id));
    }

    /**
     * 查询用户收藏文章 id 集合
     * @param userId
     * @return
     */
    @GetMapping("/favor/strategies")
    public R<List<Long>> getFavorStrategyIdList(Long userId){
        List<Long> list = userInfoService.getFavorStrategyIdList(userId);
        return R.success(list);
    }

    @RequireLogin
    @PostMapping("/favor/strategies")
    public R<Boolean> favoriteStrategy(Long sid){
        boolean ret = userInfoService.favoriteStrategy(sid);
        return R.success();
    }

}
