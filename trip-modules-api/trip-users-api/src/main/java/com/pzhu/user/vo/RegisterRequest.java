package com.pzhu.user.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * 接收注册请求传递的参数
 */
@Getter
@Setter
public class RegisterRequest {

    private String phone;
    private String nickname;
    private String password;
    private String verifyCode;
}
