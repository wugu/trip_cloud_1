package com.pzhu.user.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {


    private Long id;

    private String nickname; //昵称
    private String phone; //手机
    private String email; //邮箱


    private Integer gender; //性别
    private Integer level = 0; //用户等级
    private String city; //所在城市
    private String info; //个性签名
    private String headImgUrl; //头像

//    @JsonIgnore//不返回到前端和redis
    private Long loginTime; //登录时间
//    @JsonIgnore
    private Long expireTime; //过期时间
}
