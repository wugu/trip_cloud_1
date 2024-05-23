package com.pzhu.user.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {

    public static final String TOKEN_HEADER = "token";
    public static final String LOGIN_USER_REDIS_UUID = "uuid";
    public static final long MINUTES_MILLISECONDS = 60 * 1000;
    public static final long TWENTY_MILLISECONDS = 20 * MINUTES_MILLISECONDS;


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
